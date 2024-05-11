package com.existingeevee.hermitsarsenal.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMultipartHitItem {

	void onHitEntityOrBodyPart(ItemStack stack, EntityLivingBase target, EntityPlayer attacker);
	
}
