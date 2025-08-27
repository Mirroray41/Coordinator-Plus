package net.zapp.options;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zapp.CoordinatorPlusClient;

import java.io.File;
import java.util.Arrays;

public class CoordinatorOptions extends GameOptions {
    private final SimpleOption<Boolean> coordinatorToggle = SimpleOption.ofBoolean(Identifier.of(CoordinatorPlusClient.MOD_ID, "options.toggle").toTranslationKey(), true);

    private final Text CARDINAL_TOOLTIP = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction.cardinal.tooltip").toTranslationKey());
    private final Text PREDICT_TOOLTIP = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction.predict.tooltip").toTranslationKey());
    private final Text CARDINAL_AND_PREDICT_TOOLTIP = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction.cardinal_and_predict.tooltip").toTranslationKey());
    private final SimpleOption<DirectionOptions> direction = new SimpleOption<>(
            Identifier.of(CoordinatorPlusClient.MOD_ID, "options.direction").toTranslationKey(),
            value -> switch (value) {
                case DirectionOptions.CARDINAL -> Tooltip.of(CARDINAL_TOOLTIP);
                case DirectionOptions.PREDICT -> Tooltip.of(PREDICT_TOOLTIP);
                case DirectionOptions.CARDINAL_AND_PREDICT -> Tooltip.of(CARDINAL_AND_PREDICT_TOOLTIP);
            },
            SimpleOption.enumValueText(),
            new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(DirectionOptions.values()), Codec.INT.xmap(DirectionOptions::get, DirectionOptions::getId)),
            DirectionOptions.CARDINAL,
            value -> {}
    );

    private final Text WHOLE_TOOLTIP = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "options.coordinate.whole.tooltip").toTranslationKey());
    private final Text TWO_DECIMAL_TOOLTIP = Text.translatable(Identifier.of(CoordinatorPlusClient.MOD_ID, "options.coordinate.two_decimal.tooltip").toTranslationKey());
    private final SimpleOption<CoordinateOptions> coordinate = new SimpleOption<>(
            Identifier.of(CoordinatorPlusClient.MOD_ID, "options.coordinate").toTranslationKey(),
            value -> switch (value) {
                case CoordinateOptions.WHOLE -> Tooltip.of(WHOLE_TOOLTIP);
                case CoordinateOptions.TWO_DECIMAL -> Tooltip.of(TWO_DECIMAL_TOOLTIP);
            },
            SimpleOption.enumValueText(),
            new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(CoordinateOptions.values()), Codec.INT.xmap(CoordinateOptions::get, CoordinateOptions::getId)),
            CoordinateOptions.WHOLE,
            value -> {}
    );


    public CoordinatorOptions(MinecraftClient client, File optionsFile) {
        super(client, optionsFile);
    }

    public SimpleOption<Boolean> getCoordinatorToggle() {
        return coordinatorToggle;
    }

    public SimpleOption<DirectionOptions> getDirection() {
        return direction;
    }

    public SimpleOption<CoordinateOptions> getCoordinate() {
        return coordinate;
    }
}

