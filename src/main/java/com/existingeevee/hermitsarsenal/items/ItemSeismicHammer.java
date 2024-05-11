package com.existingeevee.hermitsarsenal.items;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.MiscUtils;
import com.existingeevee.hermitsarsenal.config.compiled_data.ConfigLookupHelper;
import com.existingeevee.hermitsarsenal.config.generated.IAutoconfig;
import com.existingeevee.hermitsarsenal.entity.hammer_wave.EntityHammerWave;
import com.existingeevee.hermitsarsenal.init.HAEffects;
import com.existingeevee.hermitsarsenal.misc.HAToolMaterials;
import com.existingeevee.hermitsarsenal.misc.IExtendedReach;
import com.existingeevee.hermitsarsenal.misc.IHasProbabilityProc;
import com.google.common.collect.Multimap;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSeismicHammer extends ItemSword implements IExtendedReach, IAutoformat, IAutoconfig, IMultipartHitItem, IHasProbabilityProc {

	public ItemSeismicHammer() {
		super(HAToolMaterials.SEISMIC_WARHAMMER);
		this.setRegistryName("seismic_warhammer");
		this.setTranslationKey("seismic_warhammer");
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
	
	
	@Override
	public boolean shouldProcEasterEgg(ItemStack stack, @Nullable World worldIn) {
		return worldIn != null && worldIn.getPlayerEntityByName("Evan_Wingerter") != null;
	}

	@SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		CooldownTracker tracker = event.getEntityPlayer().getCooldownTracker();
		if (event.getWorld().isRemote || !event.getEntityPlayer().isSneaking() || event.getItemStack().getItem() != this || tracker.hasCooldown(this))
			return;
		if (this.spawnShock(event.getWorld(), event.getEntityLiving(), event.getHitVec(), event.getItemStack())) {
			tracker.setCooldown(this, 60);
			event.getItemStack().damageItem(3, event.getEntityLiving());
			event.getWorld().playSound(null, event.getPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1, 0);
		}
	}

	private boolean spawnShock(World world, EntityLivingBase entity, Vec3d hitVec, ItemStack stack) {
		EntityHammerWave wa = new EntityHammerWave(world, entity);
		if (ConfigLookupHelper.getEnchantmentAndAlternatives(Enchantments.FLAME, stack) > 0) {
			wa.setFire(999);
		}
		wa.setBonusDamage(ConfigLookupHelper.getEnchantmentAndAlternatives(Enchantments.POWER, stack) * wavePowerDamage);
		wa.setPositionAndUpdate(hitVec.x, hitVec.y, hitVec.z);
		if (!wa.world.getCollisionBoxes(wa, wa.getEntityBoundingBox()).isEmpty()) {
			wa.setDead();
		} else {
			wa.setDuration(waveDuration);
			world.spawnEntity(wa);
			double rotRad = -entity.rotationYaw * Math.PI / 180d;
			wa.setAttackTarget(new Vec3d(Math.sin(rotRad), 0, Math.cos(rotRad)));
			return true;
		}
		return false;
	}

	private static final ThreadLocal<Boolean> isResetting = ThreadLocal.withInitial(() -> false);

	@SubscribeEvent
	public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
		if (event.getEntityLiving().isPotionActive(HAEffects.stunned) && !isResetting.get() && event.getEntityLiving() instanceof EntityLiving) {
			isResetting.set(true);
			((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
			isResetting.set(false);
		}
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
	public double getReach(EntityPlayer player, ItemStack stack) {
		return extraReach;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2, 0));
		}

		return multimap;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (ConfigLookupHelper.isEnchantmentSimilar(Enchantments.FLAME, enchantment) || ConfigLookupHelper.isEnchantmentSimilar(Enchantments.POWER, enchantment))
			return true;
		return enchantment.type.canEnchantItem(stack.getItem());
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

				livingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, (int) Math.round(stunTime * 20), slownessAmplifier - 1));
				livingBase.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, (int) Math.round(stunTime * 20)));
				livingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, (int) Math.round(stunTime * 20), weaknessAmplifier - 1));

				if (target instanceof EntityLiving) {
					EntityLiving living = (EntityLiving) target;

					living.addPotionEffect(new PotionEffect(HAEffects.stunned, (int) Math.round(stunTime * 20)));
					living.setAttackTarget(null);
					MiscUtils.playProcEffect(target);
				}
			}
		}
	}
	
	double procPercentage = 0.125;
	double extraReach = 2;
	double stunTime = 2;
	
	int slownessAmplifier = 3;
	int weaknessAmplifier = 2;
	
	double waveDamage = 7.5;
	double wavePenDamage = 1.5;
	double wavePowerDamage = 1;
	int waveFlameDuration = 10;
	int waveDuration = 30;
	
	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = new LinkedHashSet<>();

		set.add(config.get(getRegistryName().getPath(), "Proc Chance", procPercentage, "The probability of the item procing. 0 will never proc, 0.5 will proc 50% of the times, and 1 will always proc.", 0, 1));
		set.add(config.get(getRegistryName().getPath(), "Extra Reach", extraReach, "How much further can you reach while holding this item.", 0, Byte.MAX_VALUE));

		set.add(config.get(getRegistryName().getPath(), "Stun Duration", stunTime, "How long (seconds) the attacked entities should be stunned.", 0, Integer.MAX_VALUE));

		set.add(config.get(getRegistryName().getPath(), "Slowness Amplifier", slownessAmplifier, "What the should amplifier for Slowness be. Setting it to 0 will disable it.", 0, Byte.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Weakness Amplifier", weaknessAmplifier, "What the should amplifier for Weakness be. Setting it to 0 will disable it.", 0, Byte.MAX_VALUE));

		set.add(config.get(getRegistryName().getPath(), "Wave Duration", waveDuration, "How what is the max time (seconds) that a wave can exist for", 0, Integer.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Base Wave Damage", waveDamage, "How much damage should the waves created deal.", 0, Float.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Penetrating Wave Damage", wavePenDamage, "How much unblockable damage should the waves created deal.", 0, Float.MAX_VALUE));
		set.add(config.get(getRegistryName().getPath(), "Wave Power Bonus", wavePowerDamage, "How much extra damage should be dealt per level of Power of equivalent.", 0, Double.MAX_VALUE / 100));
		set.add(config.get(getRegistryName().getPath(), "Wave Flame Time", waveFlameDuration, "How long (seconds) should the waves summoned set targets ablaze when enchanted with Flame or an equivalent.", 0, Integer.MAX_VALUE));
		
		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		procPercentage = map.get("Proc Chance").getDouble();
		extraReach = map.get("Extra Reach").getDouble();
		
		stunTime = map.get("Stun Duration").getDouble();
		
		slownessAmplifier = map.get("Slowness Amplifier").getInt();
		weaknessAmplifier = map.get("Weakness Amplifier").getInt();
		
		waveDuration = map.get("Wave Duration").getInt();
		waveDamage = map.get("Base Wave Damage").getDouble();
		wavePenDamage = map.get("Penetrating Wave Damage").getDouble();
		wavePowerDamage = map.get("Wave Power Bonus").getDouble();
		waveFlameDuration = map.get("Wave Flame Time").getInt();
	}

	public double getWaveDamage() {
		return waveDamage;
	}

	public double getWavePenDamage() {
		return wavePenDamage;
	}

	public int getWaveFlameDuration() {
		return waveFlameDuration;
	}
}
