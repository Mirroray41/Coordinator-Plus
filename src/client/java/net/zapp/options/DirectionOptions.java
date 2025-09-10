package net.zapp.options;

import net.minecraft.client.render.ChunkBuilderMode;
import net.minecraft.util.Identifier;
import net.minecraft.util.TranslatableOption;
import net.minecraft.util.function.ValueLists;
import net.zapp.CoordinatorPlusClient;

import java.util.function.IntFunction;

public enum DirectionOptions implements TranslatableOption {
    CARDINAL(0, Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction.cardinal").toTranslationKey()),
    PREDICT(1, Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction.predict").toTranslationKey()),
    CARDINAL_AND_PREDICT(2, Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction.cardinal_and_predict").toTranslationKey());


    private static final IntFunction<DirectionOptions> BY_ID = ValueLists.createIndexToValueFunction(DirectionOptions::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);
    private final int id;
    private final String name;

    DirectionOptions(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.name;
    }

    public static DirectionOptions get(int id) {
        return (DirectionOptions)BY_ID.apply(id);
    }
}
