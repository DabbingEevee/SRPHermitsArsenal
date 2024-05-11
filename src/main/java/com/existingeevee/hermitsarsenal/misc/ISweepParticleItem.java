package com.existingeevee.hermitsarsenal.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ISweepParticleItem {

	default boolean shouldSweep(EntityLivingBase wielder, ItemStack stack) {
		return true;
	}
	
}
