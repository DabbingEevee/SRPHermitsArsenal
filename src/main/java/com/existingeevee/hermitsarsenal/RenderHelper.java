package com.existingeevee.hermitsarsenal;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderHelper {

	public static Item registerItemModel(Item item) {
		if (isClient()) {
			if (item.getHasSubtypes()) {
				if (item instanceof ItemCloth) {
					for (EnumDyeColor dye : EnumDyeColor.values()) {
						ModelLoader.setCustomModelResourceLocation(item, dye.ordinal(), new ModelResourceLocation(
								HermitsArsenal.MODID + ":" + item.getRegistryName().getPath() + "/" + dye.name().toLowerCase(), "inventory"));	
					}
				}
				for (int i = 0; i < item.getMaxDamage(); i++) {
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(
							HermitsArsenal.MODID + ":" + item.getRegistryName().getPath() + "/" + i, "inventory"));
				}
			} else {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
						HermitsArsenal.MODID + ":" + item.getRegistryName().getPath(), "inventory"));
			}
		}
		return item;
	}

	public static void registerBlockModel(Block block) {
		if (isClient()) {
			if (block instanceof BlockFluidClassic) {
				RenderHelper.registerFluidCustomMeshesAndStates(block);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerFluidCustomMeshesAndStates(Block blockIn) {
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(blockIn), new ItemMeshDefinition() {

			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(blockIn.getRegistryName(), "fluid");
			}

		});

		ModelLoader.setCustomStateMapper(blockIn, new StateMapperBase() {

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(blockIn.getRegistryName(), "fluid");
			}

		});
	}

	public static boolean isClient() {
		boolean isclient = true;
		try {
			emptyClientFunc();
		} catch (NoSuchMethodError e) {
			isclient = false;
		}
		return isclient;
	}

	@SideOnly(Side.CLIENT)
	private static void emptyClientFunc() {
	}
}
