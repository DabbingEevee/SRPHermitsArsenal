package com.existingeevee.hermitsarsenal.config.compiled_data;

import java.util.HashMap;
import java.util.Map;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.config.GeneralConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompiledConfig {

	public static Map<ResourceLocation, Map<ResourceLocation, Double>> enchantmentAlternatives = new HashMap<>();

	public static void reload() {
		//Parse alt enchantments
		for (String str : GeneralConfig.alternativeEnchantments) {
			try {
				String[] split = str.split(";");
				ResourceLocation base = new ResourceLocation(split[0]);
				ResourceLocation alt = new ResourceLocation(split[1]);
				double multiplier = Double.parseDouble(split[2]);
				
				Map<ResourceLocation, Double> map = enchantmentAlternatives.computeIfAbsent(base, e -> new HashMap<>());
				map.put(alt, multiplier);
			} catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}
	}

	@SubscribeEvent
	public static void onOnConfigChangedEvent(OnConfigChangedEvent event) {
		if (event.getModID().equals(HermitsArsenal.MODID)) {
			reload();
		}
	}
}
