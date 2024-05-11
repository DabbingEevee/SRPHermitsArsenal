package com.existingeevee.hermitsarsenal.network.handlers;

import java.lang.reflect.Field;

import com.existingeevee.hermitsarsenal.misc.IExtendedReach;
import com.existingeevee.hermitsarsenal.misc.IOffhandExtendedReach;
import com.existingeevee.hermitsarsenal.network.messages.MessageExtendedReachAttack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ExtendedReachAttackHandler implements IMessageHandler<MessageExtendedReachAttack, IMessage> {

	static final Field ticksSinceLastSwing = ObfuscationReflectionHelper.findField(EntityLivingBase.class, "field_184617_aD");

	@Override
	public IMessage onMessage(MessageExtendedReachAttack message, MessageContext ctx) {
		World world = ctx.getServerHandler().player.world;
		world.getMinecraftServer().addScheduledTask(() -> {
			Entity entity = world.getEntityByID(message.entityID);
			if (!(entity instanceof EntityLivingBase)) {
				return;
			}
			EntityLivingBase entityLiving = (EntityLivingBase) entity;
			EntityPlayer entityPlayer = ctx.getServerHandler().player;
			ItemStack stack = entityPlayer.getHeldItemMainhand();
			
			boolean usingMain = true;
			
			double reach = -1;
			if (stack.getItem() instanceof IExtendedReach) {
				IExtendedReach reachData = (IExtendedReach) stack.getItem();
				reach = reachData.getReach(entityPlayer, stack);
			} 
			if (entityPlayer.getHeldItemOffhand().getItem() instanceof IOffhandExtendedReach) {
				IOffhandExtendedReach offReachData = (IOffhandExtendedReach) entityPlayer.getHeldItemOffhand().getItem();
				double newReach = offReachData.getReach(entityPlayer, entityPlayer.getHeldItemOffhand());
				if (newReach > reach)
					usingMain = false;
				reach = Math.max(newReach, reach);
			}
						
			if (reach + 3 < entityPlayer.getDistance(entityLiving)) {
				return;
			}
			
			if (usingMain) {
				if (stack.getItem() instanceof IExtendedReach) {
					IExtendedReach reachData = (IExtendedReach) stack.getItem();
					reachData.onReach(entityPlayer, stack);
				} 
			} else {
				IOffhandExtendedReach offReachData = (IOffhandExtendedReach) entityPlayer.getHeldItemOffhand().getItem();
				offReachData.onReach(entityPlayer, entityPlayer.getHeldItemOffhand());
			}

			if (message.attCooldown >= 0)
				try {
					ticksSinceLastSwing.set(entityPlayer, message.attCooldown);
				} catch (Exception e) {
					e.printStackTrace();
				}

			entityPlayer.attackTargetEntityWithCurrentItem(entityLiving);
		});
		return null;
	}
}