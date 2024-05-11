package com.existingeevee.hermitsarsenal.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.existingeevee.hermitsarsenal.network.NetworkHandler;
import com.existingeevee.hermitsarsenal.network.messages.MessageExtendedReachAttack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IExtendedReach {

	public double getReach(EntityPlayer player, ItemStack stack);

	default void onReach(EntityPlayer player, ItemStack stack) {

	}

	static final Field ticksSinceLastSwing = ObfuscationReflectionHelper.findField(EntityLivingBase.class, "field_184617_aD");

	@SideOnly(Side.CLIENT)
	static void handleExtendedReach(EntityPlayer player, ItemStack stack) {

		double reach = -1;
		if (stack.getItem() instanceof IExtendedReach) {
			IExtendedReach reachData = (IExtendedReach) stack.getItem();
			reach = reachData.getReach(player, stack);
		}
		if (player.getHeldItemOffhand().getItem() instanceof IOffhandExtendedReach) {
			IOffhandExtendedReach offReachData = (IOffhandExtendedReach) player.getHeldItemOffhand().getItem();
			reach = Math.max(offReachData.getReach(player, player.getHeldItemOffhand()), reach);
		}

		if (reach <= 0)
			return;
		List<Entity> exclude = new ArrayList<>();
		exclude.add(player.getRidingEntity());
		RayTraceResult trace = rayTrace(player, 3.5 + reach, exclude);
		if (trace != null && trace.entityHit != null) {
			try {
				NetworkHandler.HANDLER.sendToServer(new MessageExtendedReachAttack(trace.entityHit.getEntityId(), MiscReferences.ticksSinceLastSwing.get()));
			} catch (Exception e) {
				NetworkHandler.HANDLER.sendToServer(new MessageExtendedReachAttack(trace.entityHit.getEntityId(), -1));
			}
			player.attackTargetEntityWithCurrentItem(trace.entityHit);
		}

	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void interact(PlayerInteractEvent.LeftClickEmpty event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.getItemStack();

		if (player.world.isRemote)
			handleExtendedReach(player, stack);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void interact(PlayerInteractEvent.LeftClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.getItemStack();

		if (player.world.isRemote)
			handleExtendedReach(player, stack);
	}

	@SideOnly(Side.CLIENT)
	static RayTraceResult rayTrace(EntityLivingBase entityLiving, double maxRange, List<Entity> exclude) {
		Vec3d start = entityLiving.getPositionEyes(0.5f);
		Vec3d lookVec = entityLiving.getLook(0.5f);
		Vec3d end = start.add(lookVec.x * maxRange, lookVec.y * maxRange, lookVec.z * maxRange);
		RayTraceResult firstTrace = entityLiving.world.rayTraceBlocks(start, end, false, true, true);
		AxisAlignedBB area = new AxisAlignedBB(start, firstTrace != null ? firstTrace.hitVec : end);
		List<Entity> entities = entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving, area);

		Entity closestValid = null;

		for (Entity e : entities) {
			if (!(e instanceof EntityLivingBase))
				continue;

			if (e.equals(entityLiving))
				continue;

			if (exclude != null && exclude.contains(e))
				continue;

			RayTraceResult intercept = e.getEntityBoundingBox().calculateIntercept(start, end);

			if (intercept != null) {
				if (closestValid == null) {
					closestValid = e;
				} else {
					if (closestValid.getDistanceSq(entityLiving) > e.getDistanceSq(entityLiving)) {
						closestValid = e;
					}
				}
			}
		}

		if (closestValid != null) {
			return new RayTraceResult(closestValid);
		} else {
			return firstTrace;
		}
	}
}
