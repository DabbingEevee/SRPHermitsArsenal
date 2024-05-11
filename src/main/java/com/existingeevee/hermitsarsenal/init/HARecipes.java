package com.existingeevee.hermitsarsenal.init;

import org.apache.commons.lang3.tuple.Pair;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.existingeevee.hermitsarsenal.misc.RecipeHelper;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

//I HATE JSON!!!!!!!!!

public class HARecipes {

	
	public static final IRecipe reprogrammed_gland = RecipeHelper.createRecipe("reprogrammed_gland", new ItemStack(HAMaterials.reprogrammed_gland),
			new String[] { " E ", "GGG", " E " },
			Pair.of('G', Ingredient.fromItem(HAMaterials.viral_gland)),
			Pair.of('E', Ingredient.fromItem(Items.ENDER_EYE))
			);
	
	public static final IRecipe outbreak_dagger = RecipeHelper.createRecipe("outbreak_dagger", new ItemStack(HATools.outbreak_dagger),
			new String[] { " RP", "PCR", "BP " },
			Pair.of('P', Ingredient.fromItem(HAMaterials.reprogrammed_gland)),
			Pair.of('C', Ingredient.fromItem(HAMaterials.vengence_core)),
			Pair.of('R', Ingredient.fromItem(SRPItems.anogla_drop)),
			Pair.of('B', Ingredient.fromItem(SRPItems.hardbone))
			);
	
	public static final IRecipe gene_splicer = RecipeHelper.createRecipe("gene_splicer", new ItemStack(HATools.gene_desplicer),
			new String[] { " FF", "LCF", "HL " },
			Pair.of('F', Ingredient.fromItem(HAMaterials.fragmented_genome)),
			Pair.of('L', Ingredient.fromItem(HAMaterials.incinerated_flesh)),
			Pair.of('C', Ingredient.fromItem(HAMaterials.vengence_core)),
			Pair.of('H', Ingredient.fromItem(SRPItems.hardbone))
			);
	
	public static final IRecipe ignis_knife = RecipeHelper.createRecipe("ignis_knife", new ItemStack(HATools.ignis_knife),
			new String[] { "  R", " BI", "SP " },
			Pair.of('R', Ingredient.fromItem(Items.BLAZE_ROD)),
			Pair.of('P', Ingredient.fromItem(Items.BLAZE_POWDER)),
			Pair.of('B', Ingredient.fromItem(HAMaterials.incinerated_flesh)),
			Pair.of('I', Ingredient.fromItem(SRPItems.infblade)),
			Pair.of('S', Ingredient.fromItem(Items.STICK))
			);
	
	public static final IRecipe ignis_chakram1 = RecipeHelper.createRecipe("ignis_chakram1", new ItemStack(HATools.ignis_chakram),
			new String[] { "VKI", "KBK", "IKV" },
			Pair.of('V', Ingredient.fromItem(HAMaterials.vengence_core)),
			Pair.of('K', Ingredient.fromItem(HATools.ignis_knife)),
			Pair.of('B', Ingredient.fromItem(SRPItems.hardbone)),
			Pair.of('I', Ingredient.fromItem(HAMaterials.incinerated_flesh))
			);
	
	public static final IRecipe ignis_chakram2 = RecipeHelper.createRecipe("ignis_chakram2", new ItemStack(HATools.ignis_chakram),
			new String[] { "IKV", "KBK", "VKI" },
			Pair.of('V', Ingredient.fromItem(HAMaterials.vengence_core)),
			Pair.of('K', Ingredient.fromItem(HATools.ignis_knife)),
			Pair.of('B', Ingredient.fromItem(SRPItems.hardbone)),
			Pair.of('I', Ingredient.fromItem(HAMaterials.incinerated_flesh))
			);
	
	public static final IRecipe longarm_appendage = RecipeHelper.createRecipe("longarm_appendage", new ItemStack(HATools.longarm_appendage),
			new String[] { "TTA", " FA", "  A" },
			Pair.of('T', Ingredient.fromItem(SRPItems.tendrons)),
			Pair.of('A', Ingredient.fromItem(SRPItems.ashyco_drop)),
			Pair.of('F', Ingredient.fromItem(HAMaterials.fragmented_genome))
			);
	
	public static final IRecipe extended_appendage = RecipeHelper.createRecipe("extended_appendage", new ItemStack(HATools.extended_appendage),
			new String[] { "DBD", "WLW", "DBD" },
			Pair.of('D', Ingredient.fromItem(SRPItems.dod_drop)),
			Pair.of('B', Ingredient.fromItem(SRPItems.venkrol_drop)),
			Pair.of('W', Ingredient.fromItem(HAMaterials.wrathful_core)),
			Pair.of('L', Ingredient.fromItem(HATools.longarm_appendage))
			);
	
	public static final IRecipe cytokine_mutator = RecipeHelper.createRecipe("cytokine_mutator", new ItemStack(HATools.cytokine_mutator),
			new String[] { "BGG", "WSG", "HWB" },
			Pair.of('H', Ingredient.fromItem(SRPItems.hardbone)),
			Pair.of('B', Ingredient.fromItem(SRPItems.infblade)),
			Pair.of('W', Ingredient.fromItem(HAMaterials.wrathful_core)),
			Pair.of('S', Ingredient.fromItem(HATools.gene_desplicer)),
			Pair.of('G', Ingredient.fromItem(HAMaterials.fragmented_genome))
			);
	
	public static final IRecipe seizer_spear = RecipeHelper.createRecipe("seizer_spear", new ItemStack(HATools.seizer_spear),
			new String[] { "MVB", "MHR", "HMM" },
			Pair.of('M', Ingredient.fromItem(SRPItems.dod_drop)),
			Pair.of('V', Ingredient.fromItem(HAMaterials.vengence_core)),
			Pair.of('B', Ingredient.fromItem(SRPItems.infblade)),
			Pair.of('H', Ingredient.fromItem(SRPItems.hardbone)),
			Pair.of('R', Ingredient.fromItem(HAMaterials.rekindled_dispatcher))
			);
	
	public static final IRecipe rekindled_dispatcher = RecipeHelper.createRecipe("rekindled_dispatcher", new ItemStack(HAMaterials.rekindled_dispatcher),
			new String[] { "MMM", "MVM", "MMM" },
			Pair.of('M', Ingredient.fromItem(SRPItems.dod_drop)),
			Pair.of('V', Ingredient.fromItem(HAMaterials.vengence_core))
			);
	
	public static final IRecipe seismic_warhammer = RecipeHelper.createRecipe("seismic_warhammer", new ItemStack(HATools.seismic_warhammer),
			new String[] { "VSA", "HRS", "RHV" },
			Pair.of('V', Ingredient.fromItem(HAMaterials.vengence_core)),
			Pair.of('S', Ingredient.fromItem(SRPItems.vileshell)),
			Pair.of('A', Ingredient.fromItem(SRPItems.ashyco_drop)),
			Pair.of('H', Ingredient.fromItem(SRPItems.hardbone)),
			Pair.of('R', Ingredient.fromItem(HAMaterials.rewoven_flesh))
			);
	
	public static final IRecipe rewoven_flesh = RecipeHelper.createRecipe("rewoven_flesh", new ItemStack(HAMaterials.rewoven_flesh, 4),
			new String[] { " UF", "UFU", "FU " },
			Pair.of('U', Ingredient.fromItem(HAMaterials.unraveled_muscles)),
			Pair.of('F', Ingredient.fromItem(SRPItems.infected_drop))
			);
}
