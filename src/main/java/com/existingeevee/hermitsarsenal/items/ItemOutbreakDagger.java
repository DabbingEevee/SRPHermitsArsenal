package com.existingeevee.hermitsarsenal.items;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.MiscUtils;
import com.existingeevee.hermitsarsenal.config.generated.IAutoconfig;
import com.existingeevee.hermitsarsenal.init.HAEffects;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHandSide;
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

public class ItemOutbreakDagger extends ItemSword implements IHasProbabilityProc, IAutoformat, IAutoconfig, IMultipartHitItem, ISweepParticleItem {

	public static final CustomParticleBuilder FIRE_PIERCE_BUILDER = new CustomParticleBuilder(
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/viral_slash/viral_slash_1.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/viral_slash/viral_slash_2.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/viral_slash/viral_slash_3.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/viral_slash/viral_slash_4.png"),
			new ResourceLocation(HermitsArsenal.MODID, "textures/particle/viral_slash/viral_slash_5.png")).withMaxAge(5).withShape(ParticleShape.HORIZONTAL).withSize(10);
	
	public ItemOutbreakDagger() {
		super(HAToolMaterials.OUTBREAK_DAGGER);
		this.setRegistryName("outbreak_dagger");
		this.setTranslationKey("outbreak_dagger");
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
	public boolean shouldSweep(EntityLivingBase wielder, ItemStack stack) {
		return false;
	}
	
	@SubscribeEvent
	public void onLeftClick(LeftClickEvent event) {
		EntityPlayer player = event.getEntityPlayer();

		if (!player.world.isRemote && player.getHeldItemMainhand().getItem() == this && this.getClass() == ItemOutbreakDagger.class) {
			
			//Spawn the particle
			boolean leftHanded = player.getPrimaryHand() == EnumHandSide.LEFT;
			CustomParticleBuilder builder = FIRE_PIERCE_BUILDER.clone().setPitchYaw(player.rotationYaw, player.rotationPitch - 11.25f);
			builder.setInverted(leftHanded);
			Vec3d lookVec = player.getLookVec();
			Vec3d motionVec = lookVec.scale(0.025);
			builder.withVelocity(motionVec);

			Vec3d loc = player.getPositionEyes(0.5f).add(lookVec.scale(1.125));
			SpawnCustomParticleAction.INSTANCE.run(player.getEntityWorld(), loc.x, loc.y - 0.25, loc.z, builder.toNBT());
			
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_POP, SoundCategory.PLAYERS, 5f, 0f);
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_BOBBER_SPLASH, SoundCategory.PLAYERS, 0.5f, 2f);
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
		if (target instanceof EntityLivingBase) {

			EntityLivingBase living = (EntityLivingBase) target;

			PotionEffect eff = null;

			int amplifier = -1;

			if (living.isPotionActive(HAEffects.outbreak)) {
				amplifier = living.getActivePotionEffect(HAEffects.outbreak).getAmplifier();
			}

			if (this.shouldProc(stack, procPercentage) && amplifier < maxAmplifier - 1) { //3 is the ingame amplifier. 
				eff = new PotionEffect(HAEffects.outbreak, 10 * 20, amplifier + 1);

				MiscUtils.playProcEffect(target);

			} else if (amplifier >= 0 && living.isPotionActive(HAEffects.outbreak)) {
				eff = new PotionEffect(HAEffects.outbreak, 10 * 20, amplifier);
			}

			if (eff != null)
				living.addPotionEffect(eff);
		}
	}

	double procPercentage = 0.25;
	int maxAmplifier = 3;

	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = new LinkedHashSet<>();

		set.add(config.get(getRegistryName().getPath(), "Proc Chance", procPercentage, "The probability of the item procing. 0 will never proc, 0.5 will proc 50% of the times, and 1 will always proc.", 0, 1));
		set.add(config.get(getRegistryName().getPath(), "Max Amplifier", maxAmplifier, "The maximum level of outbreak that can be applied", 0, Byte.MAX_VALUE));

		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		procPercentage = map.get("Proc Chance").getDouble();
		maxAmplifier = map.get("Max Amplifier").getInt();
	}
}
