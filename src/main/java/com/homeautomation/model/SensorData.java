package com.homeautomation.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class SensorData {
	private Integer id;
	private String time;
	private Float value;
	private Sensor sensor;
	public Sensor getSensor() {
		return sensor;
	}
	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
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
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	
}
