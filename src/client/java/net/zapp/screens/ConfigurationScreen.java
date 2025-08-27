package net.zapp.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.zapp.options.CoordinatorOptions;
import org.jetbrains.annotations.Nullable;

public class ConfigurationScreen extends GameOptionsScreen {
    protected final Screen parent;
    protected final CoordinatorOptions coordinatorOptions;
    @Nullable
    protected OptionListWidget body;
    public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    public ConfigurationScreen(Screen parent, CoordinatorOptions coordinatorOptions, Text title) {
        super(parent, (GameOptions) coordinatorOptions, title);
        this.parent = parent;
        this.coordinatorOptions = coordinatorOptions;
    }

    private static SimpleOption<?>[] getOptions(CoordinatorOptions coordinatorOptions) {
        return new SimpleOption[]{
                coordinatorOptions.getCoordinatorToggle(),
                coordinatorOptions.getDirection(),
                coordinatorOptions.getCoordinate()
        };
    }

    @Override
    protected void init() {
        this.initHeader();
        this.initBody();
        this.initFooter();
        this.layout.forEachChild(child -> {
            ClickableWidget widget = this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    protected void initHeader() {
        this.layout.addHeader(this.title, this.textRenderer);
    }

    protected void initBody() {
        this.body = this.layout.addBody(new OptionListWidget(this.client, this.width, (GameOptionsScreen) this));
        this.addOptions();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    protected void addOptions() {
        this.body.addAll(getOptions(this.coordinatorOptions));
    }

    protected void initFooter() {
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        if (this.body != null) {
            this.body.position(this.width, this.layout);
        }
    }

    @Override
    public void close() {
        if (this.body != null) {
            this.body.applyAllPendingValues();
        }

        this.client.setScreen(this.parent);
    }
}
