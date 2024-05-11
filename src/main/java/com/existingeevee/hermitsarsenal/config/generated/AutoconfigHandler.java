package com.existingeevee.hermitsarsenal.config.generated;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.existingeevee.hermitsarsenal.HermitsArsenal;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AutoconfigHandler {

	public static void load(File configFolder) {
		configFolder.mkdir();

		//We do the items first
		File items = new File(configFolder + "/items");
		execItems(items);
	}

	private static void execItems(File itemsFolder) {
		//First we make the folder
		itemsFolder.mkdir();

		//We loop through EVERY item
		for (Item item : ForgeRegistries.ITEMS) {
			//Not our item, not our problem
			if (!item.getRegistryName().getNamespace().equals(HermitsArsenal.MODID)) {
				continue;
			}

			//If the item is autoconfigable
			if (item instanceof IAutoconfig) {
				IAutoconfig config = (IAutoconfig) item;

				//Make the actual file object
				File configFile = new File(itemsFolder + "/" + item.getRegistryName().getPath() + ".cfg");

				//load it into forge config
				Configuration forgeConfig = new Configuration(configFile, Loader.instance().getIndexedModList().get(HermitsArsenal.MODID).getDisplayVersion());
				forgeConfig.load();

				//Create a map to store any values created
				Map<String, Property> valueMap = new HashMap<>();
				
				//Save the default value or load in any user modifications
				for (Property property : config.getConfigFields(forgeConfig)) {
					property.setRequiresMcRestart(true);
					valueMap.put(property.getName(), property);
				}
				
				//save it to file
				forgeConfig.save();
				
				//read the damn thing
				config.readConfigFields(valueMap);
			}
		}
	}
}
