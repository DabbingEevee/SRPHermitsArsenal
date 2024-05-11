package com.existingeevee.hermitsarsenal.enchantment;

import java.util.function.Function;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class DummyEnchantment extends Enchantment {

	private final Function<ItemStack, Boolean> canApply;

	private final int maxLevel;

	public DummyEnchantment(String name, Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots, Function<ItemStack, Boolean> canApply, int maxLvl) {
		super(rarityIn, typeIn, slots);
		this.canApply = canApply;
		this.maxLevel = maxLvl;
		this.setName(name);
		this.setRegistryName(name);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return super.canApplyAtEnchantingTable(stack) && canApply.apply(stack);
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 0;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return Integer.MAX_VALUE;
	}

}
