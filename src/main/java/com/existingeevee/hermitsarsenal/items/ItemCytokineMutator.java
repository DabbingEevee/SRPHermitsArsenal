package com.existingeevee.hermitsarsenal.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.misc.HAToolMaterials;
import com.existingeevee.hermitsarsenal.misc.IExtendedReach;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ItemCytokineMutator extends ItemGeneDesplicer implements IExtendedReach {

	public ItemCytokineMutator() {
		super(HAToolMaterials.CYTOKINE_MUTATOR, "cytokine_mutator");
	}

	@Override
	public void onProc(EntityLivingBase target, EntityLivingBase attacker, int totalPointsRemoved) {
		int hurtResistantTime = target.hurtResistantTime;
		target.hurtResistantTime = 0;
		float damage = totalPointsRemoved * (float) procDamageMultiplier;
		target.attackEntityFrom(DamageSource.OUT_OF_WORLD, damage);
		target.hurtResistantTime = hurtResistantTime;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2, 0));
		}

		return multimap;
	}

	@Override
	public double getReach(EntityPlayer player, ItemStack stack) {
		return extraReach;
	}

	protected double extraReach = 2;
	protected double procDamageMultiplier = 1;

	@Override
	public List<String> getFormatters(ItemStack stack, @Nullable World worldIn) {
		List<String> list = super.getFormatters(stack, worldIn);
		list.add("" + DEC_FORMAT.format(procDamageMultiplier));
		return list;
	}
	
	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = super.getConfigFields(config);
		set.add(config.get(getRegistryName().getPath(), "Extra Damage Multiplier", procDamageMultiplier, "How much damage per point of adaptation removed to deal.", 0, Float.MAX_VALUE / 1000));
		set.add(config.get(getRegistryName().getPath(), "Extra Reach", extraReach, "How much further can you reach while holding this item.", 0, Byte.MAX_VALUE));
		
		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		super.readConfigFields(map);

		procDamageMultiplier = map.get("Extra Damage Multiplier").getDouble(); 
		extraReach = map.get("Extra Reach").getDouble(); 
	}
}
