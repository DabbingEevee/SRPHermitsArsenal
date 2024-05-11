package com.existingeevee.hermitsarsenal.items;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.init.HAMaterials;
import com.existingeevee.hermitsarsenal.misc.TextHelper;
import com.google.common.collect.Lists;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAutoformat {

	public static final DecimalFormat DEC_FORMAT = new DecimalFormat("0.##");

	@SideOnly(Side.CLIENT)
	default void format(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String key = stack.getItem().getTranslationKey() + (shouldProcEasterEgg(stack, worldIn) ? ".egg_desc" : ".desc");
		
		if (I18n.canTranslate(key)) {
			String translation = I18n.translateToLocal(key);
			List<String> formatters = getFormatters(stack, worldIn);
			
			for (int i = 1; i <= formatters.size(); i++) {
				translation = translation.replaceAll("\\%" + i + "\\$", formatters.get(i - 1));
			}
			 
			if (!translation.contains("--null")) {
				TextHelper.smartSplitString(translation, 35).forEach(tooltip::add);
				if (stack.isItemEnchanted()) {
					tooltip.add("");
				}
			}
		}
	}
	
	default List<String> getFormatters(ItemStack stack, @Nullable World worldIn) {
		return Lists.newArrayList();
	}
	
	default boolean shouldProcEasterEgg(ItemStack stack, @Nullable World worldIn) {
		if (this == HAMaterials.unraveled_muscles) {
			return worldIn != null && worldIn.getPlayerEntityByName("UwU_Peanut") != null;
		}
		
		return false;
	}
	
}
