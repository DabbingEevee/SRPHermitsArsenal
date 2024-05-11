package com.existingeevee.hermitsarsenal.entity.spear_seiser;

import java.util.List;
import java.util.stream.Collectors;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.init.HAEffects;
import com.existingeevee.hermitsarsenal.init.HATools;
import com.existingeevee.hermitsarsenal.items.ItemSeizerSpear;
import com.existingeevee.hermitsarsenal.misc.particle.CustomParticleBuilder;
import com.existingeevee.hermitsarsenal.misc.particle.SpawnCustomParticleAction;
import com.existingeevee.hermitsarsenal.misc.particle.CustomParticle.ParticleShape;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySpearNak extends EntityLiving {

	private int id = -1;

	private static final ItemSeizerSpear SPEAR = (ItemSeizerSpear) HATools.seizer_spear;
	
	public static final CustomParticleBuilder NERD_VIRUS_BUILDER = new CustomParticleBuilder(new ResourceLocation(HermitsArsenal.MODID, "textures/particle/nerdvirus.png")).withMaxAge(10).withShape(ParticleShape.FLAT).withSize(2);
	
	public EntitySpearNak(World worldIn) {
		super(worldIn);
		this.setSize(0.5f, 1f);
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(SPEAR.getMiniSeiserHP());
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(6.0D);
	}

	@Override
	public void onLivingUpdate() {
		if (onGround) {
			this.motionX = 0;
			this.motionZ = 0;
		}
		super.onLivingUpdate();
		if (!world.isRemote && this.getTargetedEntity() != null) {
			double dis = this.getDistanceSq(this.getTargetedEntity());
			if (dis > 1.0D) {
				EntityLivingBase target = this.getTargetedEntity();
				target.dismountRidingEntity();
				double str = 0.5D;
				target.motionX += (Math.signum(this.posX - target.posX) * str - target.motionX) * str;
				target.motionZ += (Math.signum(this.posZ - target.posZ) * str - target.motionZ) * str;
				target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 2, false, false));
				target.velocityChanged = true;
				target.setRevengeTarget(this);
				if (target instanceof EntityLiving) {
					((EntityLiving) target).setAttackTarget(this);
				}
			}
		}

		if (!world.isRemote && this.rand.nextInt(10) == 0 && this.getCustomNameTag().equalsIgnoreCase("nerd")) {
			CustomParticleBuilder builder = NERD_VIRUS_BUILDER.clone();
			builder.withVelocity(new Vec3d(Math.random() * 0.2 - 0.1, Math.random() * 0.1, Math.random() * 0.2 - 0.1));
			SpawnCustomParticleAction.INSTANCE.run(world, posX, this.getEntityBoundingBox().maxY, posZ, builder.toNBT());
		}
		
		if (this.canDespawn() && !this.hasCustomName() && this.ticksExisted > SPEAR.getMiniSeiserDuration() * 20) {
			this.setDead();
		}

		this.clearActivePotions(); //nuh uh
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		return source == DamageSource.FALL || super.isEntityInvulnerable(source);
	}

	@Override
	public void damageEntity(DamageSource source, float amount) {
		super.damageEntity(source, SPEAR.getMiniSeiserDamageCap() <= 0 ? amount : Math.min(amount, (float) SPEAR.getMiniSeiserDamageCap()));
	}

	@Override
	public void setHealth(float newHealth) {
		try {
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			if (trace[2].getMethodName().equals("attackEntityAsMobMinimum")) {
				return; //no you dont get to use minimum damage. dingus
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		super.setHealth(newHealth);
	}

	public EntityLivingBase getTargetedEntity() {
		Entity ent = world.getEntityByID(id);
		if (validate(ent, true)) {
			return (EntityLivingBase) ent;
		}

		if (id < 0) {
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(10))
					.stream().filter(e -> validate(e, false)).collect(Collectors.toList());

			if (!list.isEmpty()) {
				Entity closest = list.remove(0);

				for (Entity e : list) {
					if (e.getDistanceSq(this) < closest.getDistanceSq(this))
						closest = e;
				}

				id = closest.getEntityId();
				return (EntityLivingBase) closest;
			}
		}

		return null;
	}

	private boolean validate(Entity ent, boolean resetID) {
		if (ent instanceof EntityLivingBase && ent.getDistanceSq(this) < 10 * 10 && ent.isEntityAlive() && ((EntityLivingBase) ent).isPotionActive(HAEffects.seised))
			return true;
		if (resetID)
			id = -1;
		return false;
	}

	public int getParasiteIDRegister() {
		return -1;
	}
}
