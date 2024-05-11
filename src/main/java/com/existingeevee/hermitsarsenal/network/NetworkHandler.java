package com.existingeevee.hermitsarsenal.network;

import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.misc.ClientAction.SentClientActionMessage;
import com.existingeevee.hermitsarsenal.network.handlers.ExtendedReachAttackHandler;
import com.existingeevee.hermitsarsenal.network.handlers.NotifyEmptyLeftClickHandler;
import com.existingeevee.hermitsarsenal.network.messages.MessageExtendedReachAttack;
import com.existingeevee.hermitsarsenal.network.messages.MessageNotifyEmptyLeftClick;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

	public static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(HermitsArsenal.MODID);
	private static int i = 0;
	
	public static void init() {
		HANDLER.registerMessage(ExtendedReachAttackHandler.class, MessageExtendedReachAttack.class, i++, Side.SERVER);
		HANDLER.registerMessage(SentClientActionMessage.class, SentClientActionMessage.class, i++, Side.CLIENT);
		HANDLER.registerMessage(NotifyEmptyLeftClickHandler.class, MessageNotifyEmptyLeftClick.class, i++, Side.SERVER);
	}
	
} 
