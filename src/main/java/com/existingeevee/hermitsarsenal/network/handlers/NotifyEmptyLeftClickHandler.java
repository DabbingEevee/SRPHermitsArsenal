package com.existingeevee.hermitsarsenal.network.handlers;

import com.existingeevee.hermitsarsenal.misc.event.LeftClickEvent;
import com.existingeevee.hermitsarsenal.network.messages.MessageNotifyEmptyLeftClick;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NotifyEmptyLeftClickHandler implements IMessageHandler<MessageNotifyEmptyLeftClick, IMessage> {

	@Override
	public IMessage onMessage(MessageNotifyEmptyLeftClick message, MessageContext ctx) {
		World world = ctx.getServerHandler().player.world;
		world.getMinecraftServer().addScheduledTask(() -> {
			MinecraftForge.EVENT_BUS.post(new LeftClickEvent(ctx.getServerHandler().player, message.full));
		});
		return null;
	}
}