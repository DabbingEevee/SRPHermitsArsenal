package com.existingeevee.hermitsarsenal.proxy;

public class CommonProxy {

	public void clientRun(Runnable r) {

	}

	public boolean isClient() {
		return false;
	}

	public boolean isClientSneaking() {
		return false;
	}
}
