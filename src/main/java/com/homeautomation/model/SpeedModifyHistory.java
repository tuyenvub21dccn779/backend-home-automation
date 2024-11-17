package com.homeautomation.model;

public class SpeedModifyHistory {
	private Integer id;
	private String time;
	private Integer speed;
	private Float threshold;
	private HomeOwner homeOwner;
	private Speed speedObject;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
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
	public Speed getSpeedObject() {
		return speedObject;
	}
	public void setSpeedObject(Speed speedObject) {
		this.speedObject = speedObject;
	}
	
}
