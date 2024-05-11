package com.existingeevee.hermitsarsenal.entity.spear_seiser;

import com.dhanantry.scapeandrunparasites.client.model.entity.deterrent.ModelNak;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelSpearNak extends ModelNak {

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		float f1;
		float f2;
		float f3;
		f1 = MathHelper.cos(ageInTicks * 0.11095986F) * 0.16429871F;
		f2 = -1.0F * MathHelper.cos(ageInTicks * 0.13986F) * 0.17429872F;
		f3 = MathHelper.cos(ageInTicks * 0.0886F) * 0.1472F;
		this.jointLA.rotateAngleX = f1;
		this.jointLA.rotateAngleY = f1 * 0.3F;
		this.jointLA_1.rotateAngleZ = f1;
		this.jointLA_2.rotateAngleZ = 0.0F;
		this.jointLA_3.rotateAngleZ = f1;
		this.jointLA_5.rotateAngleZ = f1;
		this.jointLA_7.rotateAngleZ = f1;
		this.jointA.rotateAngleX = f2;
		this.jointA.rotateAngleY = f2 * -0.2333F;
		this.jointA_1.rotateAngleZ = f2;
		this.jointA_2.rotateAngleZ = 0.0F;
		this.jointA_3.rotateAngleZ = f2;
		this.jointA_5.rotateAngleZ = f2;
		this.jointA_7.rotateAngleZ = f2;
		this.jointL.rotateAngleX = f3;
		this.jointL.rotateAngleY = f3 * 0.5F;
		this.jointL_1.rotateAngleZ = f3;
		this.jointL_2.rotateAngleZ = 0.0F;
		this.jointL_3.rotateAngleZ = f3;
		this.jointL_5.rotateAngleZ = f3;
		this.jointL_7.rotateAngleZ = f3;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		this.mainbody.offsetY = 0.0F;
		this.mainbody.offsetX = 0.0F;
		this.mainbody.offsetZ = 0.0F;
	}
}
