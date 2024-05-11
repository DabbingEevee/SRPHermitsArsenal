package com.existingeevee.hermitsarsenal.misc.particle;

import org.apache.commons.lang3.Validate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomParticle extends Particle {
	private final TextureManager textureManager;

	private final ResourceLocation[] resourceLocations;

	private boolean fixedRotation = false, inverted = false;
	private ParticleShape shape = ParticleShape.FLAT;
	private float yaw = 0, pitch = 0;

	public CustomParticle(World worldIn, double posXIn, double posYIn, double posZIn, float angle, float size, int particleMaxAge, ResourceLocation... resourceLocations) {
		super(worldIn, posXIn, posYIn, posZIn);
		this.canCollide = false;
		this.particleAngle = angle;
		this.particleScale = size;
		this.particleMaxAge = particleMaxAge;

		this.textureManager = Minecraft.getMinecraft().getTextureManager();
		this.resourceLocations = resourceLocations;

		Validate.isTrue(this.resourceLocations.length > 0, "ResourceLocations cannot be empty", 0);
	}

	public CustomParticle setAlignment(ParticleShape alignment) {
		this.shape = alignment;
		return this;
	}

	public CustomParticle setPitchYaw(float yaw, float pitch) {
		this.fixedRotation = true;
		this.yaw = yaw;
		this.pitch = pitch;
		return this;
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);
	}

	@Override
	public int getFXLayer() {
		return 3;
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		float f = 1;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightnessForRender(partialTick);
		int j = i & 255;
		int k = i >> 16 & 255;
		j = j + (int) (f * 15.0F * 16.0F);

		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	public static enum ParticleShape {
		FLAT, HORIZONTAL, STAR;
	}

	private static final float PI_OVER_180 = (float) (Math.PI / 180);

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (particleScale <= 0 || this.renderMultishape(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ))
			return;

		float rotYaw;
		float rotPitch;

		if (fixedRotation) {
			rotYaw = yaw * PI_OVER_180;
			rotPitch = pitch * PI_OVER_180;
		} else {
			rotYaw = entityIn.rotationYaw * PI_OVER_180;
			rotPitch = entityIn.rotationPitch * PI_OVER_180;
		}

		float pitchTranslation = (shape == ParticleShape.HORIZONTAL ? (float) Math.PI / 2 : 0);
		//float yawTranslation = (shape == ParticleShape.VERTICAL ? (float) Math.PI / 2 : 0);

		rotationX = MathHelper.cos(rotYaw);
		rotationYZ = MathHelper.sin(rotYaw);

		this.particleAngle = 0;

		rotationXY = -rotationYZ * MathHelper.sin(rotPitch + pitchTranslation);
		rotationXZ = rotationX * MathHelper.sin(rotPitch + pitchTranslation);
		rotationZ = MathHelper.cos(rotPitch + pitchTranslation);

		float f = 0;
		float f1 = 1;
		float f2 = 0;
		float f3 = 1;

		float f4 = 0.1F * this.particleScale;

		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d((-rotationX * f4 - rotationXY * f4), (-rotationZ * f4), (-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((-rotationX * f4 + rotationXY * f4), (rotationZ * f4), (-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((rotationX * f4 + rotationXY * f4), (rotationZ * f4), (rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((rotationX * f4 - rotationXY * f4), (-rotationZ * f4), (rotationYZ * f4 - rotationXZ * f4)) };

		if (this.particleAngle != 0.0F) {
			float f8 = this.particleAngle;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d(f10, f11, f12);

			for (int l = 0; l < 4; ++l) {
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((2.0F * f9)));
			}
		}

		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

		int timePerLoc = this.particleMaxAge / resourceLocations.length;

		int index = (int) Math.ceil(1d * this.particleAge / timePerLoc) - 1;

		try {
			textureManager.bindTexture(resourceLocations[index]);
		} catch (ArrayIndexOutOfBoundsException t) {
			textureManager.bindTexture(resourceLocations[index > resourceLocations.length ? resourceLocations.length - 1 : 0]);
		}
		if (inverted) {
			buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		} else {
			buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();

		}
		Tessellator.getInstance().draw();
	}

	public boolean renderMultishape(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (this.shape == ParticleShape.STAR) {
			this.shape = ParticleShape.HORIZONTAL;
			this.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

			this.shape = ParticleShape.FLAT;
			this.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

			this.shape = ParticleShape.STAR;
			return true;
		}
		return false;
	}

	public CustomParticle setVelocity(Vec3d velocity) {
		this.motionX = velocity.x;
		this.motionY = velocity.y;
		this.motionZ = velocity.z;
		return this;
	}

	public CustomParticle setInverted(boolean inverted) {
		this.inverted = inverted;
		return this;
	}
}
