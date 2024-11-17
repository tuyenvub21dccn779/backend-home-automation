package com.homeautomation.model;

import java.util.ArrayList;

public class Home {
	private Integer id;
	private String address;
	private String description;
	private ArrayList<Floor> floors;
	private ArrayList<HomeOwner> homeOwners;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<Floor> getFloors() {
		return floors;
	}
	public void setFloors(ArrayList<Floor> floors) {
		this.floors = floors;
	}
	public ArrayList<HomeOwner> getHomeOwners() {
		return homeOwners;
	}
	public void setHomeOwners(ArrayList<HomeOwner> homeOwners) {
		this.homeOwners = homeOwners;
	}
	
	
}
