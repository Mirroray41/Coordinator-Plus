package net.zapp;

import net.minecraft.client.MinecraftClient;
import net.zapp.options.CoordinatorOptions;

public class InfoText {
    private final MinecraftClient client;

    public InfoText(MinecraftClient client) {
        this.client = client;
    }

    public String get() {
        double x = client.getCameraEntity().getX();
        double y = client.getCameraEntity().getY();
        double z = client.getCameraEntity().getZ();

        if (client.getCameraEntity() != null) {
            return formatCoordinate(x) + " " + formatCoordinate(y) + " " + formatCoordinate(z);
        }
        return "";
    }

    private static Number formatCoordinate(double coordinate) {
        return switch (CoordinatorPlusClient.options.getCoordinate().getValue()) {
            case WHOLE -> (int) Math.floor(coordinate);
            case TWO_DECIMAL -> (double) Math.round(coordinate*100)/100D;
        };
    }
}