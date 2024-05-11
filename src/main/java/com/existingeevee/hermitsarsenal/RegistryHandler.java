package com.existingeevee.hermitsarsenal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.existingeevee.hermitsarsenal.init.HAEffects;
import com.existingeevee.hermitsarsenal.init.HAEnchantments;
import com.existingeevee.hermitsarsenal.init.HAMaterials;
import com.existingeevee.hermitsarsenal.init.HARecipes;
import com.existingeevee.hermitsarsenal.init.HATools;
import com.google.common.collect.Lists;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RegistryHandler {

	public static final ArrayList<Class<?>> classes = Lists.newArrayList(HATools.class, HARecipes.class, HAMaterials.class, HAEffects.class, HAEnchantments.class);

	@SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
    	for (Class<?> c : classes) {
        	for (Field f : c.getDeclaredFields()) {
            	try {				
    				if (Modifier.isStatic(f.getModifiers()) && f.get(null) instanceof Enchantment) {
    		        	Enchantment item = (Enchantment) f.get(null);
    					event.getRegistry().register(item);
    				}
    			} catch (IllegalAccessException e1) {
    			}
    		}
    	}
    }
	
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	for (Class<?> c : classes) {
    		for (Field f : c.getDeclaredFields()) {
    			try {				
    				if (Modifier.isStatic(f.getModifiers()) && f.get(null) instanceof Item) {
    					Item item = (Item) f.get(null);
    					event.getRegistry().register(item);
    					RenderHelper.registerItemModel(item);
    				}
    			} catch (IllegalAccessException e1) {
    			}
    		}
    	}
    }
    
    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
    	for (Class<?> c : classes) {
    		for (Field f : c.getDeclaredFields()) {
    			try {				
    				if (Modifier.isStatic(f.getModifiers()) && f.get(null) instanceof Potion) {
    					Potion potion = (Potion) f.get(null);
    					event.getRegistry().register(potion);
    				}
    			} catch (IllegalAccessException e1) {
    			}
    		}
    	}
    }
    
    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
    	for (Class<?> c : classes) {
    		for (Field f : c.getDeclaredFields()) {
    			try {				
    				if (Modifier.isStatic(f.getModifiers()) && f.get(null) instanceof PotionType) {
    					PotionType type = (PotionType) f.get(null);
    					event.getRegistry().register(type);
    				}
    			} catch (IllegalAccessException e1) {
    			}
    		}
    	} 
    }
    
	@SubscribeEvent
	public static void registerIRecipes(RegistryEvent.Register<IRecipe> event) {
		for (Class<?> c : classes) {
			for (Field f : c.getDeclaredFields()) {
				try {
					if (Modifier.isStatic(f.getModifiers()) && f.get(null) instanceof IRecipe) {
						IRecipe rec = (IRecipe) f.get(null);
						event.getRegistry().register(rec);
					}
				} catch (IllegalAccessException e1) {
				}
			}
		}
	}
}
