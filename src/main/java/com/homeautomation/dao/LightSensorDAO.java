package com.homeautomation.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.homeautomation.model.LightSensor;

public class LightSensorDAO extends DAO {
	public LightSensorDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<LightSensor> getLightSensorByRoomId(int id) {
		ArrayList<LightSensor> lightSensors = new ArrayList<>();
		String sql = "{call getFlameSensorsByRoomId(?)}";
		try {
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				LightSensor lightSensor = new LightSensor();
				lightSensor.setId(rs.getInt("id"));
				lightSensor.setName(rs.getString("name"));
				lightSensor.setDescription(rs.getString("description"));
				lightSensor.setLocation(rs.getString("location"));
				lightSensors.add(lightSensor);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return lightSensors;
	}
	
	public LightSensor getLightSensorById(int id) {
		LightSensor lightSensor = new LightSensor();
		String sql = "SELECT \r\n" + 
				"        s.id ,\r\n" + 
				"        s.name ,\r\n" + 
				"        s.description,\r\n" + 
				"        s.location\r\n" + 
				"    FROM \r\n" + 
				"        tblLightSensor AS ls\r\n" + 
				"    INNER JOIN \r\n" + 
				"        tblSensor AS s ON ls.tblSensorid = s.id\r\n" + 
				"    WHERE \r\n" + 
				"        ls.tblSensorid = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				
				lightSensor.setId(rs.getInt("id"));
				lightSensor.setName(rs.getString("name"));
				lightSensor.setDescription(rs.getString("description"));
				lightSensor.setLocation(rs.getString("location"));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return lightSensor;
	}
}
