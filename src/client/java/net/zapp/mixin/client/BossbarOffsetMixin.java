package net.zapp.mixin.client;

import net.minecraft.client.gui.hud.BossBarHud;
import net.zapp.CoordinatorPlusClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BossBarHud.class)
public class BossbarOffsetMixin {
	@ModifyVariable(
			method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;I[Lnet/minecraft/util/Identifier;[Lnet/minecraft/util/Identifier;)V",
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
			at = @At("STORE"),
			name = "o")
	private int modifyOText(int o) {
        if (CoordinatorPlusClient.options.getCoordinatorToggle().getValue()) {
            return o + 28;
        }
		return o;
	}
}