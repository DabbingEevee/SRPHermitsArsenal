package com.existingeevee.hermitsarsenal.mixin.hit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dhanantry.scapeandrunparasites.entity.EntityBody;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.existingeevee.hermitsarsenal.items.IMultipartHitItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;

@Mixin(EntityBody.class) //ObfuscationReflectionHelper
public class MixinEntityBody {

	@Shadow(remap = false)
	EntityParasiteBase parent;

	@Inject(at = @At("RETURN"), method = "attackEntityFrom")
	void hermitsarsenal$RETURN_Inject$attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (source.getImmediateSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) source.getImmediateSource();
			Item itemInHand = player.getHeldItemMainhand().getItem();
			if (itemInHand instanceof IMultipartHitItem) {
				IMultipartHitItem listener = (IMultipartHitItem) itemInHand;
				listener.onHitEntityOrBodyPart(player.getHeldItemMainhand(), parent, player);
			}
		}
	}
}
