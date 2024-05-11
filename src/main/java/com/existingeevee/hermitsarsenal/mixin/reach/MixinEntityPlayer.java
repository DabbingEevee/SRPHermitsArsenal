package com.existingeevee.hermitsarsenal.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.hermitsarsenal.misc.MiscReferences;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

	private MixinEntityPlayer(World worldIn) {
		super(worldIn);
	}

	@Inject(method = "resetCooldown()V", at = @At("HEAD"))
	public void hermitsarsenal$HEAD_Inject$resetCooldown(CallbackInfo ci) {
		MiscReferences.ticksSinceLastSwing.set(ticksSinceLastSwing);
	}

}
