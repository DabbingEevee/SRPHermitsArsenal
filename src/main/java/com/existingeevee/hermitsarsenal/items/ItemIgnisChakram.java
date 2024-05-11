package com.existingeevee.hermitsarsenal.items;

import java.util.Map;
import java.util.Set;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.entity.ignis_chakram.EntityIgnisChakram;
import com.existingeevee.hermitsarsenal.misc.HAToolMaterials;
import com.existingeevee.hermitsarsenal.misc.event.LeftClickEvent;
import com.existingeevee.hermitsarsenal.misc.particle.CustomParticleBuilder;
import com.existingeevee.hermitsarsenal.misc.particle.SpawnCustomParticleAction;
import com.existingeevee.hermitsarsenal.misc.particle.CustomParticle.ParticleShape;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemIgnisChakram extends ItemIgnisKnife {

	public static final CustomParticleBuilder FIRE_SLASH_BUILDER = new CustomParticleBuilder(
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_1.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_2.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_3.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_4.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_5.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_6.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_7.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_slash/fire_slash_8.png")).withMaxAge(8).withShape(ParticleShape.HORIZONTAL).withSize(10);

	public static final CustomParticleBuilder FIRE_BLAST_BUILDER = new CustomParticleBuilder(
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_1.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_2.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_3.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_4.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_5.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_6.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_blast/fire_blast_7.png")).withMaxAge(7).withShape(ParticleShape.FLAT).withSize(10);

	
	public ItemIgnisChakram() {
		super(HAToolMaterials.IGNIS_CHAKRAM);
		this.setRegistryName("ignis_chakram");
		this.setTranslationKey("ignis_chakram");
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer entity, final EnumHand hand) {
		entity.setActiveHand(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, entity.getHeldItem(hand));
	}

	@Override
	public EnumAction getItemUseAction(final ItemStack itemstack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack itemstack) {
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(final ItemStack itemstack, final World world, final EntityLivingBase entityLivingBase, final int timeLeft) {
		int i = this.getMaxItemUseDuration(itemstack) - timeLeft;
		//only happens if you hold down for a quarter second.
		if (i >= 5) {
			//sound
			world.playSound(null, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			int slot = -2;

			//Track stats
			if (entityLivingBase instanceof EntityPlayer) {
				((EntityPlayer) entityLivingBase).addStat(StatList.getObjectUseStats(this));
				boolean offhand = entityLivingBase.getHeldItemOffhand() == itemstack;
				slot = offhand ? -1 : ((EntityPlayer) entityLivingBase).inventory.currentItem;
			}

			if (!world.isRemote) {
				//Fire the "arrow"
				EntityIgnisChakram arrow = new EntityIgnisChakram(world, entityLivingBase);

				arrow.setSlot(slot);

				arrow.setArrowStack(itemstack);
				world.spawnEntity(arrow);
				arrow.shoot(entityLivingBase.getLookVec().x, entityLivingBase.getLookVec().y, entityLivingBase.getLookVec().z, 2.0f, 0.0f);
				arrow.setIsCritical(true);
				
				//Remove a knife from the stack
				itemstack.setCount(itemstack.getCount() - 1);
				
				//Spawn the particle
				CustomParticleBuilder builder = FIRE_BLAST_BUILDER.clone().setPitchYaw(entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
				Vec3d loc = entityLivingBase.getPositionEyes(0.5f).add(entityLivingBase.getLookVec().scale(1));
				SpawnCustomParticleAction.INSTANCE.run(entityLivingBase.getEntityWorld(), loc.x, loc.y, loc.z, builder.toNBT());
				
				entityLivingBase.world.playSound(null, entityLivingBase.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 5, 0f);
			}
		}
	}
	
	@Override
	public void onLeftClick(LeftClickEvent event) {
		EntityPlayer player = event.getEntityPlayer();

		if (!player.world.isRemote && player.getHeldItemMainhand().getItem() == this && this.getClass() == ItemIgnisChakram.class) {
			
			//Spawn the particle
			boolean leftHanded = player.getPrimaryHand() == EnumHandSide.LEFT;
			CustomParticleBuilder builder = FIRE_SLASH_BUILDER.clone().setPitchYaw(player.rotationYaw + (leftHanded ? -22.5f : 22.5f), player.rotationPitch - 11.25f);
			builder.setInverted(leftHanded);
			Vec3d lookVec = player.getLookVec();
			Vec3d motionVec = lookVec.scale(0.1);
			builder.withVelocity(motionVec);
			Vec3d loc = player.getPositionEyes(0.5f).add(lookVec.scale(1));
			SpawnCustomParticleAction.INSTANCE.run(player.getEntityWorld(), loc.x, loc.y - 0.25, loc.z, builder.toNBT());
			
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 5, 2f);
		}
	}
	
	double thrownDamageMultiplier = 2;
	double maxDistanceFromThrower = 40;

	public double getMaxDistanceFromThrower() {
		return maxDistanceFromThrower;
	}

	@SubscribeEvent
	public void onCritCheck(CriticalHitEvent event) {
		if (!event.getEntityLiving().getTags().contains("CurrentlyThrowing"))
			return;
		event.setDamageModifier((float) thrownDamageMultiplier); //2x damage.
	}

	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = super.getConfigFields(config);
		set.add(config.get(getRegistryName().getPath(), "Thrown Damage Multiplier", thrownDamageMultiplier, "How much damage this weapon should deal compared to using it as an melee weapon.", 0, Float.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Max Distance From Thrower", maxDistanceFromThrower, "How far must this weapon be to recalled to the thrower's inventory.", 0, Math.sqrt(Double.MAX_VALUE) - 1));
		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		super.readConfigFields(map);

		thrownDamageMultiplier = map.get("Thrown Damage Multiplier").getDouble();
		maxDistanceFromThrower = map.get("Max Distance From Thrower").getDouble();
	}
}
