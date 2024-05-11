package com.existingeevee.hermitsarsenal.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageNotifyEmptyLeftClick implements IMessage {

	public MessageNotifyEmptyLeftClick() {
		this(true);
	}
	
	public MessageNotifyEmptyLeftClick(boolean full) {
		this.full = full;
	}
	
	public boolean full;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.full = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(full);
	}
	
}