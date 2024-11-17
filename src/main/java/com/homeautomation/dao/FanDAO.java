package com.homeautomation.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.homeautomation.model.Fan;
import com.homeautomation.model.Speed;

public class FanDAO extends DAO {
	public FanDAO() {
		super();
	}
	
	public ArrayList<Fan> getFanByRoomId(Integer id) {
		ArrayList<Fan> fans = new ArrayList<>();
		String sql = "{call getFansByRoomId(?)}";
		try {
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				Fan fan = new Fan();
				fan.setId(rs.getInt("id"));
				fan.setName(rs.getString("name"));
				fan.setDescription(rs.getString("description"));
				fan.setLocation(rs.getString("location"));
				fan.setState(rs.getInt("state"));
				fan.setMode(rs.getInt("mode"));
				SpeedDAO speedDAO = new SpeedDAO();
				fan.setSpeeds(speedDAO.getSpeedByFanId(fan.getId()));
				fans.add(fan);
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return fans;
	}
	
	public Fan getFanById(Integer id) {
		Fan fan = new Fan();
		String sql = "SELECT \r\n" + 
				"        d.id,\r\n" + 
				"        d.name,\r\n" + 
				"        d.description,\r\n" + 
				"        d.location,\r\n" + 
				"        d.state,\r\n" + 
				"        d.mode\r\n" + 
				"    FROM \r\n" + 
				"        tblFan AS f\r\n" + 
				"    INNER JOIN \r\n" + 
				"        tblDevice AS d ON f.tblDeviceid = d.id\r\n" + 
				"    WHERE \r\n" + 
				"        f.tblDeviceid = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				fan.setId(rs.getInt("id"));
				fan.setName(rs.getString("name"));
				fan.setDescription(rs.getString("description"));
				fan.setLocation(rs.getString("location"));
				fan.setState(rs.getInt("state"));
				fan.setMode(rs.getInt("mode"));
				SpeedDAO speedDAO = new SpeedDAO();
				fan.setSpeeds(speedDAO.getSpeedByFanId(fan.getId()));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return fan;
	}
	
	public boolean updateFan(Fan fan) {
		boolean kq = false;
		String sqlUpdateDevice = "UPDATE tblDevice SET state=?, mode=?"
				+ " WHERE id=?";
		String sqlUpdateSpeed = "UPDATE tblSpeed SET speed=?, threshold=?" 
				+ " WHERE id=?";
		String sqlKiemtra = "SELECT id FROM tblSpeed WHERE id=? AND tblFanTblDeviceid=?";
		try{
			this.con.setAutoCommit(false);
            PreparedStatement psUpdateDevice = con.prepareStatement(sqlUpdateDevice);
            psUpdateDevice.setInt(1, fan.getState());
            psUpdateDevice.setInt(2, fan.getMode());
            psUpdateDevice.setInt(3, fan.getId());
            psUpdateDevice.executeUpdate();
            
            for(Speed speed : fan.getSpeeds()) {
            	PreparedStatement ps = con.prepareStatement(sqlKiemtra);
                ps.setInt(1, speed.getId());
                ps.setInt(2, fan.getId());
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                	PreparedStatement psUpdateSpeed = con.prepareStatement(sqlUpdateSpeed);
                	psUpdateSpeed.setInt(1, speed.getSpeed());
                	psUpdateSpeed.setFloat(2, speed.getThreshold());
                	psUpdateSpeed.setInt(3, speed.getId());
                	psUpdateSpeed.executeUpdate();
                }
            }
            this.con.commit();
            kq=true;
        }catch(Exception e){
            try{
                this.con.rollback();
            }catch(Exception ex){
                kq=false;
                ex.printStackTrace();
            }
            kq=false;
            e.printStackTrace();
        }finally{
            try{
                this.con.setAutoCommit(true);
            }catch(Exception ex){
                kq=false;
                ex.printStackTrace();
            }
        }       
        return kq;
	}
	
}
