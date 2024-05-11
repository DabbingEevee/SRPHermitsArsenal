package com.existingeevee.hermitsarsenal.config.generated;

import java.util.Map;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public interface IAutoconfig {

	Set<Property> getConfigFields(Configuration config);

	void readConfigFields(Map<String, Property> map);

}
