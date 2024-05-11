package com.existingeevee.hermitsarsenal.entity.piss_rupter;

import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PissRupterHandler {

	@SubscribeEvent
	public static void entityHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityMudo) {
			EntityMudo rupter = (EntityMudo) event.getEntityLiving();
			if (rupter.getSkin() == -99) {
				if (rupter.world.getBlockState(rupter.getPosition()).getMaterial().isReplaceable()) {
					rupter.world.setBlockState(rupter.getPosition(), getPissState(), 3);
				}
				if (rupter.world.getBlockState(rupter.getPosition().up()).getMaterial().isReplaceable()) {
					rupter.world.setBlockState(rupter.getPosition().up(), getPissState(), 3);
				}
			}
		}
	}
	
	private static boolean attemptedPissSearch = false;
	private static IBlockState pissBlock = Blocks.FLOWING_WATER.getStateFromMeta(1);
	
	public static IBlockState getPissState() {
		if (!attemptedPissSearch) {
			attemptedPissSearch = true;
			
			Fluid piss = FluidRegistry.getFluid("piss");
			
			if (piss.canBePlacedInWorld() && piss.getBlock() != null) {
				pissBlock = piss.getBlock().getStateFromMeta(1);
			}
		}
		
		return pissBlock;
	}
}
