package net.zapp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zapp.options.DirectionOptions;

@Environment(EnvType.CLIENT)
public final class CoordinateBarHud {
    private static CoordinateBarHud INSTANCE;

    private final MinecraftClient client;

    final BarRenderer renderer;

    final InfoText text;

    private static final Identifier OVERLAY = Identifier.of(CoordinatorPlusClient.MOD_ID, "textures/gui/sprites/overlay.png");
    private static final Identifier CONCAT_PATTERN = Identifier.of(CoordinatorPlusClient.MOD_ID, "directions.concat");


    public CoordinateBarHud (MinecraftClient client, DrawContext context) {
        this.client = client;
        this.renderer = new BarRenderer(client, context);
        this.text = new InfoText(client);
    }

    public static CoordinateBarHud getInstance(MinecraftClient client, DrawContext context) {
        if(INSTANCE == null) {
            INSTANCE = new CoordinateBarHud(client, context);
        }

        return INSTANCE;
    }

    public void render(DrawContext context) {
        if (client.getCameraEntity() != null) {
            Entity entity = client.getCameraEntity();
            Direction direction = entity.getHorizontalFacing();
            float yaw = entity.getYaw();

            renderer.renderBarPart(yaw, 0);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, OVERLAY, (context.getScaledWindowWidth() / 2) - 92, 13, 0, 0, 182, 5, 182, 5);
            for (int i = 0 ; i < 120 ; i++) {
                renderer.renderLine(yaw, i * 3);
            }
            for (int i = 0 ; i < 24 ; i++) {
                renderer.renderSemiCardinalLine(yaw, i * 15);
            }
            renderer.renderCardinalLine(yaw,0,  getTranslatedDirection("south"), 0, 0, 255);
            renderer.renderCardinalLine(yaw,45, getTranslatedDirection("south_east"), 0, 255, 255);
            renderer.renderCardinalLine(yaw,90, getTranslatedDirection("east"), 0, 255, 0);
            renderer.renderCardinalLine(yaw,135, getTranslatedDirection("north_east"), 127, 255, 0);
            renderer.renderCardinalLine(yaw,180, getTranslatedDirection("north"), 255, 255, 0);
            renderer.renderCardinalLine(yaw,225, getTranslatedDirection("north_west"), 255, 127, 0);
            renderer.renderCardinalLine(yaw,270, getTranslatedDirection("west"), 255, 0, 0);
            renderer.renderCardinalLine(yaw,315, getTranslatedDirection("south_west"), 255, 0, 255);

//            String coordinates = (int) Math.floor(client.getCameraEntity().getX()) + " " + (int) Math.floor(client.getCameraEntity().getY()) + " " + (int) Math.floor(client.getCameraEntity().getZ());
            context.drawText(client.textRenderer, text.get(), (context.getScaledWindowWidth() / 2) - (client.textRenderer.getWidth(text.get()) / 2), 2, 0xFFFFFFFF, true);
        }
    }

    private Text getTranslatedDirection(String identifier) {
        Text shortDir = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "directions." + identifier + ".short").toTranslationKey());
        Text predictDir = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "directions." + identifier + ".predict").toTranslationKey());
        return switch (CoordinatorPlusClient.options.getDirection().getValue()) {
            case CARDINAL -> shortDir;
            case PREDICT -> predictDir;
            case CARDINAL_AND_PREDICT -> Text.translatable(CONCAT_PATTERN.toTranslationKey(), shortDir, predictDir);
        };
    }
}
