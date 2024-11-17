package com.homeautomation.model;

public class Sensor {
	private Integer id;
	private String name;
	private String description;
	private String location;
	private Float workingvoltage;
	private String outputsignal;
	private String unit;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Float getWorkingvoltage() {
		return workingvoltage;
	}
	public void setWorkingvoltage(Float workingvoltage) {
		this.workingvoltage = workingvoltage;
	}
	public String getOutputsignal() {
		return outputsignal;
	}
	public void setOutputsignal(String outputsignal) {
		this.outputsignal = outputsignal;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
