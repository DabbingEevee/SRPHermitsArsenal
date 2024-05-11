package com.existingeevee.hermitsarsenal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.dhanantry.scapeandrunparasites.entity.EntityBody;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MiscUtils {

	public static Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClassSafe(line, packageName)).filter(c -> c != null).collect(Collectors.toSet());
	}

	private final static TreeMap<Integer, String> ROMAN_MAP = new TreeMap<Integer, String>();

	static {
		ROMAN_MAP.put(1000, "M");
		ROMAN_MAP.put(900, "CM");
		ROMAN_MAP.put(500, "D");
		ROMAN_MAP.put(400, "CD");
		ROMAN_MAP.put(100, "C");
		ROMAN_MAP.put(90, "XC");
		ROMAN_MAP.put(50, "L");
		ROMAN_MAP.put(40, "XL");
		ROMAN_MAP.put(10, "X");
		ROMAN_MAP.put(9, "IX");
		ROMAN_MAP.put(5, "V");
		ROMAN_MAP.put(4, "IV");
		ROMAN_MAP.put(1, "I");
	}

	public static String toRoman(int number) {
		int l = ROMAN_MAP.floorKey(number);
		if (number == l) {
			return ROMAN_MAP.get(number);
		}
		return ROMAN_MAP.get(l) + toRoman(number - l);
	}

	private static Class<?> getClassSafe(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
		}
		return null;
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

	public static void playProcEffect(Entity hit) {
		if (hit.world instanceof WorldServer) {
			((WorldServer) hit.world).playSound(null, hit.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 5f, 2);
			AxisAlignedBB box = hit.getEntityBoundingBox();
			Vec3d center = box.getCenter();
			((WorldServer) hit.world).spawnParticle(EnumParticleTypes.TOTEM, center.x, center.y, center.z, 40, (box.maxX - box.minX), (box.maxY - box.minY), (box.maxZ - box.minZ), 0);
			((WorldServer) hit.world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, center.x, center.y, center.z, 40, 0, 0, 0, 1d);
		}
	}

	public static Entity getActualEntity(Entity entity) {
		if (entity instanceof MultiPartEntityPart) {
			entity = (Entity) ((MultiPartEntityPart) entity).parent;
		} else if (entity instanceof EntityBody) {
			entity = ((EntityBody) entity).getFather();
		}
		return entity;
	}

	@SideOnly(Side.CLIENT)
	private static void emptyClientFunc() {
	}
}
