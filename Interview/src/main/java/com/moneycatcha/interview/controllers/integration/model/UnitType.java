package com.moneycatcha.interview.controllers.integration.model;

public enum UnitType {

	METRIC("metric"), IMPERIAL("imperial");
	
	private final String unit;

	UnitType(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}
	
	public static UnitType getEnum(String value) {
		if(value == null || value.trim().equals("")) {
			throw new IllegalArgumentException("The value cannot be null or empty");
		}
		if(UnitType.IMPERIAL.getUnit().equalsIgnoreCase(value)) {
			return UnitType.IMPERIAL;
		}
		if(UnitType.METRIC.getUnit().equalsIgnoreCase(value)) {
			return UnitType.METRIC;
		}
		throw new IllegalArgumentException(value +" does not match any value");
		
		
	}
	
}
