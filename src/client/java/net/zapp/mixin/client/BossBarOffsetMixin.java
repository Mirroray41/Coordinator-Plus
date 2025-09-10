package net.zapp.mixin.client;

import net.zapp.CoordinatorPlusClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(net.minecraft.client.gui.hud.BossBarHud.class)
public class BossBarOffsetMixin {
    @ModifyVariable(
            method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true)
	private int modifyYBar(int y) {
        if (CoordinatorPlusClient.options.getCoordinatorToggle().getValue()) {
            return y + 28;
        }
        return y;
	}

    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At(value = "STORE"),
            ordinal = 0)
    private int modifyOText(int o) {
        if (CoordinatorPlusClient.options.getCoordinatorToggle().getValue()) {
            return o + 28;
        }
        return o;
    }
}