package com.homeautomation.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.homeautomation.model.TemperatureSensor;

public class TemperatureSensorDAO extends DAO {

	public TemperatureSensorDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<TemperatureSensor> getTemperatureSensorByRoomId(int id) {
		ArrayList<TemperatureSensor> temperatureSensors = new ArrayList<>();
		String sql = "{call getTemperatureSensorsByRoomId(?)}";
		try {
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				TemperatureSensor temperatureSensor = new TemperatureSensor();
				temperatureSensor.setId(rs.getInt("id"));
				temperatureSensor.setName(rs.getString("name"));
				temperatureSensor.setDescription(rs.getString("description"));
				temperatureSensor.setLocation(rs.getString("location"));
				temperatureSensor.setAccuracy(rs.getFloat("accuracy"));
				temperatureSensors.add(temperatureSensor);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return temperatureSensors;
	}
	
	public TemperatureSensor getTemperatureSensorById(int id) {
		TemperatureSensor temperatureSensor = new TemperatureSensor();
		String sql = "SELECT \r\n" + 
				"        ts.accuracy,\r\n" + 
				"        s.id,\r\n" + 
				"        s.name ,\r\n" + 
				"        s.description ,\r\n" + 
				"        s.location \r\n" + 
				"    FROM \r\n" + 
				"        tblTemperatureSensor AS ts\r\n" + 
				"    INNER JOIN \r\n" + 
				"        tblSensor AS s ON ts.tblSensorid = s.id\r\n" + 
				"    WHERE \r\n" + 
				"        ts.tblSensorid = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				
				temperatureSensor.setId(rs.getInt("id"));
				temperatureSensor.setName(rs.getString("name"));
				temperatureSensor.setDescription(rs.getString("description"));
				temperatureSensor.setLocation(rs.getString("location"));
				temperatureSensor.setAccuracy(rs.getFloat("accuracy"));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return temperatureSensor;
	}
}
