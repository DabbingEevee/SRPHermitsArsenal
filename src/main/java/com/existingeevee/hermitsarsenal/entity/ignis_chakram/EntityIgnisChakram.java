package com.existingeevee.hermitsarsenal.entity.ignis_chakram;

import java.lang.reflect.Field;
import java.util.UUID;

import com.existingeevee.hermitsarsenal.init.HATools;
import com.existingeevee.hermitsarsenal.items.ItemIgnisChakram;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EntityIgnisChakram extends EntityArrow {

	public EntityIgnisChakram(final World a) {
		super(a);
		this.setNoGravity(true);
	}

	public EntityIgnisChakram(final World worldIn, final double x, final double y, final double z) {
		super(worldIn, x, y, z);
		this.setNoGravity(true);
	}

	public EntityIgnisChakram(final World worldIn, final EntityLivingBase shooter) {
		super(worldIn, shooter);
		this.setNoGravity(true);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	private static final UUID FAKE_PLAYER_UUID = UUID.nameUUIDFromBytes("ignis chakram fake player".getBytes());
	private static final Field ticksSinceLastAtt = ObfuscationReflectionHelper.findField(EntityLivingBase.class, "field_184617_aD");
	private static final Field handInventory = ObfuscationReflectionHelper.findField(EntityLivingBase.class, "field_184630_bs");
	private static final Field armorArray = ObfuscationReflectionHelper.findField(EntityLivingBase.class, "field_184631_bt");
	private static final Field ticksInGround = ObfuscationReflectionHelper.findField(EntityArrow.class, "field_70252_j");
	private static final Field ticksInAir = ObfuscationReflectionHelper.findField(EntityArrow.class, "field_70257_an");

	@Override
	public void onUpdate() {
		this.setIsCritical(false); //never crit so never particles. thats it.

		double maxRange = ((ItemIgnisChakram) HATools.ignis_chakram).getMaxDistanceFromThrower();
		boolean inRange = shootingEntity == null || shootingEntity != null && this.getDistanceSq(shootingEntity) < maxRange * maxRange;

		double motionSq = motionX * motionX + motionY * motionY + motionZ * motionZ;
		if (!inRange) {
			this.motionX *= 0.5;
			this.motionY *= 0.5;
			this.motionZ *= 0.5;
		}

		if (this.pickupStatus == PickupStatus.ALLOWED) {
			try {
				ticksInGround.set(this, 69);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			if ((this.inGround || !inRange || motionSq < 0.2) && this.shootingEntity instanceof EntityPlayer) {
				EntityPlayer player = ((EntityPlayer) this.shootingEntity);

				boolean given = false;

				int slot = this.getSlot();

				if (slot == -1) {
					if (player.getHeldItemOffhand().isEmpty()) {
						player.setHeldItem(EnumHand.OFF_HAND, getArrowStack());
						given = true;
					}
				} else if (slot >= 0 && slot < 9) {
					if (player.inventory.getStackInSlot(slot).isEmpty()) {
						given = player.inventory.add(slot, this.getArrowStack());
					}
				}

				if (!given && player.inventory.addItemStackToInventory(this.getArrowStack())) {
					given = true;
				}

				if (given) {
					player.onItemPickup(this, 1);
					this.setDead();
					return;
				}
			}
		}

		for (int k = 0; k < 4; ++k) {
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1);
			this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
		}

		super.onUpdate();
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;
		boolean hasHit = this.getEntityData().getBoolean("HasHit");

		if (entity != null) {

			ItemStack stack = this.getArrowStack();
			if (!world.isRemote) {
				if (!stack.isEmpty()) {
					EntityPlayer tempHolder;

					String name = "Unknown";
					if (this.shootingEntity != null) {
						name = this.shootingEntity.getName();
					}

					if (!(this.shootingEntity instanceof EntityPlayer)) {
						tempHolder = new FakePlayer((WorldServer) world, new GameProfile(FAKE_PLAYER_UUID, name));
					} else {
						tempHolder = (EntityPlayer) shootingEntity;
					}

					ItemStack currentHandSlot = tempHolder.getHeldItemMainhand();

					try {
						//Attack the entity at full swing. \o/
						//Bit janky lmao	
						int orig = ticksSinceLastAtt.getInt(tempHolder);
						boolean onGround = tempHolder.onGround;

						tempHolder.onGround = false;
						tempHolder.setHeldItem(EnumHand.MAIN_HAND, stack);
						ticksSinceLastAtt.set(tempHolder, Integer.MAX_VALUE);

						refreshAttributes(tempHolder);

						boolean sprinting = tempHolder.isSprinting();

						tempHolder.getTags().add("CurrentlyThrowing");
						tempHolder.getTags().add("ForceNextCrit");
						tempHolder.attackTargetEntityWithCurrentItem(entity);
						tempHolder.getTags().remove("CurrentlyThrowing");

						tempHolder.setSprinting(sprinting);

						refreshAttributes(tempHolder);

						stack = tempHolder.getHeldItemMainhand(); //maybe something changed (ie durability)
						tempHolder.setHeldItem(EnumHand.MAIN_HAND, currentHandSlot);
						ticksSinceLastAtt.set(tempHolder, orig);
						tempHolder.onGround = onGround;

					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

					//prevent this from repeatily proc'ing since this is a full attack each time.
					this.getEntityData().setBoolean("HasHit", true);
				}

				if (stack.isEmpty()) {
					this.setDead();
				} else {
					this.setArrowStack(stack);
				}
			}
		} else {
			super.onHit(raytraceResultIn); //should be fine. were only after the actual hit entity bit.			
			//Damage the item
			boolean shouldDamage = true;

			if (this.shootingEntity instanceof EntityPlayer) {
				shouldDamage = !((EntityPlayer) this.shootingEntity).capabilities.isCreativeMode;
			}

			if (!world.isRemote && !hasHit) {
				ItemStack stack = this.getArrowStack();
				if (shouldDamage) {

					stack.attemptDamageItem(1, rand, null);
					if (stack.isEmpty()) {
						this.setDead();
					} else {
						this.setArrowStack(stack);
					}
				}
				this.getEntityData().setBoolean("HasHit", true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void refreshAttributes(EntityLivingBase entity) {
		if (entity.world.isRemote)
			return;
		try {
			for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
				ItemStack itemstack;

				switch (entityequipmentslot.getSlotType()) {
				case HAND:
					itemstack = ((NonNullList<ItemStack>) handInventory.get(entity)).get(entityequipmentslot.getIndex());
					break;
				case ARMOR:
					itemstack = ((NonNullList<ItemStack>) armorArray.get(entity)).get(entityequipmentslot.getIndex());
					break;
				default:
					continue;
				}

				ItemStack itemstack1 = entity.getItemStackFromSlot(entityequipmentslot);

				if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
					if (!ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack))
						((WorldServer) entity.world).getEntityTracker().sendToTracking(entity, new SPacketEntityEquipment(entity.getEntityId(), entityequipmentslot, itemstack1));
					MinecraftForge.EVENT_BUS.post(new LivingEquipmentChangeEvent(entity, entityequipmentslot, itemstack, itemstack1));

					if (!itemstack.isEmpty()) {
						entity.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
					}

					if (!itemstack1.isEmpty()) {
						entity.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot));
					}

					switch (entityequipmentslot.getSlotType()) {
					case HAND:
						((NonNullList<ItemStack>) handInventory.get(entity)).set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
						break;
					case ARMOR:
						((NonNullList<ItemStack>) armorArray.get(entity)).set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("Item", this.getArrowStack().serializeNBT());

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setArrowStack(new ItemStack(compound.getCompoundTag("Item")));
	}

	@Override
	public ItemStack getArrowStack() {
		ItemStack stack = new ItemStack(this.getEntityData().getCompoundTag("Item"));
		if (this.getEntityData().hasKey("Item") && (stack.getItem() == HATools.ignis_chakram || stack.isEmpty())) {
			return stack;
		}
		return new ItemStack(HATools.ignis_chakram);
	}

	public EntityIgnisChakram setArrowStack(ItemStack stack) {
		stack = stack.copy();
		stack.setCount(1);
		this.getEntityData().setTag("Item", stack.serializeNBT());
		this.setCustomNameTag(stack.getDisplayName());
		return this;
	}

	public void setSlot(int slot) {
		getEntityData().setInteger("ThrownSlot", slot);
	}

	public int getSlot() {
		return getEntityData().getInteger("ThrownSlot");
	}

	public boolean isInGround() {
		return this.inGround;
	}

	public int getTicksInAir() {
		try {
			return ticksInAir.getInt(this);
		} catch (IllegalAccessException e) {
		}
		return 0;
	}
}
