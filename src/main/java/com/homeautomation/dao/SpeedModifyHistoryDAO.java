package com.homeautomation.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.homeautomation.model.Fan;
import com.homeautomation.model.FanControlHistory;
import com.homeautomation.model.Speed;
import com.homeautomation.model.SpeedModifyHistory;

public class SpeedModifyHistoryDAO extends DAO {

	public SpeedModifyHistoryDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean insertSpeedModifyHistory(SpeedModifyHistory tsmh) {
		boolean kq = false;
		String sqlInsertTSMH = "INSERT tblSpeedModifyHistory(time, speed, threshold, tblSpeedid, tblHomeOwnerid) "
				+ " VALUES(?,?,?,?,?);";
		String sqlUpdateSpeed = "UPDATE tblSpeed SET speed=?, threshold=?" 
				+ " WHERE id=?";
		try{
			this.con.setAutoCommit(false);
			PreparedStatement psInsertSMH = con.prepareStatement(sqlInsertTSMH);
			psInsertSMH.setString(1, tsmh.getTime());
			psInsertSMH.setInt(2, tsmh.getSpeed());
			psInsertSMH.setFloat(3, tsmh.getThreshold());
			psInsertSMH.setInt(4, tsmh.getSpeedObject().getId());
			psInsertSMH.setInt(5, tsmh.getHomeOwner().getId());
			psInsertSMH.executeUpdate();
			
			PreparedStatement psUpdateSpeed = con.prepareStatement(sqlUpdateSpeed);
        	psUpdateSpeed.setInt(1, tsmh.getSpeedObject().getSpeed());
        	psUpdateSpeed.setFloat(2, tsmh.getSpeedObject().getThreshold());
        	psUpdateSpeed.setInt(3, tsmh.getSpeedObject().getId());
        	psUpdateSpeed.executeUpdate();
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
	
	public List<SpeedModifyHistory> getSpeedModifyHistoryByFanId(Integer id) {
		List<SpeedModifyHistory> smhList = new ArrayList<>();
		SpeedDAO speedDAO = new SpeedDAO();
		HomeOwnerDAO homeOwnerDAO = new HomeOwnerDAO();
		String sql = "SELECT \r\n"
				+ "    smh.threshold, \r\n"
				+ "    smh.time, \r\n"
				+ "    smh.speed,\r\n"
				+ "    smh.tblSpeedid,\r\n"
				+ "    smh.tblHomeOwnerid,\r\n"
				+ "    smh.id\r\n"
				+ "FROM \r\n"
				+ "    tblSpeedModifyHistory smh\r\n"
				+ "JOIN \r\n"
				+ "    tblSpeed s ON smh.tblSpeedid = s.id\r\n"
				+ "JOIN \r\n"
				+ "    tblFan f ON s.tblFanTblDeviceid = f.tblDeviceid\r\n"
				+ "WHERE \r\n"
				+ "    f.tblDeviceid = ? \r\n"
				+ "ORDER BY smh.time DESC;";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Set to Vietnam time zone
		try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
 
            ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				SpeedModifyHistory smh = new SpeedModifyHistory();
				smh.setId(rs.getInt("id"));
				smh.setSpeed(rs.getInt("speed"));
				smh.setThreshold(rs.getFloat("threshold"));
				Timestamp timestamp = rs.getTimestamp("time");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                smh.setTime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }

	            Speed speedObject = speedDAO.getSpeedBySpeedId(rs.getInt("tblSpeedid"));
	            smh.setSpeedObject(speedObject);
	            smh.setHomeOwner(homeOwnerDAO.getHomeOwnerByHomeOwnerId(rs.getInt("tblHomeOwnerid")));
	            smhList.add(smh);
			}
            
        }catch(Exception e){
            e.printStackTrace();
        }
		return smhList;
	}
}
