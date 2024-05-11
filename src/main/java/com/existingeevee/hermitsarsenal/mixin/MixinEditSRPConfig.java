package com.existingeevee.hermitsarsenal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;

@Mixin(SRPConfig.class)
public class MixinEditSRPConfig {

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void hermitsarsenal$HEAD_Inject$Inject$__clinit__(CallbackInfo ci) {

	}
}
