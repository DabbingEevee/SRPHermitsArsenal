package com.existingeevee.hermitsarsenal.entity.hammer_wave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.dhanantry.scapeandrunparasites.entity.monster.EntityWaveShock;
import com.existingeevee.hermitsarsenal.init.HATools;
import com.existingeevee.hermitsarsenal.items.ItemSeismicHammer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityHammerWave extends EntityCreature {
	private static boolean setup = false;

	private static final ItemSeismicHammer HAMMER = (ItemSeismicHammer) HATools.seismic_warhammer;
	
	private double targetX;
	private double targetY;
	private double targetZ;
	private int duration;
	private boolean canTarget;
	private double bonusDamage = 0;
	EntityLivingBase caster;

	EntityWaveShock mindamageDelagate = new EntityWaveShock(world);

	List<UUID> hit = new ArrayList<>();

	public EntityHammerWave(World worldIn) {
		super(worldIn);
		this.setSize(3.1F, 0.2F);
		this.canTarget = true;
		this.duration = 1;
		if (!setup) {
			setup = true;
			MinecraftForge.EVENT_BUS.register(EntityHammerWave.class);
		}
	}

	private static final ThreadLocal<Boolean> isResetting = ThreadLocal.withInitial(() -> false);

	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
		if (event.getTarget() instanceof EntityHammerWave && !isResetting.get() && event.getEntityLiving() instanceof EntityLiving) {
			isResetting.set(true);
			((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
			isResetting.set(false);
		}
	}

	public EntityHammerWave(World worldIn, EntityLivingBase father) {
		this(worldIn);
		this.caster = father;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
	}

	public void setDuration(int durationF) {
		this.duration = durationF;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isRemote) {
			IBlockState state = this.world.getBlockState(this.getPosition().down());
			if (state.getBlock() != Blocks.AIR) {
				int id = Block.getStateId(state);

				for (int i = 0; i < 35; ++i) {
					this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) this.rand.nextFloat() * (double) this.width * 1.2D * 2.0D - (double) this.width * 1.2D, this.posY, this.posZ + (double) this.rand.nextFloat() * (double) this.width * 1.2D * 2.0D - (double) this.width * 1.2D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() + 140.0D, this.rand.nextGaussian() * 0.02D, id);
				}
				if (this.isBurning()) {
					for (int i = 0; i < 5; ++i) {
						this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX + (double) this.rand.nextFloat() * (double) this.width * 1.2D * 2.0D - (double) this.width * 1.2D, this.posY, this.posZ + (double) this.rand.nextFloat() * (double) this.width * 1.2D * 2.0D - (double) this.width * 1.2D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() + 140.0D, this.rand.nextGaussian() * 0.02D);
					}
				}
			}
		} else {
			if (this.targetX == 0.0D || this.caster == null) {
				this.setDead();
				return;
			}

			if (this.world.getBlockState(this.getPosition()).getBlock() instanceof BlockLiquid) {
				this.setDead();
				return;
			}

			if (this.ticksExisted > 20) {
				if (this.posX == this.prevPosX || this.posZ == this.prevPosZ) {
					this.setDead();
				}

				if (this.ticksExisted > 20 * this.duration) {
					this.setDead();
					return;
				}
			}

			float f = this.width / 2.0F;
			float f1 = this.height;
			AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.posX - (double) f, this.posY, this.posZ - (double) f, this.posX + (double) f, this.posY + (double) f1, this.posZ + (double) f)).grow(1.5D, 0.2D, 1.5D);
			List<EntityLivingBase> moblist = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
			Iterator var5 = moblist.iterator();

			while (var5.hasNext()) {
				EntityLivingBase mob = (EntityLivingBase) var5.next();
				if (mob instanceof EntityHammerWave || mob == caster || hit.contains(mob.getUniqueID()))
					continue;
				hit.add(mob.getUniqueID());
				this.attackEntityAsMob(mob);
			}

			if (this.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 2.0D) {
				this.getMoveHelper().setMoveTo(this.targetX, this.targetY, this.targetZ, 0.6D);
			}

			if (this.posX == this.targetX && this.posZ == this.targetZ) {
				this.setDead();
			}
		}
	}

	@Override
	protected void jump() {
		this.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	public boolean canHit(Entity entityIn) {
		return entityIn.hurtResistantTime <= 0;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		entityIn.motionY += 0.75;

		if (caster != null && entityIn instanceof EntityLiving)
			((EntityLiving) entityIn).setRevengeTarget(caster);
		boolean canHit = canHit(entityIn); //check for iframe presence
		
		if (this.isBurning()) {
			entityIn.setFire(HAMMER.getWaveFlameDuration());
		}

		boolean hit = entityIn.attackEntityFrom(caster != null ? new EntityDamageSourceIndirect("waved", this, caster) : DamageSource.GENERIC, (float) (HAMMER.getWaveDamage() + bonusDamage)); //deal the main damage
		if (entityIn instanceof EntityLivingBase && canHit) {
			mindamageDelagate.setDamages(0, (float) HAMMER.getWavePenDamage(), 0, 0);
			hit = hit || mindamageDelagate.attackEntityAsMobMinimum((EntityLivingBase) entityIn);
		}

		return hit;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
	}

	@Override
	protected void collideWithNearbyEntities() {
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	public void setAttackTarget(Vec3d look) {
		look = look.scale(50);

		if (this.ticksExisted <= 40 && this.caster != null) {
			if (this.canTarget) {
				this.targetX = this.posX + look.x;
				this.targetY = this.caster.posY;
				this.targetZ = this.posZ + look.z;
				this.canTarget = false;
			}
		} else {
			this.setDead();
		}
	}

	public void setBonusDamage(double bonusDamage) {
		this.bonusDamage = bonusDamage;
	}
}
