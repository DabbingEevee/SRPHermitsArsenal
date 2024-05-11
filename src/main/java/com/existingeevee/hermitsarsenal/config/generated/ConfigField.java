package com.existingeevee.hermitsarsenal.config.generated;

public final class ConfigField {

	public ConfigField(String keyVal, double defaultVal, String comment) {
		this.keyVal = keyVal;
		this.defaultVal = defaultVal;
		this.comment = comment;
	}

	final String keyVal;
	final double defaultVal;
	final String comment;

	public String getKeyVal() {
		return keyVal;
	}

	public double getDefaultVal() {
		return defaultVal;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public int hashCode() {
		return keyVal.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ConfigField) {
			return ((ConfigField) other).keyVal.equals(keyVal);
		}
		return false;
	}
}
