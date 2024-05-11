package com.existingeevee.hermitsarsenal.mixin.piss_rupter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;

import net.minecraft.util.ResourceLocation;

@Mixin(RenderMudo.class)
public class MixinRenderPissVarient {

	private static final ResourceLocation TEXTURE_PISS = new ResourceLocation("hermitsarsenal:textures/entity/easter_egg/mudo_piss.png");

	@Inject(method = { "getEntityTexture", "func_110775_a" }, at = @At("HEAD"), cancellable = true, remap = false)
	void hermitsarsenal$HEAD_Inject$getEntityTexture(EntityMudo entity, CallbackInfoReturnable<ResourceLocation> ci) {
		if (entity.getSkin() == -99) {
			ci.setReturnValue(TEXTURE_PISS);
		}
	}
}
