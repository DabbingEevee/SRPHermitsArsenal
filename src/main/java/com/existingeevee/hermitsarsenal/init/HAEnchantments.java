package com.existingeevee.hermitsarsenal.init;

import com.existingeevee.hermitsarsenal.enchantment.DummyEnchantment;
import com.existingeevee.hermitsarsenal.misc.IHasProbabilityProc;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.enchantment.EnumEnchantmentType;

public class HAEnchantments {

	public static final Enchantment lucky = new DummyEnchantment("lucky", Rarity.VERY_RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.values(), t -> t.getItem() instanceof IHasProbabilityProc, 2);
	
}
