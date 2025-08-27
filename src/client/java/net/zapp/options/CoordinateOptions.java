package net.zapp.options;

import net.minecraft.util.Identifier;
import net.minecraft.util.TranslatableOption;
import net.minecraft.util.function.ValueLists;
import net.zapp.CoordinatorPlusClient;

import java.util.function.IntFunction;

public enum CoordinateOptions implements TranslatableOption {
    WHOLE(0, Identifier.of(CoordinatorPlusClient.MOD_ID, "options.coordinate.whole").toTranslationKey()),
    TWO_DECIMAL(1, Identifier.of(CoordinatorPlusClient.MOD_ID, "options.coordinate.two_decimal").toTranslationKey());

    private static final IntFunction<CoordinateOptions> BY_ID = ValueLists.createIdToValueFunction(CoordinateOptions::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);
    private final int id;
    private final String name;

    CoordinateOptions(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.name;
    }

    public static CoordinateOptions get(int id) {
        return (CoordinateOptions)BY_ID.apply(id);
    }
}
