package com.existingeevee.hermitsarsenal;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.existingeevee.hermitsarsenal.config.compiled_data.CompiledConfig;
import com.existingeevee.hermitsarsenal.config.generated.AutoconfigHandler;
import com.existingeevee.hermitsarsenal.entity.piss_rupter.PissRupterHandler;
import com.existingeevee.hermitsarsenal.event.GeneralEventHandler;
import com.existingeevee.hermitsarsenal.init.HAEntities;
import com.existingeevee.hermitsarsenal.init.HATools;
import com.existingeevee.hermitsarsenal.items.IMultipartHitItem;
import com.existingeevee.hermitsarsenal.misc.IExtendedReach;
import com.existingeevee.hermitsarsenal.misc.event.LeftClickEvent;
import com.existingeevee.hermitsarsenal.network.NetworkHandler;
import com.existingeevee.hermitsarsenal.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HermitsArsenal.MODID)
public class HermitsArsenal {
	public static final String MODID = "hermitsarsenal";
	
	@SidedProxy(serverSide = "com.existingeevee.hermitsarsenal.proxy.CommonProxy", clientSide = "com.existingeevee.hermitsarsenal.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	@Instance
	public static HermitsArsenal instance = null;

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	
	private File configLocation = null;
	
	public static final CreativeTabs tab = new CreativeTabs(MODID + ".name") {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(HATools.gene_desplicer);
		}
	};

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(GeneralEventHandler.class);
		MinecraftForge.EVENT_BUS.register(PissRupterHandler.class);
		MinecraftForge.EVENT_BUS.register(RegistryHandler.class);
		
		NetworkHandler.init();

		MinecraftForge.EVENT_BUS.register(IExtendedReach.class);
		MinecraftForge.EVENT_BUS.register(IMultipartHitItem.class);
		MinecraftForge.EVENT_BUS.register(LeftClickEvent.class);
		
		if (proxy.isClient())
			HAEntities.preInitClient();
		configLocation = event.getModConfigurationDirectory();
	}
	
	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		HAEntities.init();
	}
	
	@Mod.EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		CompiledConfig.reload();
		AutoconfigHandler.load(new File(configLocation + "/" + MODID));
	}

	//flame for isnis chak
	//weaponized fishing rod of some sort
	//dragon head flamethrower
	//siezer spear
	//reeker charge shield
	//lightning rod to stop things from turning adapted
}
