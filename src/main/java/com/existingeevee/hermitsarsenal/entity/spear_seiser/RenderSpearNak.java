package com.existingeevee.hermitsarsenal.entity.spear_seiser;

import com.dhanantry.scapeandrunparasites.client.renderer.entity.deterrent.RenderNak;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSpearNak extends RenderLiving<EntitySpearNak> {

	public RenderSpearNak(RenderManager manager) {
		super(manager, new ModelSpearNak(), 0.4F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpearNak entity) {
		return RenderNak.TEXTURES;
	}

	@Override
	protected void renderModel(EntitySpearNak entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		GlStateManager.pushMatrix();
        GlStateManager.scale(0.35F, 0.35F, 0.35F);
        GlStateManager.translate(0, 2.6 , 0);
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		GlStateManager.popMatrix();
	}
}
