package com.existingeevee.hermitsarsenal.misc.particle;

import org.apache.commons.lang3.Validate;

import com.existingeevee.hermitsarsenal.misc.particle.CustomParticle.ParticleShape;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CustomParticleBuilder implements Cloneable {

	final ResourceLocation[] resourceLocations;

	float angle = 0;
	float size = 1;
	int maxAge = 10;

	private boolean inverted = false;
	private boolean fixedRotation = false;
	private ParticleShape shape = ParticleShape.FLAT;
	private float yaw = 0, pitch = 0;

	private Vec3d velocity = Vec3d.ZERO;

	public CustomParticleBuilder(ResourceLocation... resourceLocations) {
		this.resourceLocations = resourceLocations;
		Validate.isTrue(this.resourceLocations.length > 0, "ResourceLocations cannot be empty", 0);
	}

	public CustomParticleBuilder(NBTBase base) {
		if (base instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) base;

			angle = tag.getFloat("Angle");
			size = tag.getFloat("Size");
			maxAge = tag.getInteger("MaxAge");

			fixedRotation = tag.getBoolean("FixedRotation");
			shape = ParticleShape.valueOf(tag.getString("HorizontallyAligned"));
			if (shape == null) {
				shape = ParticleShape.FLAT;
			}

			if (fixedRotation) {
				yaw = tag.getFloat("RotYaw");
				pitch = tag.getFloat("RotPitch");
			}
			
			double motX = tag.getDouble("VelX");
			double motY = tag.getDouble("VelY");
			double motZ = tag.getDouble("VelZ");
			
			inverted = tag.getBoolean("Inverted");
			
			velocity = new Vec3d(motX, motY, motZ);
			
			NBTTagList list = tag.getTagList("ResourceLocations", NBT.TAG_STRING);

			ResourceLocation[] locations = new ResourceLocation[list.tagCount()];

			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagString rl = (NBTTagString) list.get(i);
				locations[i] = new ResourceLocation(rl.getString());
			}

			this.resourceLocations = locations;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public CustomParticleBuilder withAngle(float angle) {
		this.angle = angle;
		return this;
	}

	public CustomParticleBuilder withSize(float size) {
		this.size = size;
		return this;
	}

	public CustomParticleBuilder withMaxAge(int maxAge) {
		this.maxAge = maxAge;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public CustomParticle build(World world, double x, double y, double z) {
		CustomParticle particle = new CustomParticle(world, x, y, z, angle, size, maxAge, resourceLocations).setAlignment(shape);
		
		particle.setInverted(inverted);
		
		particle.setVelocity(velocity);
		
		if (fixedRotation)
			particle.setPitchYaw(yaw, pitch);

		return particle;
	}

	public CustomParticleBuilder withShape(ParticleShape shape) {
		this.shape = shape;
		return this;
	}

	public CustomParticleBuilder setPitchYaw(float yaw, float pitch) {
		this.fixedRotation = true;
		this.yaw = yaw;
		this.pitch = pitch;
		return this;
	}

	public CustomParticleBuilder withVelocity(Vec3d velocity) {
		this.velocity = velocity;
		return this;
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();

		tag.setFloat("Angle", angle);
		tag.setFloat("Size", size);
		tag.setInteger("MaxAge", maxAge);

		tag.setBoolean("FixedRotation", fixedRotation);
		tag.setString("HorizontallyAligned", shape.name());

		if (fixedRotation) {
			tag.setFloat("RotYaw", yaw);
			tag.setFloat("RotPitch", pitch);
		}

		tag.setDouble("VelX", velocity.x);
		tag.setDouble("VelY", velocity.y);
		tag.setDouble("VelZ", velocity.z);
		
		tag.setBoolean("Inverted", inverted);
		
		NBTTagList list = new NBTTagList();
		for (ResourceLocation rl : resourceLocations) {
			list.appendTag(new NBTTagString(rl.toString()));
		}

		tag.setTag("ResourceLocations", list);

		return tag;
	}

	@Override
	public CustomParticleBuilder clone() {
		return new CustomParticleBuilder(this.toNBT());
	}

	public CustomParticleBuilder setInverted(boolean inverted) {
		this.inverted = inverted;
		return this;
	}
}
