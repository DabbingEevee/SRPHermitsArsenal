package com.existingeevee.hermitsarsenal.mixin.piss_rupter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.existingeevee.hermitsarsenal.config.GeneralConfig;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;

//summon srparasites:rupter ~ ~ ~ {parasitetype:-99}

@Mixin(EntityMudo.class)
public class MixinAddPissVarient {
	
	@Inject(method = { "onInitialSpawn", "func_180482_a" }, at = @At("RETURN"), remap = false)
	private void hermitsarsenal$HEAD_Inject$onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> ci) {
		EntityMudo $this = (EntityMudo) (Object) this;
		
		if (Math.random() < GeneralConfig.pissRupterSpawnChance && $this.getSkin() == 0) {
			$this.setSkin(-99);
		}
	}
}
