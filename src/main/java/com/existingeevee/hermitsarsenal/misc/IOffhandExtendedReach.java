package com.existingeevee.hermitsarsenal.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;	

public interface IOffhandExtendedReach {

	double getReach(EntityPlayer player, ItemStack stack);
	
	default void onReach(EntityPlayer player, ItemStack stack) {
		
	}
}
