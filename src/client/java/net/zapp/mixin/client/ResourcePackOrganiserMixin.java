package net.zapp.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.zapp.BarPartRenderer.applyResourcePackTintTemplate;

@Mixin(net.minecraft.client.gui.screen.pack.ResourcePackOrganizer.class)
public class ResourcePackOrganiserMixin {
    @Inject(method = "apply", at = @At("TAIL"))
    private void apply(CallbackInfo ci) {
        System.out.println("REFRESH");
        applyResourcePackTintTemplate(MinecraftClient.getInstance());
    }
}
