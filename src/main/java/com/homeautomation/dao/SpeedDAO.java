package com.homeautomation.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.homeautomation.model.Fan;
import com.homeautomation.model.Speed;

public class SpeedDAO extends DAO {

	public SpeedDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Speed> getSpeedByFanId(Integer id) {
		ArrayList<Speed> speeds = new ArrayList<>();
		String sql = "{call getSpeedsByFanId(?)}";
		try {
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				Speed speed = new Speed();
				speed.setId(rs.getInt("id"));
				speed.setSpeed(rs.getInt("speed"));
				speed.setThreshold(rs.getFloat("threshold"));
				speeds.add(speed);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return speeds;
	}
	
	public Speed getSpeedBySpeedId(Integer id) {
		Speed speed = new Speed();
		String sql = "SELECT \r\n" + 
				"        s.id,\r\n" + 
				"        s.speed,\r\n" + 
				"        s.threshold\r\n" + 
				"    FROM \r\n" + 
				"        tblSpeed AS s\r\n" + 
				"    WHERE \r\n" + 
				"        s.id = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				speed.setId(rs.getInt("id"));
				speed.setSpeed(rs.getInt("speed"));
				speed.setThreshold(rs.getFloat("threshold"));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return speed;
	}
}
