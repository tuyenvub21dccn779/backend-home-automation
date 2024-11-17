package com.homeautomation.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.homeautomation.model.Bulb;

public class BulbDAO extends DAO {

	public BulbDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Bulb> getBulbByRoomId(Integer id) {
		ArrayList<Bulb> bulbs = new ArrayList<>();
		String sql = "{call getBulbsByRoomId(?)}";
		try {
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				Bulb bulb = new Bulb();
				bulb.setId(rs.getInt("id"));
				bulb.setName(rs.getString("name"));
				bulb.setDescription(rs.getString("description"));
				bulb.setLocation(rs.getString("location"));
				bulb.setState(rs.getInt("state"));
				bulb.setMode(rs.getInt("mode"));
				bulbs.add(bulb);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return bulbs;
	}
	
	public Bulb getBulbById(Integer id) {
		Bulb bulb = new Bulb();
		String sql = "SELECT \r\n" + 
				"        d.id,\r\n" + 
				"        d.name ,\r\n" + 
				"        d.description,\r\n" + 
				"        d.location ,\r\n" + 
				"        d.state,\r\n" + 
				"        d.mode\r\n" + 
				"    FROM \r\n" + 
				"        tblBulb AS b\r\n" + 
				"    INNER JOIN \r\n" + 
				"        tblDevice AS d ON b.tblDeviceid = d.id\r\n" + 
				"    WHERE \r\n" + 
				"        b.tblDeviceid = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				
				bulb.setId(rs.getInt("id"));
				bulb.setName(rs.getString("name"));
				bulb.setDescription(rs.getString("description"));
				bulb.setLocation(rs.getString("location"));
				bulb.setState(rs.getInt("state"));
				bulb.setMode(rs.getInt("mode"));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return bulb;
	}
	
	public boolean updateBulb(Bulb bulb) {
		String sql = "UPDATE tblDevice SET name=?, description=?, location=?, state=?, mode=?"
				+ " WHERE id=?";
		try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, bulb.getName());
            ps.setString(2, bulb.getDescription());
            ps.setString(3, bulb.getLocation());
            ps.setInt(4, bulb.getState());
            ps.setInt(5, bulb.getMode());
            ps.setInt(6, bulb.getId());
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }       
        return true;
	}
}
