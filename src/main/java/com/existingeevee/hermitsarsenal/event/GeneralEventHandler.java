package com.existingeevee.hermitsarsenal.event;

import java.util.Random;
import java.util.function.Predicate;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPFeral;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPure;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.existingeevee.hermitsarsenal.init.HAEffects;
import com.existingeevee.hermitsarsenal.init.HAMaterials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GeneralEventHandler {

	public static final Random RAND = new Random();

	@SubscribeEvent
	public static void onLootCalc(LivingDropsEvent e) {
		if (!e.getEntity().world.getGameRules().getBoolean("doMobLoot"))
			return;
		if (e.getEntity() instanceof EntityParasiteBase) {
			EntityParasiteBase parasite = (EntityParasiteBase) e.getEntity();
			if ((parasite.isBurning() || e.getSource().isFireDamage())) {
				if (RAND.nextInt(25) == 0)
					parasite.dropItem(HAMaterials.incinerated_flesh, 1);
				if (parasite instanceof EntityPAdapted && RAND.nextInt(10) == 0) {
					parasite.dropItem(HAMaterials.fragmented_genome, 1);
				}
			}
			
			if (parasite.getSkin() == 5 && RAND.nextInt(25) == 0) {
				parasite.dropItem(HAMaterials.viral_gland, 1);
			}
			
			if (parasite instanceof EntityPFeral && RAND.nextInt(15) == 0) {
				parasite.dropItem(HAMaterials.unraveled_muscles, 1);
			}
		}
	}

	@SubscribeEvent
	public static void entityHurt(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null && !entity.world.isRemote) {
			if (entity.isPotionActive(HAEffects.outbreak)) {
				float amp = (float) (entity.getActivePotionEffect(HAEffects.outbreak).getAmplifier() + 1);
				float damage = event.getAmount();
				event.setAmount(damage + damage * amp * SRPConfigSystems.viralAmount * 0.75f);
			}
		}
		Entity src = event.getSource().getTrueSource();
		if (src instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) src;
			ItemStack offhand = living.getHeldItemOffhand();
			
			if (offhand.getItem() == SRPItems.livingcore) {
				
			}
		}
	}
	
	@SubscribeEvent
	public static void entityDie(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();

		Entity src = event.getSource().getTrueSource();
		
		if (src == null) {
			src = entity.getAttackingEntity();
		}
		
		if (src instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) src;
			
			if (entity instanceof EntityPAdapted)
				convertItem(attacker, entity, s -> s.getItem() == SRPItems.livingcore, new ItemStack(HAMaterials.vengence_core));
			else if (entity instanceof EntityPPure)
				convertItem(attacker, entity, s -> s.getItem() == SRPItems.livingcore, new ItemStack(HAMaterials.wrathful_core));
		}
	}
	
	private static void convertItem(EntityLivingBase attacker, EntityLivingBase target, Predicate<ItemStack> pred, ItemStack result) {
		ItemStack offhand = attacker.getHeldItemOffhand();
		
		if (pred.test(offhand)) {
			offhand.setCount(offhand.getCount() - 1);
			if (offhand.isEmpty()) {
				attacker.setHeldItem(EnumHand.OFF_HAND, result);
			} else {
				attacker.entityDropItem(result, 0);
				attacker.setHeldItem(EnumHand.OFF_HAND, offhand);
			}
		}
	}
	
	@SubscribeEvent
	public static void onCritCheck(CriticalHitEvent event) {
		if (event.getEntityPlayer().getTags().remove("ForceNextCrit")) {
			event.setResult(Result.ALLOW);
		}
		if (event.getEntityPlayer().getTags().remove("DenyNextCrit")) {
			event.setResult(Result.DENY);
		}
	}
}
