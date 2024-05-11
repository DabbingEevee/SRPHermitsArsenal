package com.existingeevee.hermitsarsenal.entity;

import com.dhanantry.scapeandrunparasites.client.model.entity.misc.ModelNULL;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT) 
public class RenderNull extends RenderLiving<EntityLiving> {

	public RenderNull(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelNULL(), 0.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}