package com.existingeevee.hermitsarsenal.items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.existingeevee.hermitsarsenal.HermitsArsenal;
import com.existingeevee.hermitsarsenal.MiscUtils;
import com.existingeevee.hermitsarsenal.config.generated.IAutoconfig;
import com.existingeevee.hermitsarsenal.misc.ClientAction;
import com.existingeevee.hermitsarsenal.misc.HAToolMaterials;
import com.existingeevee.hermitsarsenal.misc.IHasProbabilityProc;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGeneDesplicer extends ItemSword implements IHasProbabilityProc, IMultipartHitItem, IAutoconfig, IAutoformat {
	
	public ItemGeneDesplicer() {
		super(HAToolMaterials.GENE_DESPLICER);
		this.setRegistryName("gene_desplicer");
		this.setTranslationKey("gene_desplicer");
		this.setCreativeTab(HermitsArsenal.tab);
	}
	
	public ItemGeneDesplicer(ToolMaterial mat, String name) {
		super(mat);
		this.setRegistryName(name);
		this.setTranslationKey(name);
		this.setCreativeTab(HermitsArsenal.tab);
	}

	private static final Field resistanceS$EntityPMalleable = ObfuscationReflectionHelper.findField(EntityPMalleable.class, "resistanceS");
	private static final Field resistanceI$EntityPMalleable = ObfuscationReflectionHelper.findField(EntityPMalleable.class, "resistanceI");

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		this.format(stack, worldIn, tooltip, flagIn);
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (attacker instanceof EntityPlayer)
			onHitEntityOrBodyPart(stack, target, (EntityPlayer) attacker);
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void onHitEntityOrBodyPart(ItemStack stack, EntityLivingBase target, EntityPlayer attacker) {		
		if (target instanceof EntityPMalleable && !target.world.isRemote) {
			
			if (this.shouldProc(stack, procPercentage)) {
				EntityPMalleable parasite = (EntityPMalleable) target; 
				
				int totalPointsRemoved = 0;
				
				try {
					ArrayList<String> resistanceS = (ArrayList<String>) resistanceS$EntityPMalleable.get(parasite);
					ArrayList<Integer> resistanceI = (ArrayList<Integer>) resistanceI$EntityPMalleable.get(parasite);

					NBTTagCompound resistances = new NBTTagCompound();
					
					boolean changed = false;
					
					for (int i = 0; i < resistanceS.size() && i < resistanceI.size(); i++) {
						changed = true;
						
						int pointsToRemove = itemRand.nextInt(pointToRemove) + 1;
						totalPointsRemoved += pointsToRemove;
						int ada = resistanceI.get(i) - pointsToRemove;
						if (ada <= 0) {
							resistanceS.remove(i);
							resistanceI.remove(i);
							i--;
							continue;
						} else {
							resistanceI.set(i, ada);
						}
						
						resistances.setInteger(resistanceS.get(i), resistanceI.get(i));
					}
					
					NBTTagCompound data = new NBTTagCompound();
					data.setTag("resistances", resistances);
					data.setInteger("entity_id", parasite.getEntityId());
					
					CLEAR_ADA.runAsClient(target.world, 0, 0, 0, data);
					
					if (changed && target instanceof EntityLivingBase) {
						MiscUtils.playProcEffect(target);
						this.onProc((EntityLivingBase) target, attacker, totalPointsRemoved);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void onProc(EntityLivingBase target, EntityLivingBase attacker, int totalPointsRemoved) {
		target.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 10 * 20, 0));
	}
	
	public static final ClearResistanceClientAction CLEAR_ADA = new ClearResistanceClientAction();
	
	public static class ClearResistanceClientAction extends ClientAction {

		@Override
		@SideOnly(Side.CLIENT)
		@SuppressWarnings("unchecked")
		public void runAsClient(World world, double x, double y, double z, NBTBase data) {
			if (data instanceof NBTTagCompound) {
				NBTTagCompound comp = (NBTTagCompound) data;
				
				int entityID = comp.getInteger("entity_id");
				Entity ent = world.getEntityByID(entityID);
				if (ent instanceof EntityPMalleable) {
					EntityPMalleable parasite = (EntityPMalleable) ent;
					try {
						ArrayList<String> resistanceS = (ArrayList<String>) resistanceS$EntityPMalleable.get(parasite);
						ArrayList<Integer> resistanceI = (ArrayList<Integer>) resistanceI$EntityPMalleable.get(parasite);
						resistanceS.clear();
						resistanceI.clear();
						
						NBTTagCompound newResistances = comp.getCompoundTag("resistances");
						
						for (String key : newResistances.getKeySet()) {
							int val = newResistances.getInteger(key);
							resistanceS.add(key);
							resistanceI.add(val);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected double procPercentage = 0.5;
	protected int pointToRemove = 10;
	
	@Override
	public List<String> getFormatters(ItemStack stack, @Nullable World worldIn) {
		List<String> list = IAutoformat.super.getFormatters(stack, worldIn);
		list.add("" + DEC_FORMAT.format(pointToRemove));
		return list;
	}
	
	@Override
	public Set<Property> getConfigFields(Configuration config) {
		Set<Property> set = new LinkedHashSet<>();
		set.add(config.get(getRegistryName().getPath(), "Proc Chance", procPercentage, "The probability of the item procing. 0 will never proc, 0.5 will proc 50% of the times, and 1 will always proc.", 0, 1));
		set.add(config.get(getRegistryName().getPath(), "Max Points to Remove", pointToRemove, "The maximum amount of points that can be removed from EACH adaptation.", 0, Integer.MAX_VALUE));

		return set;
	}

	@Override
	public void readConfigFields(Map<String, Property> map) {
		procPercentage = map.get("Proc Chance").getDouble();
		pointToRemove = map.get("Max Points to Remove").getInt();
	}
}
