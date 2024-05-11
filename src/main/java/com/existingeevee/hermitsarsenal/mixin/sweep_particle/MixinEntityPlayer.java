package com.existingeevee.hermitsarsenal.mixin.sweep_particle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.hermitsarsenal.misc.ISweepParticleItem;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

	private MixinEntityPlayer(World worldIn) {
		super(worldIn);
	}

	@Inject(method = "spawnSweepParticles()V", at = @At("HEAD"), cancellable = true)
	public void hermitsarsenal$HEAD_Inject$spawnSweepParticles(CallbackInfo ci) {
		if (this.getHeldItemMainhand().getItem() instanceof ISweepParticleItem) {
			ISweepParticleItem sweepItem = (ISweepParticleItem) this.getHeldItemMainhand().getItem();
			if (!sweepItem.shouldSweep(this, this.getHeldItemMainhand())) {
				ci.cancel();
			}
		}
	}

}
