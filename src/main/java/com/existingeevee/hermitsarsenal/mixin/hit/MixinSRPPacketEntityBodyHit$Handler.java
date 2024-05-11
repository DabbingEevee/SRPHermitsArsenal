package com.existingeevee.hermitsarsenal.mixin.hit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.network.SRPPacketEntityBodyHit;
import com.existingeevee.hermitsarsenal.items.IMultipartHitItem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Mixin(SRPPacketEntityBodyHit.Handler.class)
public class MixinSRPPacketEntityBodyHit$Handler {

	@Inject(at = @At("RETURN"), method = "onMessage", remap = false)
	void hermitsarsenal$RETURN_Inject$onMessage(SRPPacketEntityBodyHit message, MessageContext ctx, CallbackInfoReturnable<IMessage> ci) {
		EntityPlayerMP thePlayer = SRPMain.proxy.getPlayerEntityFromContext(ctx);
		Entity target = thePlayer.world.getEntityByID(ObfuscationReflectionHelper.getPrivateValue(SRPPacketEntityBodyHit.class, message, "targetId"));
		if (target instanceof EntityLivingBase && target.isEntityAlive()) {
			thePlayer.getServer().addScheduledTask(() -> {
				Item itemInHand = thePlayer.getHeldItemMainhand().getItem();
				if (itemInHand instanceof IMultipartHitItem) {
					IMultipartHitItem listener = (IMultipartHitItem) itemInHand;
					listener.onHitEntityOrBodyPart(thePlayer.getHeldItemMainhand(), (EntityLivingBase) target, thePlayer);
				}
			});
		}
	}
}
