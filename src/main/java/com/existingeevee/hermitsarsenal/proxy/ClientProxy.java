package com.existingeevee.hermitsarsenal.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTexturesStitch(TextureStitchEvent.Pre event) {

	}
	
	@Override
	public void clientRun(Runnable r) {
		r.run();
	}
	
	@Override 
	public boolean isClient() {
		return true;
	}
	
	@Override
	public boolean isClientSneaking() {
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		return p == null ? false : p.isSneaking();
	}
}
