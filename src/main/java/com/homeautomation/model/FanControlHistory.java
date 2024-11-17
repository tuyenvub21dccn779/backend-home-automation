package com.homeautomation.model;

public class FanControlHistory extends DeviceControlHistory {
	private Float threshold;
	private HomeOwner homeOwner;
	private Fan fan;
	public Float getThreshold() {
		return threshold;
	}
	public void setThreshold(Float threshold) {
		this.threshold = threshold;
	}
	public HomeOwner getHomeOwner() {
		return homeOwner;
	}
	public void setHomeOwner(HomeOwner homeOwner) {
		this.homeOwner = homeOwner;
	}
	public Fan getFan() {
		return fan;
	}
	public void setFan(Fan fan) {
		this.fan = fan;
	}
}
