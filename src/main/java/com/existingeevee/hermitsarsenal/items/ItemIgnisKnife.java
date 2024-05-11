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
import com.existingeevee.hermitsarsenal.misc.HAToolMaterials;
import com.existingeevee.hermitsarsenal.misc.IHasProbabilityProc;
import com.existingeevee.hermitsarsenal.misc.ISweepParticleItem;
import com.existingeevee.hermitsarsenal.misc.event.LeftClickEvent;
import com.existingeevee.hermitsarsenal.misc.particle.CustomParticle.ParticleShape;
import com.existingeevee.hermitsarsenal.misc.particle.CustomParticleBuilder;
import com.existingeevee.hermitsarsenal.misc.particle.SpawnCustomParticleAction;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIgnisKnife extends ItemSword implements IHasProbabilityProc, IMultipartHitItem, IAutoconfig, IAutoformat, ISweepParticleItem {

	public static final CustomParticleBuilder FIRE_PIERCE_BUILDER = new CustomParticleBuilder(
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_1.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_2.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_3.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_4.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_5.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_6.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/fire_pierce/fire_pierce_7.png")).withMaxAge(7).withShape(ParticleShape.HORIZONTAL).withSize(6);

	public ItemIgnisKnife() {
		super(HAToolMaterials.IGNIS_KNIFE);
		this.setRegistryName("ignis_knife");
		this.setTranslationKey("ignis_knife");
		this.setCreativeTab(HermitsArsenal.tab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	protected ItemIgnisKnife(ToolMaterial mat) {
		super(mat);
		this.setCreativeTab(HermitsArsenal.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		this.format(stack, worldIn, tooltip, flagIn);
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public boolean shouldProcEasterEgg(ItemStack stack, @Nullable World worldIn) {
		return worldIn != null && worldIn.getPlayerEntityByName("TreeVirus") != null;
	}

	@Override
	public boolean shouldSweep(EntityLivingBase wielder, ItemStack stack) {
		return false;
	}
	
	@Override
	public List<String> getFormatters(ItemStack stack, @Nullable World worldIn) {
		List<String> list = IAutoformat.super.getFormatters(stack, worldIn);
		list.add("" + DEC_FORMAT.format(initialDamageBonus));
		list.add("" + DEC_FORMAT.format(fireAspectDamageBonus));
		return list;
	}

	double procPercentage = 0.25;
	double initialDamageBonus = 2;
	double fireAspectDamageBonus = 1;
	int fireTimeSeconds = 5;

	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = new LinkedHashSet<>();

		set.add(config.get(getRegistryName().getPath(), "Proc Chance", procPercentage, "The probability of the item procing. 0 will never proc, 0.5 will proc 50% of the times, and 1 will always proc.", 0, 1));
		set.add(config.get(getRegistryName().getPath(), "Damage Bonus", initialDamageBonus, "Extra fire damage that will be dealt when it procs.", 0, Float.MAX_VALUE / 1000));
		set.add(config.get(getRegistryName().getPath(), "Fire Aspect Damage Bonus", fireAspectDamageBonus, "Extra proc damage per level of fire aspect or equivalent.", 0, Float.MAX_VALUE / 20));
		set.add(config.get(getRegistryName().getPath(), "Fire Time Seconds", fireTimeSeconds, "Time (seconds) that the hit entity will be set ablaze for.", 0, Integer.MAX_VALUE));

		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		procPercentage = map.get("Proc Chance").getDouble();
		initialDamageBonus = map.get("Damage Bonus").getDouble();
		fireAspectDamageBonus = map.get("Fire Aspect Damage Bonus").getDouble();
		fireTimeSeconds = map.get("Fire Time Seconds").getInt();
	}

	@SubscribeEvent
	public void onLeftClick(LeftClickEvent event) {
		EntityPlayer player = event.getEntityPlayer();

		if (!player.world.isRemote && player.getHeldItemMainhand().getItem() == this && this.getClass() == ItemIgnisKnife.class) {
			CustomParticleBuilder builder = FIRE_PIERCE_BUILDER.clone().setPitchYaw(player.rotationYaw, player.rotationPitch - 11.25f);

			Vec3d lookVec = player.getLookVec();
			
			Vec3d motionVec = lookVec.scale(0.1);
			builder.withVelocity(motionVec);

			Vec3d loc = player.getPositionEyes(0.5f).add(lookVec.scale(1.5));
			SpawnCustomParticleAction.INSTANCE.run(player.getEntityWorld(), loc.x, loc.y - 0.25, loc.z, builder.toNBT());
			
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 5, 2f);
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (attacker instanceof EntityPlayer)
			onHitEntityOrBodyPart(stack, target, (EntityPlayer) attacker);

		return super.hitEntity(stack, target, attacker);
	}

	@Override
	public void onHitEntityOrBodyPart(ItemStack stack, EntityLivingBase target, EntityPlayer attacker) {
		if (!target.world.isRemote) {
			if (this.shouldProc(stack, procPercentage)) {
				int hurtResistantTime = target.hurtResistantTime;
				target.hurtResistantTime = 0;

				double damage = initialDamageBonus + ConfigLookupHelper.getEnchantmentAndAlternatives(Enchantments.FIRE_ASPECT, stack) * fireAspectDamageBonus;

				target.attackEntityFrom(DamageSource.ON_FIRE, (float) damage);
				target.hurtResistantTime = hurtResistantTime;

				MiscUtils.playProcEffect(target);
			}

			target.setFire(fireTimeSeconds);
		}
	}
}
