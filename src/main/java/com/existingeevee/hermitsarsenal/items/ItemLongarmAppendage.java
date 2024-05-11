package com.existingeevee.hermitsarsenal.items;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.config.generated.IAutoconfig;
import com.existingeevee.hermitsarsenal.misc.IOffhandExtendedReach;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ItemLongarmAppendage extends ItemHA implements IOffhandExtendedReach, IAutoconfig {

	private double reach;

	public ItemLongarmAppendage(String name, double reach) {
		super(name);
		this.setMaxStackSize(1);
		this.reach = reach;
		this.setMaxDamage((int) (reach * 100));
	}

	@Override
	public List<String> getFormatters(ItemStack stack, @Nullable World worldIn) {
		List<String> list = super.getFormatters(stack, worldIn);
		list.add("" + DEC_FORMAT.format(reach));
		return list;
	}
	
	@Override
	public double getReach(EntityPlayer player, ItemStack stack) {
		return reach;
	}

	@Override
	public void onReach(EntityPlayer player, ItemStack stack) {
		stack.damageItem(1, player);
	}

	@Override
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EntityEquipmentSlot.OFFHAND;
	}
	
	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = new LinkedHashSet<>();
		set.add(config.get(getRegistryName().getPath(), "Extra Reach", reach, "How much further can you reach while holding this item.", 0, Byte.MAX_VALUE));
		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		reach = map.get("Extra Reach").getDouble();
	}
}
