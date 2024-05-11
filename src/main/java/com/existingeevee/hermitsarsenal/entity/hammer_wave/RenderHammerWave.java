package com.existingeevee.hermitsarsenal.entity.hammer_wave;

import com.dhanantry.scapeandrunparasites.client.model.entity.misc.ModelNULL;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHammerWave extends RenderLiving<EntityLiving> {

	public RenderHammerWave(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelNULL(), 0.0F);
	}

	@Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		//trolled
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}