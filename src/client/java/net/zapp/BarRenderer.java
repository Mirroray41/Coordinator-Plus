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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

