package com.homeautomation.model;

import java.util.ArrayList;

public class Room {
	private Integer id;
	private String name;
	private String description;
	private ArrayList<Fan> fans;
	private ArrayList<Bulb> bulbs;
	private ArrayList<FlameSensor> flameSensors;
	private ArrayList<LightSensor> lightSensors;
	private ArrayList<TemperatureSensor> temperatureSensors;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<Fan> getFans() {
		return fans;
	}
	public void setFans(ArrayList<Fan> fans) {
		this.fans = fans;
	}
	public ArrayList<Bulb> getBulbs() {
		return bulbs;
	}
	public void setBulbs(ArrayList<Bulb> bulbs) {
		this.bulbs = bulbs;
	}
	public ArrayList<FlameSensor> getFlameSensors() {
		return flameSensors;
	}
	public void setFlameSensors(ArrayList<FlameSensor> flameSensors) {
		this.flameSensors = flameSensors;
	}
	public ArrayList<LightSensor> getLightSensors() {
		return lightSensors;
	}
	public void setLightSensors(ArrayList<LightSensor> lightSensors) {
		this.lightSensors = lightSensors;
	}
	public ArrayList<TemperatureSensor> getTemperatureSensors() {
		return temperatureSensors;
	}
	public void setTemperatureSensors(ArrayList<TemperatureSensor> temperatureSensors) {
		this.temperatureSensors = temperatureSensors;
	}
	
}
