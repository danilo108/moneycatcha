package com.moneycatcha.interview.controllers.integration.model;

public enum TravelMode {

	
	DRIVE("driving"), WALK("walking");
	private final String mode;
	
	TravelMode(String mode){
		this.mode = mode;
	}
	
	
	public String getMode() {
		return mode;
	}
	
	public static TravelMode getEnum(String value) {
		if(value == null || value.trim().equals("")) {
			throw new IllegalArgumentException("The value cannot be null or empty");
		}
		if(TravelMode.DRIVE.getMode().equalsIgnoreCase(value)) {
			return TravelMode.DRIVE;
		}
		if(TravelMode.WALK.getMode().equalsIgnoreCase(value)) {
			return TravelMode.WALK;
		}
		throw new IllegalArgumentException(value +" does not match any value");
		
		
	}
	
}
