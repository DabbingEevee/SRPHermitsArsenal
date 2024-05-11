package com.existingeevee.hermitsarsenal.misc;

@Deprecated //Please dont use this class lmfao
public class MiscReferences {

	public static final ThreadLocal<Integer> ticksSinceLastSwing = ThreadLocal.withInitial(() -> -1);
	public static final ThreadLocal<Boolean> isAttacking = ThreadLocal.withInitial(() -> false);

}
