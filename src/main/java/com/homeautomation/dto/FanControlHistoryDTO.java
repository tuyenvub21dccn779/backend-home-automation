package com.homeautomation.dto;

public class FanControlHistoryDTO extends DeviceControlHistoryDTO {
	private Float threshold;
	public Float getThreshold() {
		return threshold;
	}
	public void setThreshold(Float threshold) {
		this.threshold = threshold;
	}
	
}
