package com.homeautomation.model;

import java.util.ArrayList;

public class Fan extends Device {
	private Float bladesize;
	private ArrayList<Speed> speeds;
	public Float getBladesize() {
		return bladesize;
	}
	public void setBladesize(Float bladesize) {
		this.bladesize = bladesize;
	}
	public ArrayList<Speed> getSpeeds() {
		return speeds;
	}
	public void setSpeeds(ArrayList<Speed> speeds) {
		this.speeds = speeds;
	}
	
	
}
