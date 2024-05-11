package com.existingeevee.hermitsarsenal.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageExtendedReachAttack implements IMessage {

	public MessageExtendedReachAttack() {
		this(-1, -1);
	}
	
	public MessageExtendedReachAttack(int id, int attCooldown) {
		this.entityID = id;
		this.attCooldown = attCooldown;
	}
	
	public int entityID = -1;
	public int attCooldown = -1;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.attCooldown = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeFloat(attCooldown);
	}
	
}