package com.homeautomation.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.homeautomation.model.Bulb;
import com.homeautomation.model.BulbControlHistory;
import com.homeautomation.model.Fan;
import com.homeautomation.model.FanControlHistory;

public class FanControlHistoryDAO extends DAO {

	public FanControlHistoryDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean insertFanControlHistory(FanControlHistory fch) {
		boolean kq = false;
		String sqlUpdateDevice = "UPDATE tblDevice SET state=?, mode=?"
				+ " WHERE id=?";
		String sqlInsertDCH = "INSERT tblDeviceControlHistory (starttime, mode, state) VALUES(?,?,?);";
		String sqlInsertFCH;
		if(fch.getHomeOwner() == null) {
			sqlInsertFCH = "INSERT tblFanControlHistory(tblDeviceControlHistoryid, tblFantblDeviceid, threshold) VALUES(?,?,?);";
		} else {
			sqlInsertFCH = "INSERT tblFanControlHistory(tblDeviceControlHistoryid, tblFantblDeviceid, threshold, tblHomeOwnerid) VALUES(?,?,?,?);";
		}
		try {
			updateNewestFanControlHistory(fch.getFan().getId(), fch.getEndtime());
			this.con.setAutoCommit(false);
			PreparedStatement psInsertDCH = con.prepareStatement(sqlInsertDCH, 
					Statement.RETURN_GENERATED_KEYS);
			psInsertDCH.setString(1, fch.getStarttime());
			psInsertDCH.setInt(2, fch.getMode());
			psInsertDCH.setInt(3, fch.getState());
			
			psInsertDCH.executeUpdate();
			
			//get id of the new inserted client
            ResultSet generatedKeys = psInsertDCH.getGeneratedKeys();
            if (generatedKeys.next()) {
            	fch.setId(generatedKeys.getInt(1));
            }
            
            PreparedStatement psInsertFCH = con.prepareStatement(sqlInsertFCH);
            psInsertFCH.setInt(1, fch.getId());
            psInsertFCH.setInt(2, fch.getFan().getId());
            psInsertFCH.setFloat(3, fch.getThreshold());
            
            if(fch.getHomeOwner() != null) {
            	psInsertFCH.setInt(4, fch.getHomeOwner().getId());
            }
            
            psInsertFCH.executeUpdate();
            
            PreparedStatement psUpdateDevice = con.prepareStatement(sqlUpdateDevice);
            psUpdateDevice.setInt(1, fch.getState());
            psUpdateDevice.setInt(2, fch.getMode());
            psUpdateDevice.setInt(3, fch.getFan().getId());
            psUpdateDevice.executeUpdate();
            
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
	
	public boolean updateNewestFanControlHistory(Integer fanId, String endTime) {
		boolean kq = false;
		String sqlNewestDCH = "SELECT dch.id, dch.endtime\r\n"
				+ "FROM tblFanControlHistory fch\r\n"
				+ "JOIN tblDeviceControlHistory dch ON fch.tblDeviceControlHistoryid = dch.id\r\n"
				+ "WHERE fch.tblFantblDeviceid = ? -- Replace ? with the specific bulb ID\r\n"
				+ "ORDER BY dch.starttime DESC\r\n"
				+ "LIMIT 1;";
		String sqlUpdateNewestDCH = "UPDATE tblDeviceControlHistory set endtime=? WHERE id = ?";
		try {
			this.con.setAutoCommit(false);
			PreparedStatement psSelectNewestDCH = con.prepareStatement(sqlNewestDCH);
			psSelectNewestDCH.setInt(1, fanId);
			ResultSet rs = psSelectNewestDCH.executeQuery();
			if(rs.next() && rs.getString("endtime") == null) {
				PreparedStatement psUpdateNewestDCH = con.prepareStatement(sqlUpdateNewestDCH);
				psUpdateNewestDCH.setString(1, endTime);
				psUpdateNewestDCH.setInt(2, rs.getInt("id"));
				psUpdateNewestDCH.executeUpdate();
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
	
	public List<FanControlHistory> getFanControlHistoryByFanId(Integer id) {
		FanDAO fanDAO = new FanDAO();
		Fan fan = fanDAO.getFanById(id);
		List<FanControlHistory> fchList = new ArrayList<>();
		String sql = "SELECT dch.id, dch.endtime, dch.starttime, dch.mode, dch.state, fch.threshold\r\n"
				+ "FROM tblFanControlHistory fch\r\n"
				+ "JOIN tblDeviceControlHistory dch ON fch.tblDeviceControlHistoryid = dch.id\r\n"
				+ "WHERE fch.tblFantblDeviceid = ? -- Replace ? with the specific bulb ID\r\n"
				+ "ORDER BY dch.starttime DESC\r\n";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Set to Vietnam time zone
		try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, fan.getId());
 
            ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				FanControlHistory fch = new FanControlHistory();
				fch.setId(rs.getInt("id"));
				fch.setMode(rs.getInt("mode"));
				fch.setState(rs.getInt("state"));
				fch.setThreshold(rs.getFloat("threshold"));
				Timestamp timestamp = rs.getTimestamp("starttime");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                fch.setStarttime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }
	            timestamp = rs.getTimestamp("endtime");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                fch.setEndtime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }
	            fch.setFan(fan);
				fchList.add(fch);
			}
            
        }catch(Exception e){
            e.printStackTrace();
        }
		
		return fchList;
	}
	
}
