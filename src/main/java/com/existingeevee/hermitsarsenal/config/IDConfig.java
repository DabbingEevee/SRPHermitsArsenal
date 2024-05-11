package com.existingeevee.hermitsarsenal.config;

import com.existingeevee.hermitsarsenal.HermitsArsenal;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = HermitsArsenal.MODID, name = HermitsArsenal.MODID + "/ids")
public class IDConfig {

	@Name("Ignis Chakram Entity ID") 
	@Comment("Change if ignis chakram entities are displaying wrongly or crashing.")
	public static int ignisChakram = 4200001;
	
	@Name("Spear Seizer Entity ID") 
	@Comment("Change if spear seizer entities are displaying wrongly or crashing.")
	public static int spearSeizer = 4200002;
	
	@Name("Hammer Wave Entity ID") 
	@Comment("Change if hammer wave entities are displaying wrongly or crashing.")
	public static int hammerWave = 4200003;
}
