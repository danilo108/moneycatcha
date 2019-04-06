package com.moneycatcha.interview.controllers.integration.model;

public class DistanceResponse {

	private boolean found;
	private String origin;
	private String destination;
	private DistanceValue distance;
	private DistanceValue duration;
	private String originalJson;
	
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public DistanceValue getDistance() {
		return distance;
	}
	public void setDistance(DistanceValue distance) {
		this.distance = distance;
	}
	public DistanceValue getDuration() {
		return duration;
	}
	public void setDuration(DistanceValue duration) {
		this.duration = duration;
	}
	public String getOriginalJson() {
		return originalJson;
	}
	public void setOriginalJson(String originalJson) {
		this.originalJson = originalJson;
	}
	
	
}
