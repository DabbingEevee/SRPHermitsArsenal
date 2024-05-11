package com.existingeevee.hermitsarsenal.misc.particle;

import com.existingeevee.hermitsarsenal.misc.ClientAction;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpawnCustomParticleAction extends ClientAction {

	public static final SpawnCustomParticleAction INSTANCE = new SpawnCustomParticleAction();

	private SpawnCustomParticleAction() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void runAsClient(World world, double x, double y, double z, NBTBase data) {
		try {
			CustomParticleBuilder builder = new CustomParticleBuilder(data);
			CustomParticle particle = builder.build(world, x, y, z);
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		} catch (Exception e) {

		}
	}
}
