package com.existingeevee.hermitsarsenal.items;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.MiscUtils;
import com.existingeevee.hermitsarsenal.config.generated.IAutoconfig;
import com.existingeevee.hermitsarsenal.entity.spear_seiser.EntitySpearNak;
import com.existingeevee.hermitsarsenal.init.HAEffects;
import com.existingeevee.hermitsarsenal.misc.HAToolMaterials;
import com.existingeevee.hermitsarsenal.misc.IExtendedReach;
import com.existingeevee.hermitsarsenal.misc.IHasProbabilityProc;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSeizerSpear extends ItemSword implements IExtendedReach, IAutoformat, IAutoconfig, IMultipartHitItem, IHasProbabilityProc {

	public ItemSeizerSpear() {
		super(HAToolMaterials.SEIZER_SPEAR);
		this.setRegistryName("seizer_spear");
		this.setTranslationKey("seizer_spear");
		this.setCreativeTab(HermitsArsenal.tab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		this.format(stack, worldIn, tooltip, flagIn);
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public List<String> getFormatters(ItemStack stack, @Nullable World worldIn) {
		List<String> list = IAutoformat.super.getFormatters(stack, worldIn);
		list.add(MiscUtils.toRoman(slownessAmplifier));
		list.add(MiscUtils.toRoman(weaknessAmplifier));
		return list;
	}
	
	@SubscribeEvent
	public void onAttackEntityEvent(AttackEntityEvent event) {
		EntityLivingBase attacker = event.getEntityPlayer();

		if (this != attacker.getHeldItemMainhand().getItem()) {
			return;
		}

		if (attacker.onGround) {
			attacker.onGround = false;
			attacker.getTags().add("DenyNextCrit");
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		final World world = entityLiving.world;
		if (!world.isRemote) {
			//Smol power
			final float power = 1f;

			//Spawn and shoot the little shit
			final EntitySpearNak seizer = new EntitySpearNak(world);
			Vec3d eyes = entityLiving.getPositionEyes(0.0f);
			seizer.setPosition(eyes.x, eyes.y, eyes.z);
			shoot(seizer, entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, power, 10);
			world.spawnEntity(seizer);

			//decr one item
			world.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.arrow.shoot")), SoundCategory.NEUTRAL, 1.0f, 1.0f / (itemRand.nextFloat() * 0.5f + 1.0f) + power / 2.0f);

			//cooldown and stop using
			if (entityLiving instanceof EntityPlayer) {
				((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 15 * 20);
			}
			entityLiving.stopActiveHand();
		}

		stack.damageItem(8, entityLiving);

		return stack;
	}

	@Override
	public EnumAction getItemUseAction(final ItemStack itemstack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack itemstack) {
		return 20;
	}

	private static final Random RANDOM = new Random();

	public static void shoot(Entity projectile, Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
		double x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		double y = -MathHelper.sin(pitch * 0.017453292F);
		double z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float f3 = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f3;
		y = y / (double) f3;
		z = z / (double) f3;
		x = x + RANDOM.nextGaussian() * 0.0075D * (double) inaccuracy;
		y = y + RANDOM.nextGaussian() * 0.0075D * (double) inaccuracy;
		z = z + RANDOM.nextGaussian() * 0.0075D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		projectile.motionX = x;
		projectile.motionY = y;
		projectile.motionZ = z;
		float f4 = MathHelper.sqrt(x * x + z * z);
		projectile.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		projectile.rotationPitch = (float) (MathHelper.atan2(y, (double) f4) * (180D / Math.PI));
		projectile.prevRotationYaw = projectile.rotationYaw;
		projectile.prevRotationPitch = projectile.rotationPitch;

		projectile.motionX += shooter.motionX;
		projectile.motionY += shooter.motionY;
		projectile.motionZ += shooter.motionZ;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer entity, final EnumHand hand) {
		entity.setActiveHand(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, entity.getHeldItem(hand));
	}

	@Override
	public double getReach(EntityPlayer player, ItemStack stack) {
		return extraReach;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (attacker instanceof EntityPlayer)
			onHitEntityOrBodyPart(stack, target, (EntityPlayer) attacker);
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public void onHitEntityOrBodyPart(ItemStack stack, EntityLivingBase target, EntityPlayer attacker) {
		if (target instanceof EntityLivingBase) {
			EntityLivingBase livingBase = (EntityLivingBase) target;
			
			if (this.shouldProc(stack, procPercentage)) {
				livingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, slownessAmplifier - 1));
				livingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 30, weaknessAmplifier - 1));
				MiscUtils.playProcEffect(target);
			}
			livingBase.addPotionEffect(new PotionEffect(HAEffects.seised, (int) Math.round(seisedDuration * 20), 0));
		}
	}

	double procPercentage = 0.75;
	double extraReach = 1.75;
	
	int slownessAmplifier = 6;
	int weaknessAmplifier = 2;
	double seisedDuration = 10;

	double miniSeiserHP = 30;
	double miniSeiserDamageCap = 3.75;
	double miniSeiserDuration = 45;

	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = new LinkedHashSet<>();

		set.add(config.get(getRegistryName().getPath(), "Proc Chance", procPercentage, "The probability of the item procing. 0 will never proc, 0.5 will proc 50% of the times, and 1 will always proc.", 0, 1));
		set.add(config.get(getRegistryName().getPath(), "Extra Reach", extraReach, "How much further can you reach while holding this item.", 0, Byte.MAX_VALUE));

		set.add(config.get(getRegistryName().getPath(), "Slowness Amplifier", slownessAmplifier, "What the should amplifier for Slowness be. Setting it to 0 will disable it.", 0, Byte.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Weakness Amplifier", weaknessAmplifier, "What the should amplifier for Weakness be. Setting it to 0 will disable it.", 0, Byte.MAX_VALUE));

		set.add(config.get(getRegistryName().getPath(), "Seised Duration", seisedDuration, "How long (seconds) the attacked entities should be suseptable to be grabbed by the mini-seiser.", 0, Integer.MAX_VALUE));

		set.add(config.get(getRegistryName().getPath(), "Spear Seiser HP", miniSeiserHP, "How much HP should the Spear Seiser have by default.", 0, Double.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Spear Seiser Damage Cap", miniSeiserDamageCap, "What is the maximum amount of HP a Spear Seiser can take in one attack. Setting this to 0 will disable this feature. ", 0, Double.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Spear Seiser Duration", miniSeiserDuration, "What is the maximum amount of time (seconds) a Spear Seiser can exist before despawning. Setting this to a negative number will make them last indefinitely. ", 0, Double.MAX_VALUE));

		
		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		procPercentage = map.get("Proc Chance").getDouble();
		extraReach = map.get("Extra Reach").getDouble();
		
		slownessAmplifier = (int) map.get("Slowness Amplifier").getInt();
		weaknessAmplifier = (int) map.get("Weakness Amplifier").getInt();
		
		seisedDuration = map.get("Seised Duration").getDouble();
		
		miniSeiserHP = map.get("Spear Seiser HP").getDouble();
		miniSeiserDamageCap = map.get("Spear Seiser Damage Cap").getDouble();
		miniSeiserDuration = map.get("Spear Seiser Duration").getDouble();
	}

	public double getMiniSeiserHP() {
		return miniSeiserHP;
	}

	public double getMiniSeiserDamageCap() {
		return miniSeiserDamageCap;
	}

	public double getMiniSeiserDuration() {
		return miniSeiserDuration;
	}
}
