package com.existingeevee.hermitsarsenal.init;

import com.existingeevee.hermitsarsenal.potion.BasePotion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

public class HAEffects {
	
	public static final Potion outbreak = new BasePotion("outbreak", true, 894257);
	public static final Potion seised = new BasePotion("seised", true, 894257);
	public static final Potion stunned = new BasePotion("stunned", true, 894257);
	
	public static final PotionType outbreak_n_type = new PotionType(new PotionEffect(HAEffects.outbreak, 60 * 20, 0)).setRegistryName("outbreak_n");
	public static final PotionType outbreak_a_type = new PotionType(new PotionEffect(HAEffects.outbreak, 45 * 20, 1)).setRegistryName("outbreak_a");
}
