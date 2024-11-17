package com.homeautomation.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.homeautomation.model.FlameSensor;

public class FlameSensorDAO extends DAO {

	public FlameSensorDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<FlameSensor> getFlameSensorByRoomId(int id) {
		ArrayList<FlameSensor> flameSensors = new ArrayList<>();
		String sql = "{call getFlameSensorsByRoomId(?)}";
		try {
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				FlameSensor flameSensor = new FlameSensor();
				flameSensor.setId(rs.getInt("id"));
				flameSensor.setName(rs.getString("name"));
				flameSensor.setDescription(rs.getString("description"));
				flameSensor.setLocation(rs.getString("location"));
				flameSensors.add(flameSensor);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return flameSensors;
	}
	
	public FlameSensor getFlameSensorById(int id) {
		FlameSensor flameSensor = new FlameSensor();
		String sql = "SELECT \r\n" + 
				"        s.id,\r\n" + 
				"        s.name,\r\n" + 
				"        s.description,\r\n" + 
				"        s.location\r\n" + 
				"    FROM \r\n" + 
				"        tblFlameSensor AS fs\r\n" + 
				"    INNER JOIN \r\n" + 
				"        tblSensor AS s ON fs.tblSensorid = s.id\r\n" + 
				"    WHERE \r\n" + 
				"        fs.tblSensorid = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				flameSensor.setId(rs.getInt("id"));
				flameSensor.setName(rs.getString("name"));
				flameSensor.setDescription(rs.getString("description"));
				flameSensor.setLocation(rs.getString("location"));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return flameSensor;
	}
}
