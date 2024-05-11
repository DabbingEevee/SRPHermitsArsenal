package com.existingeevee.hermitsarsenal.misc;

import com.existingeevee.hermitsarsenal.config.compiled_data.ConfigLookupHelper;
import com.existingeevee.hermitsarsenal.init.HAEnchantments;

import net.minecraft.item.ItemStack;

public interface IHasProbabilityProc {

	default boolean shouldProc(ItemStack stack, double prob) {	
		double lvl = ConfigLookupHelper.getEnchantmentAndAlternatives(HAEnchantments.lucky, stack);
		double range = 1 - prob;
		double newProb = range * (-Math.pow(2, -lvl * 0.5d) + 1) + prob;
		
		return Math.random() < newProb;
	}
}
