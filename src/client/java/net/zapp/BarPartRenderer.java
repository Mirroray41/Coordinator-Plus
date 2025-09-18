package net.zapp;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BarPartRenderer extends AbstractPartRenderer {
    private final DrawContext context;
    private final MinecraftClient client;
    private static BufferedImage tintTemplate;
    private static final Identifier identifier = Identifier.of(CoordinatorPlusClient.MOD_ID, "textures/gui/sprites/tint_bar.png");

    public BarPartRenderer(MinecraftClient client, DrawContext context) {
        super(client, context);
        this.context = context;
        this.client = client;

        Path path = FabricLoader.getInstance().getModContainer(CoordinatorPlusClient.MOD_ID).get().findPath(String.valueOf(Path.of("assets",identifier.getNamespace(),identifier.getPath()))).get();

        try {
            tintTemplate = createImageFromBytes(Files.readAllBytes(path));// /assets/coordinator-plus/textures/gui/sprites/tint_bar.png
        } catch (IOException e) {
            System.out.println("Couldn't find tint template");
        }
    }

    @Override
    public void render(float currentYaw, int offset) throws IOException {


        //context.drawVerticalLine((context.getScaledWindowWidth() / 2) - offset, 13, 17, ColorHelper.getArgb(red, green, blue));

        //System.out.println(tintTemplate.getWidth());

        for (int xOffset = tintTemplate.getWidth()-1 ; xOffset >= 0 ; xOffset--) {
            //System.out.println(xOffset);
            int xOrigin = (context.getScaledWindowWidth() / 2) - (tintTemplate.getWidth() / 2);

            float scaledOffset = (float) ((tintTemplate.getWidth() / 2) - xOffset) / context.getScaledWindowWidth() * 180;

            int south = getProximityToYaw(currentYaw, scaledOffset, 0);
            int southWest = getProximityToYaw(currentYaw, scaledOffset, 45);
            int west = getProximityToYaw(currentYaw, scaledOffset, 90);
            int northWest = getProximityToYaw(currentYaw, scaledOffset, 135);
            int north = getProximityToYaw(currentYaw, scaledOffset, 180);
            int northEast = getProximityToYaw(currentYaw, scaledOffset, 225);
            int east = getProximityToYaw(currentYaw, scaledOffset, 270);
            int southEast = getProximityToYaw(currentYaw, scaledOffset, 315);

            int red = Math.max(255 - (south + east + southEast + (northEast / 2)), 0);
            int green = Math.max(255 - (west + south + southWest + (northWest / 2)), 0);
            int blue = Math.max(255 - (west + north + east + northEast + northWest), 0);

            for (int yOffset = 0 ; yOffset < tintTemplate.getHeight()  ; yOffset++) {
                int yOrigin = 14;
                if (tintTemplate.getRGB(xOffset, yOffset) != 0) {
                    float scale = (float) ColorHelper.getRed(tintTemplate.getRGB(xOffset, yOffset)) / 255;
                    colorPixel(xOrigin + xOffset, yOrigin + yOffset, ColorHelper.getArgb(Math.round(red * scale), Math.round(green * scale), Math.round(blue * scale)));
                }
            }
        }
    }

    private void colorPixel(int x, int y, int color) {
        context.fill(x - 1 ,y - 1, x, y, color);
    }

    private static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException("Error reading image data", e);
        }
    }

    private static BufferedImage readPngFromZip(Path zipFilePath, Path imagePath) {
        try (ZipFile zipFile = new ZipFile(String.valueOf(zipFilePath))) {
            ZipEntry entry = zipFile.getEntry(String.valueOf(imagePath));
            if (entry == null) {
                throw new FileNotFoundException("PNG file not found in ZIP archive: " + imagePath);
            }

            try (InputStream inputStream = zipFile.getInputStream(entry)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];

                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                return createImageFromBytes(baos.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading PNG from ZIP file", e);
        }
    }

    public static void applyResourcePackTintTemplate(MinecraftClient client) {
        Collection<String> enabledPacks = client.getResourcePackManager().getEnabledIds();
        Collection<String> customPacks = enabledPacks.stream()
                .filter(s -> s != null && s.startsWith("file/"))
                .toList();
        List<Path> modifyingTint = new ArrayList<>();
        for (String pack : customPacks) {
            String fileName = pack.split("/")[1];

            Path filePath = client.getResourcePackDir().resolve(fileName);
            if (containsFile(filePath, Path.of("assets",identifier.getNamespace(),identifier.getPath()))) {
                modifyingTint.add(filePath);
            }
        }


        if (!modifyingTint.isEmpty()){
            tintTemplate = readPngFromZip(modifyingTint.getLast(), Path.of("assets",identifier.getNamespace(),identifier.getPath()));
        } else {
            Path path = FabricLoader.getInstance().getModContainer(CoordinatorPlusClient.MOD_ID).get().findPath(String.valueOf(Path.of("assets",identifier.getNamespace(),identifier.getPath()))).get();

            try {
                tintTemplate = createImageFromBytes(Files.readAllBytes(path));// /assets/coordinator-plus/textures/gui/sprites/tint_bar.png
            } catch (IOException e) {
                System.out.println("Couldn't find tint template");
            }
        }
    }

    public static boolean containsFile(Path zipPath, Path filePath) {
        try (ZipFile zipFile = new ZipFile(String.valueOf(zipPath))) {
            return zipFile.getEntry(String.valueOf(filePath)) != null;
        } catch (IOException e) {
            throw new RuntimeException("Error reading ZIP file", e);
        }
    }

    private int getProximityToYaw(float currentYaw, float scaledOffset, int offset) {
        return Math.max(255 - Math.round(Math.abs(MathHelper.wrapDegrees((currentYaw - offset) - scaledOffset) / 180 * context.getScaledWindowWidth())), 0);
    }
}
