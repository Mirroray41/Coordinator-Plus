package net.zapp;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class BarRenderer {
    private final BarPartRenderer barPartRenderer;
    private final LineRenderer lineRenderer;
    private final CardinalLineRenderer cardinalLineRenderer;
    private final SemiCardinalLineRenderer semiCardinalLineRenderer;

    public BarRenderer(MinecraftClient client, DrawContext context) {
        this.barPartRenderer = new BarPartRenderer(client, context);
        this.lineRenderer = new LineRenderer(client, context);
        this.cardinalLineRenderer = new CardinalLineRenderer(client, context);
        this.semiCardinalLineRenderer = new SemiCardinalLineRenderer(client, context);
    }

    public void renderBarPart(float currentYaw, int offset) {
        try {
            this.barPartRenderer.render(currentYaw, offset);
        } catch (IOException e) {
            System.out.println("Couldn't find bar tint template");
        }
    }

    public void renderLine(float currentYaw, int offset) {
        this.lineRenderer.render(currentYaw, offset);
    }

    public void renderSemiCardinalLine(float currentYaw, int offset) {
        this.semiCardinalLineRenderer.render(currentYaw, offset);
    }

    public void renderCardinalLine(float currentYaw, int offset, Text text, int red, int green, int blue) {
        this.cardinalLineRenderer.render(currentYaw, offset, text, red, green, blue);
    }
}

abstract class AbstractPartRenderer {
    public AbstractPartRenderer(MinecraftClient client, DrawContext context) {
    }

    public abstract void render(float currentYaw, int offset) throws IOException;

    public static int scaleAlpha(int windowWidth, float wrappedYaw, int initialOpacity, int intensity) {
        if (Math.abs(wrappedYaw / 180 * windowWidth) < 91) {
            return initialOpacity;
        } else {
            return Math.max(initialOpacity - (Math.round(Math.abs(wrappedYaw / 180 * windowWidth) - 91) * intensity), 0);
        }
    }
}

abstract class AbstractTexturedPartRenderer {
    public AbstractTexturedPartRenderer(MinecraftClient client, DrawContext context, Identifier textureIdentifier) {
    }

    public abstract void render(float currentYaw, int offset);
}

class BarPartRenderer extends AbstractPartRenderer {
    private final DrawContext context;

    public BarPartRenderer(MinecraftClient client, DrawContext context) {
        super(client, context);
        this.context = context;
    }

    @Override
    public void render(float currentYaw, int offset) throws IOException {
        /*float scaledOffset = (float) (offset) / context.getScaledWindowWidth() * 180;

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

        context.drawVerticalLine((context.getScaledWindowWidth() / 2) - offset, 13, 17, ColorHelper.getArgb(red, green, blue));*/

        Identifier identifier = Identifier.of(CoordinatorPlusClient.MOD_ID, "textures/gui/sprites/tint_bar.png");
        Path path = FabricLoader.getInstance().getGameDir().resolve(Path.of("assets",identifier.getNamespace(),identifier.getPath()));
        System.out.println(path);
        BufferedImage tint_template = ImageIO.read(path.toFile());// /assets/coordinator-plus/textures/gui/sprites/tint_bar.png
        context.fill(1 ,1, 2, 2, ColorHelper.getArgb(0, 255, 0));
    }

    private int getProximityToYaw(float currentYaw, float scaledOffset, int offset) {
        return Math.max(255 - Math.round(Math.abs(MathHelper.wrapDegrees((currentYaw - offset) - scaledOffset) / 180 * context.getScaledWindowWidth())), 0);
    }
}

class LineRenderer extends AbstractPartRenderer {
    private final DrawContext context;

    public LineRenderer(MinecraftClient client, DrawContext context) {
        super(client, context);
        this.context = context;
    }

    @Override
    public void render(float currentYaw, int offset) {
        int y = 11;
        int height = 7;
        float wrappedYaw = MathHelper.wrapDegrees(currentYaw + offset);
        int alpha = scaleAlpha(context.getScaledWindowWidth(), wrappedYaw, 170, 3);

        context.drawVerticalLine(((context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth())), y, y + height, ColorHelper.getArgb(alpha, 236, 236, 236));
        context.drawVerticalLine(((context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth())) + 1, y + 1, y + height + 1, ColorHelper.getArgb(alpha, 236 / 2, 236 / 2, 236 / 2));
    }
}

class SemiCardinalLineRenderer extends AbstractPartRenderer {
    private final DrawContext context;

    public SemiCardinalLineRenderer(MinecraftClient client, DrawContext context) {
        super(client, context);
        this.context = context;
    }

    @Override
    public void render(float currentYaw, int offset) {
        int y = 10;
        int height = 9;
        float wrappedYaw = MathHelper.wrapDegrees(currentYaw + offset);
        int alpha = scaleAlpha(context.getScaledWindowWidth(), wrappedYaw, 255, 4);

        context.drawVerticalLine(((context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth())), y, y + height, ColorHelper.getArgb(alpha, 255, 255, 255));
        context.drawVerticalLine(((context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth())) + 1, y + 1, y + height + 1, ColorHelper.getArgb(alpha, 255 / 2, 255 / 2, 255 / 2));
    }
}

class CardinalLineRenderer extends AbstractPartRenderer {
    private final MinecraftClient client;
    private final DrawContext context;

    public CardinalLineRenderer(MinecraftClient client, DrawContext context) {
        super(client, context);
        this.client = client;
        this.context = context;
    }

    @Override
    public void render(float currentYaw, int offset) {
        render(currentYaw, offset, Text.empty(), 255, 255, 255);
    }

    public void render(float currentYaw, int offset, Text text, int red, int green, int blue) {
        int y = 9;
        int height = 11;
        float wrappedYaw = MathHelper.wrapDegrees(currentYaw + offset);
        int alpha = scaleAlpha(context.getScaledWindowWidth(), wrappedYaw, 255, 4);

        context.drawVerticalLine(((context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth())), y, y + height, ColorHelper.getArgb(alpha, red, green, blue));
        context.drawVerticalLine(((context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth())) + 1, y + 1, y + height + 1, ColorHelper.getArgb(alpha, red / 2, green / 2, blue / 2));
        context.drawText(client.textRenderer, text, (context.getScaledWindowWidth() / 2) - Math.round(wrappedYaw / 180 * context.getScaledWindowWidth()) - (client.textRenderer.getWidth(text) / 2) + 1,y + height + 2, ColorHelper.getArgb(Math.max(alpha, 10), red, green, blue), true);
    }
}

