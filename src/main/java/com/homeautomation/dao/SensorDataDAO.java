package com.homeautomation.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import com.homeautomation.model.Sensor;
import com.homeautomation.model.SensorData;

public class SensorDataDAO extends DAO {

	public SensorDataDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void insertSensorData(SensorData sensorData) {
		String sql = "INSERT INTO tblsensordata\r\n" + 
				"(time,\r\n" + 
				"value,\r\n" + 
				"tblSensorid)\r\n" + 
				"VALUES(?, ?, ?);";
		try{
            PreparedStatement ps = con.prepareStatement(sql,
                              Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sensorData.getTime());
            ps.setFloat(2, sensorData.getValue());
            ps.setInt(3, sensorData.getSensor().getId());
 
            ps.executeUpdate();
             
            //get id of the new inserted client
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
            	sensorData.setId(generatedKeys.getInt(1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	
	public List<SensorData> getDataBySensorId(Integer id) {
		FlameSensorDAO fsDAO = new FlameSensorDAO();
		LightSensorDAO lsDAO = new LightSensorDAO();
		TemperatureSensorDAO tsDAO = new TemperatureSensorDAO();
		Sensor sensor = fsDAO.getFlameSensorById(id);
		List<SensorData> sensorDataList = new ArrayList<>();
		if(sensor.getId() == null) {
			sensor = lsDAO.getLightSensorById(id);
		}
		if(sensor.getId() == null) {
			sensor = tsDAO.getTemperatureSensorById(id);
		}
		String sql = "SELECT id, time, value FROM tblSensorData WHERE tblSensorid=? ORDER BY time DESC";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Set to Vietnam time zone
		try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, sensor.getId());
 
            ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				SensorData sensorData = new SensorData();
				sensorData.setId(rs.getInt("id"));
				Timestamp timestamp = rs.getTimestamp("time");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                sensorData.setTime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }
				sensorData.setValue(rs.getFloat("value"));
				sensorData.setSensor(sensor);
				sensorDataList.add(sensorData);
			}
            
        }catch(Exception e){
            e.printStackTrace();
        }
		return sensorDataList;
	}
	
	public SensorData getNewestDataBySensorId(Integer id) {
		FlameSensorDAO fsDAO = new FlameSensorDAO();
		LightSensorDAO lsDAO = new LightSensorDAO();
		TemperatureSensorDAO tsDAO = new TemperatureSensorDAO();
		Sensor sensor = fsDAO.getFlameSensorById(id);
		SensorData sensorData = new SensorData();
		if(sensor.getId() == null) {
			sensor = lsDAO.getLightSensorById(id);
		}
		if(sensor.getId() == null) {
			sensor = tsDAO.getTemperatureSensorById(id);
		}
		String sql = "SELECT id, time, value FROM tblSensorData WHERE tblSensorid=? ORDER BY time DESC LIMIT 1";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Set to Vietnam time zone
		try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, sensor.getId());
 
            ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				sensorData.setId(rs.getInt("id"));
				Timestamp timestamp = rs.getTimestamp("time");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                sensorData.setTime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }
				sensorData.setValue(rs.getFloat("value"));
				sensorData.setSensor(sensor);
			}
            
        }catch(Exception e){
            e.printStackTrace();
        }
		return sensorData;
	}
}
