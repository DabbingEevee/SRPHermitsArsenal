package com.existingeevee.hermitsarsenal.init;

import com.existingeevee.hermitsarsenal.items.ItemCytokineMutator;
import com.existingeevee.hermitsarsenal.items.ItemGeneDesplicer;
import com.existingeevee.hermitsarsenal.items.ItemIgnisChakram;
import com.existingeevee.hermitsarsenal.items.ItemIgnisKnife;
import com.existingeevee.hermitsarsenal.items.ItemLongarmAppendage;
import com.existingeevee.hermitsarsenal.items.ItemOutbreakDagger;
import com.existingeevee.hermitsarsenal.items.ItemSeismicHammer;
import com.existingeevee.hermitsarsenal.items.ItemSeizerSpear;

import net.minecraft.item.Item;

public class HATools {

	public static final Item gene_desplicer = new ItemGeneDesplicer();
	public static final Item outbreak_dagger = new ItemOutbreakDagger();
	public static final Item ignis_knife = new ItemIgnisKnife();
	public static final Item seizer_spear = new ItemSeizerSpear();
	public static final Item seismic_warhammer = new ItemSeismicHammer();
	
	public static final Item ignis_chakram = new ItemIgnisChakram(); 
	public static final Item cytokine_mutator = new ItemCytokineMutator();
	//public static final Item plaguebringer = new ItemCytokineMutator();

	public static final Item longarm_appendage = new ItemLongarmAppendage("longarm_appendage", 1.75);
	public static final Item extended_appendage = new ItemLongarmAppendage("extended_appendage", 2.75);
}
