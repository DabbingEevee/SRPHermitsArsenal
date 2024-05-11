package com.existingeevee.hermitsarsenal.mixin.left_click;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.hermitsarsenal.misc.event.LeftClickEvent;
import com.existingeevee.hermitsarsenal.network.NetworkHandler;
import com.existingeevee.hermitsarsenal.network.messages.MessageNotifyEmptyLeftClick;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Shadow
	int leftClickCounter;

	@Shadow
	RayTraceResult objectMouseOver;

	@Shadow
	EntityPlayerSP player;

	@Inject(method = "clickMouse()V", at = @At("HEAD"))
	private void hermitsarsenal$HEAD_Inject$clickMouse(CallbackInfo ci) {
		if (this.leftClickCounter <= 0) {
			if (this.objectMouseOver != null && !this.player.isRowingBoat()) {
				EntityPlayer player = Minecraft.getMinecraft().player;
				boolean isFullCharge = player.getCooledAttackStrength(Minecraft.getMinecraft().getRenderPartialTicks()) >= 1;

				MinecraftForge.EVENT_BUS.post(new LeftClickEvent(player, isFullCharge));
				NetworkHandler.HANDLER.sendToServer(new MessageNotifyEmptyLeftClick(isFullCharge));
			}
		}
	}
}
