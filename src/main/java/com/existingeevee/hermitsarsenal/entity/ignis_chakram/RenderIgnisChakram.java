package com.existingeevee.hermitsarsenal.entity.ignis_chakram;

import com.existingeevee.hermitsarsenal.init.HATools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT) //adapted from spartan weaponry
public class RenderIgnisChakram extends Render<EntityIgnisChakram> {
	private final RenderItem itemRenderer;

	public RenderIgnisChakram(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void doRender(EntityIgnisChakram entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();

		float interpX = (float) (x + (entity.isAirBorne ? entity.motionX * partialTicks : 0));
		float interpY = (float) (y + (entity.isAirBorne ? entity.motionY * partialTicks : 0));
		float interpZ = (float) (z + (entity.isAirBorne ? entity.motionZ * partialTicks : 0));

		GlStateManager.translate(interpX, interpY, interpZ);
		GlStateManager.scale(2.0d, 2.0d, 2.0d);
		GlStateManager.enableRescaleNormal();

		this.transform(entity, partialTicks); 

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		ItemStack weapon = entity.getArrowStack();
		if (!weapon.isEmpty()) {
			this.itemRenderer.renderItem(weapon, ItemCameraTransforms.TransformType.FIXED);
		} else {
			this.itemRenderer.renderItem(new ItemStack(HATools.ignis_chakram), ItemCameraTransforms.TransformType.GUI);
		}
		
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected void transform(EntityIgnisChakram entity, float partialTicks) {
		float rotationInAir = entity.isInGround() ? 0.0f : (entity.getTicksInAir() + partialTicks)  * 40.0f % 360.0f;
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F,  0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 45.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(180.0f, 1.0f, 1.0f, 0.0f);
        GlStateManager.rotate(90.f, 1.0f, -1.0f, 0.0f);
        GlStateManager.rotate(rotationInAir, 0.0f, 0.0f, 1.0f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIgnisChakram entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}