package com.homeautomation.model;

public class BulbControlHistory extends DeviceControlHistory {
	private HomeOwner homeOwner;
	private Bulb bulb;
	public HomeOwner getHomeOwner() {
		return homeOwner;
	}
	public void setHomeOwner(HomeOwner homeOwner) {
		this.homeOwner = homeOwner;
	}
	public Bulb getBulb() {
		return bulb;
	}
	public void setBulb(Bulb bulb) {
		this.bulb = bulb;
	}
	
}
