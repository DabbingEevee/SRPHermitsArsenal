package com.existingeevee.hermitsarsenal.items;

import java.util.List;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.HermitsArsenal;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHA extends Item implements IAutoformat {

	//That is all. i am lazy
	public ItemHA(String name) {
		this.setRegistryName(name);
		this.setTranslationKey(name);
		this.setCreativeTab(HermitsArsenal.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		this.format(stack, worldIn, tooltip, flagIn);
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

}
