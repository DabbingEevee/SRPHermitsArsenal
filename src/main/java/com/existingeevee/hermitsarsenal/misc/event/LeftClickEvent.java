package com.existingeevee.hermitsarsenal.misc.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class LeftClickEvent extends PlayerEvent {

	private boolean fullCharge = false;

	public LeftClickEvent(EntityPlayer player, boolean fullCharge) {
		super(player);
		this.fullCharge = fullCharge;
	}

	public boolean isFullCharge() {
		return fullCharge;
	}
}
