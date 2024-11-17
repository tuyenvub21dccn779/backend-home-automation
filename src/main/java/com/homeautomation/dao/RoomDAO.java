package com.homeautomation.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.homeautomation.model.Room;

public class RoomDAO extends DAO {

	public RoomDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Room> getAllRoom() {
		String sql = "SELECT id, name, description FROM tblRoom";
		ArrayList<Room> rooms = new ArrayList<>();
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Room room = new Room();
				room.setId(rs.getInt("id"));
				room.setName(rs.getString("name"));
				room.setDescription(rs.getString("description"));
				FanDAO fanDAO = new FanDAO();
				room.setFans(fanDAO.getFanByRoomId(room.getId()));
				BulbDAO bulbDAO = new BulbDAO();
				room.setBulbs(bulbDAO.getBulbByRoomId(room.getId()));
				FlameSensorDAO fsDAO = new FlameSensorDAO();
				room.setFlameSensors(fsDAO.getFlameSensorByRoomId(room.getId()));
				LightSensorDAO lsDAO = new LightSensorDAO();
				room.setLightSensors(lsDAO.getLightSensorByRoomId(room.getId()));
				TemperatureSensorDAO tsDAO = new TemperatureSensorDAO();
				room.setTemperatureSensors(tsDAO.getTemperatureSensorByRoomId(room.getId()));
				rooms.add(room);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return rooms;
	}
	
	public Room getRoomById(Integer id) {
		String sql = "SELECT id, name, description FROM tblRoom WHERE id = ?";
		Room room = new Room();
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				room.setId(rs.getInt("id"));
				room.setName(rs.getString("name"));
				room.setDescription(rs.getString("description"));
				FanDAO fanDAO = new FanDAO();
				room.setFans(fanDAO.getFanByRoomId(room.getId()));
				BulbDAO bulbDAO = new BulbDAO();
				room.setBulbs(bulbDAO.getBulbByRoomId(room.getId()));
				FlameSensorDAO fsDAO = new FlameSensorDAO();
				room.setFlameSensors(fsDAO.getFlameSensorByRoomId(room.getId()));
				LightSensorDAO lsDAO = new LightSensorDAO();
				room.setLightSensors(lsDAO.getLightSensorByRoomId(room.getId()));
				TemperatureSensorDAO tsDAO = new TemperatureSensorDAO();
				room.setTemperatureSensors(tsDAO.getTemperatureSensorByRoomId(room.getId()));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return room;
	}
}
