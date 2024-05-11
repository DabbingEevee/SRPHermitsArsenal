package com.existingeevee.hermitsarsenal.config;

import com.existingeevee.hermitsarsenal.HermitsArsenal;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = HermitsArsenal.MODID, name = HermitsArsenal.MODID + "/general")
public class GeneralConfig {

	@Name("Do Loot Drops")
	@Comment("Whether or not items from this mod should be added to the lootpool.")
	public static boolean doLootDrops = true;

	@Name("Alternative Enchantments")
	@Comment({
			"Often in a large modpack, there are alternative or upgraded enchantments.",
			"You can specify enchantments that are similar to other enchantments below, along with a multiplier of how much better/worse it is.",
			"This will allow \"advanced\" enchantments to function on items from this mod.",
			"",
			"Format: modid:base_enchantment;modid:alternative_enchantment;multiplier",
			"Example: minecraft:power;cool_mod:mega_power;1.5"
	})
	public static String[] alternativeEnchantments = {};

	@Name("Piss Rupter Easter Egg Chance")
	@Comment("What is the chance for piss rupters to replace rupters with no varients. 0 is never, 1 is always.")
	public static double pissRupterSpawnChance = 0.005;
}
