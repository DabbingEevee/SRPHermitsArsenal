package com.existingeevee.hermitsarsenal.misc;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.existingeevee.hermitsarsenal.init.HAMaterials;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class HAToolMaterials {
	
	public static final ToolMaterial GENE_DESPLICER = EnumHelper.addToolMaterial("GENE_DESPLICER", 0, 2560, 0, 4, 10).setRepairItem(new ItemStack(HAMaterials.fragmented_genome));
	public static final ToolMaterial OUTBREAK_DAGGER = EnumHelper.addToolMaterial("OUTBREAK_DAGGER", 0, 2560, 0, 3, 10).setRepairItem(new ItemStack(HAMaterials.reprogrammed_gland));	
	public static final ToolMaterial IGNIS_KNIFE = EnumHelper.addToolMaterial("IGNIS_KNIFE", 0, 1280, 0, 1, 10).setRepairItem(new ItemStack(Items.BLAZE_ROD));	
	public static final ToolMaterial IGNIS_CHAKRAM = EnumHelper.addToolMaterial("IGNIS_CHAKRAM", 0, 5120, 0, 2, 10).setRepairItem(new ItemStack(Items.BLAZE_ROD));	
	public static final ToolMaterial CYTOKINE_MUTATOR = EnumHelper.addToolMaterial("CYTOKINE_MUTATOR", 0, 5120, 0, 6, 10).setRepairItem(new ItemStack(HAMaterials.fragmented_genome));
	public static final ToolMaterial SEIZER_SPEAR = EnumHelper.addToolMaterial("SEIZER_SPEAR", 0, 2560, 0, 5, 10).setRepairItem(new ItemStack(SRPItems.dod_drop));
	public static final ToolMaterial SEISMIC_WARHAMMER = EnumHelper.addToolMaterial("SEISMIC_WARHAMMER", 0, 5120, 0, 5, 10).setRepairItem(new ItemStack(SRPItems.ashyco_drop));
	
}
