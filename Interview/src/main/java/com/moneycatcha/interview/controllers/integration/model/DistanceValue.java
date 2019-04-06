package com.moneycatcha.interview.controllers.integration.model;

public class DistanceValue {
	private String formatted;
	private double value;
	
	
	public DistanceValue(String formatted, double value) {
		super();
		this.formatted = formatted;
		this.value = value;
	}
	public String getFormatted() {
		return formatted;
	}
	public void setFormatted(String formatted) {
		this.formatted = formatted;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	
}
