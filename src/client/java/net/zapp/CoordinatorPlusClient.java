package net.zapp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zapp.options.CoordinatorOptions;
import net.zapp.screens.ConfigurationScreen;
import org.lwjgl.glfw.GLFW;

import java.io.File;

public class CoordinatorPlusClient implements ClientModInitializer {
	public static final String MOD_ID = "coordinator-plus";
    public static CoordinatorOptions options;
    private static final Identifier COORDINATOR_HUD_LAYER = Identifier.of(CoordinatorPlusClient.MOD_ID, "coordinator-hud-layer");
    private static final Identifier COORDINATOR_TITLE = Identifier.of(CoordinatorPlusClient.MOD_ID, "options.title");
    private static final Identifier COORDINATOR_OPEN_KEYBIND = Identifier.of(CoordinatorPlusClient.MOD_ID, "keybind.open_menu");
    private static final Identifier TOGGLE_RENDERING_KEYBIND = Identifier.of(CoordinatorPlusClient.MOD_ID, "keybind.toggle_rendering");
    private static final Identifier COORDINATOR_KEYBIND_CATEGORY = Identifier.of(CoordinatorPlusClient.MOD_ID, "keybinds");
    private static MinecraftClient client;
    private static KeyBinding openCoordinatorKeybind;
    private static KeyBinding toggleRendering;

	@Override
	public void onInitializeClient() {
        client = MinecraftClient.getInstance();

        options = new CoordinatorOptions(client, new File(client.runDirectory,"config/coordinator-plus/options.txt"));

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, COORDINATOR_HUD_LAYER, CoordinatorPlusClient::render);

        openCoordinatorKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                COORDINATOR_OPEN_KEYBIND.toTranslationKey(), // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_B, // The keycode of the key
                COORDINATOR_KEYBIND_CATEGORY.toTranslationKey() // The translation key of the keybinding's category.
        ));

        toggleRendering = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                TOGGLE_RENDERING_KEYBIND.toTranslationKey(), // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_N, // The keycode of the key
                COORDINATOR_KEYBIND_CATEGORY.toTranslationKey() // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openCoordinatorKeybind.wasPressed()) {
                client.setScreen(
                        new ConfigurationScreen(null, options, Text.translatable(COORDINATOR_TITLE.toTranslationKey()))
                );
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleRendering.wasPressed()) {
                options.getCoordinatorToggle().setValue(!options.getCoordinatorToggle().getValue());
            }
        });
	}

	private static void render(DrawContext context, RenderTickCounter tickCounter) {
        CoordinateBarHud barHud = CoordinateBarHud.getInstance(client, context);
        if (options.getCoordinatorToggle().getValue()) {
            barHud.render(context);
        }
	}
}