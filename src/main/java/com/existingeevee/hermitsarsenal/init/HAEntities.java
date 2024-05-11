package com.existingeevee.hermitsarsenal.init;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.config.IDConfig;
import com.existingeevee.hermitsarsenal.entity.hammer_wave.EntityHammerWave;
import com.existingeevee.hermitsarsenal.entity.hammer_wave.RenderHammerWave;
import com.existingeevee.hermitsarsenal.entity.ignis_chakram.EntityIgnisChakram;
import com.existingeevee.hermitsarsenal.entity.ignis_chakram.RenderIgnisChakram;
import com.existingeevee.hermitsarsenal.entity.spear_seiser.EntitySpearNak;
import com.existingeevee.hermitsarsenal.entity.spear_seiser.RenderSpearNak;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HAEntities {

	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
		EntityRegistry.registerModEntity(new ResourceLocation(HermitsArsenal.MODID + ":" + name), entity, name + "_" + HermitsArsenal.MODID, id, HermitsArsenal.instance, range, 1, true);
	}

	private static <T extends Entity> void registerRenderer(Class<T> entity, IRenderFactory<T> renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entity, renderFactory);
	}
	
	public static void init() {
		registerEntity("ignis_chakram", EntityIgnisChakram.class, IDConfig.ignisChakram, 50);
		registerEntity("spear_seizer", EntitySpearNak.class, IDConfig.spearSeizer, 50);
		registerEntity("hammer_wave", EntityHammerWave.class, IDConfig.hammerWave, 50);
	}

	@SideOnly(Side.CLIENT)
	public static void preInitClient() {
		registerRenderer(EntityIgnisChakram.class, RenderIgnisChakram::new);
		registerRenderer(EntitySpearNak.class, RenderSpearNak::new);
		registerRenderer(EntityHammerWave.class, RenderHammerWave::new);
	}
}
