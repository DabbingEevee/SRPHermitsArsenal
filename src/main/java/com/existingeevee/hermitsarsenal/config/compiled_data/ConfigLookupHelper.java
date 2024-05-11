package com.existingeevee.hermitsarsenal.config.compiled_data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ConfigLookupHelper {

	public static double getEnchantmentAndAlternatives(Enchantment ench, ItemStack stack) {
		double total = 0;
		
		for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
			double multiplier = 0;
			
			if (entry.getKey() == ench) {
				multiplier = 1;
			} else {
				Map<ResourceLocation, Double> map = CompiledConfig.enchantmentAlternatives.getOrDefault(ench.getRegistryName(), new HashMap<>());
				multiplier = map.getOrDefault(entry.getKey().getRegistryName(), 0d);
			}
			total += multiplier * entry.getValue();
		}
		
		return total;
	}
	
	public static boolean isEnchantmentSimilar(Enchantment base, Enchantment alt) {
		if (base == alt)
			return true;
		Map<ResourceLocation, Double> map = CompiledConfig.enchantmentAlternatives.getOrDefault(base.getRegistryName(), new HashMap<>());
		return map.containsKey(alt.getRegistryName());
	}	
}
