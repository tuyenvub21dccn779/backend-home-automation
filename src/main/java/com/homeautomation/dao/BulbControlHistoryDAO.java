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
import com.homeautomation.model.SensorData;

public class BulbControlHistoryDAO extends DAO {

	public BulbControlHistoryDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean insertBulbControlHistory(BulbControlHistory bch) {
		boolean kq = false;
		String sqlInsertDCH = "INSERT tblDeviceControlHistory (starttime, mode, state) VALUES(?,?,?);";
		String sqlInsertBCH;
		if(bch.getHomeOwner() == null) {
			sqlInsertBCH = "INSERT tblBulbControlHistory(tblDeviceControlHistoryid, tblBulbtblDeviceid) VALUES(?,?);";
		} else {
			sqlInsertBCH = "INSERT tblBulbControlHistory(tblDeviceControlHistoryid, tblBulbtblDeviceid, tblHomeOwnerid) VALUES(?, ?,?);";
		}
		try {
			updateNewestBulbControlHistory(bch.getBulb().getId(), bch.getEndtime());
			this.con.setAutoCommit(false);
			PreparedStatement psInsertDCH = con.prepareStatement(sqlInsertDCH, 
					Statement.RETURN_GENERATED_KEYS);
			psInsertDCH.setString(1, bch.getStarttime());
			psInsertDCH.setInt(2, bch.getMode());
			psInsertDCH.setInt(3, bch.getState());
			
			psInsertDCH.executeUpdate();
			
			//get id of the new inserted client
            ResultSet generatedKeys = psInsertDCH.getGeneratedKeys();
            if (generatedKeys.next()) {
            	bch.setId(generatedKeys.getInt(1));
            }
            
            PreparedStatement psInsertBCH = con.prepareStatement(sqlInsertBCH);
            psInsertBCH.setInt(1, bch.getId());
            psInsertBCH.setInt(2, bch.getBulb().getId());
            
            if(bch.getHomeOwner() != null) {
            	psInsertBCH.setInt(3, bch.getHomeOwner().getId());
            }
            
            psInsertBCH.executeUpdate();
            
            BulbDAO bulbDAO = new BulbDAO();
            bulbDAO.updateBulb(bch.getBulb());
            
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
	
	public boolean updateNewestBulbControlHistory(Integer bulbId, String endTime) {
		boolean kq = false;
		String sqlNewestDCH = "SELECT dch.id, dch.endtime\r\n"
				+ "FROM tblBulbControlHistory bch\r\n"
				+ "JOIN tblDeviceControlHistory dch ON bch.tblDeviceControlHistoryid = dch.id\r\n"
				+ "WHERE bch.tblBulbtblDeviceid = ? -- Replace ? with the specific bulb ID\r\n"
				+ "ORDER BY dch.starttime DESC\r\n"
				+ "LIMIT 1;\r\n"
				+ "";
		String sqlUpdateNewestDCH = "UPDATE tblDeviceControlHistory set endtime=? WHERE id = ?";
		try {
			this.con.setAutoCommit(false);
			PreparedStatement psSelectNewestDCH = con.prepareStatement(sqlNewestDCH);
			psSelectNewestDCH.setInt(1, bulbId);
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
	
	public List<BulbControlHistory> getBulbControlHistoryByBulbId(Integer id) {
		BulbDAO bulbDAO = new BulbDAO();
		Bulb bulb = bulbDAO.getBulbById(id);
		List<BulbControlHistory> bchList = new ArrayList<>();
		String sql = "SELECT dch.id, dch.starttime, dch.endtime, dch.mode, dch.state\r\n"
				+ "FROM tblBulbControlHistory bch\r\n"
				+ "JOIN tblDeviceControlHistory dch ON bch.tblDeviceControlHistoryid = dch.id\r\n"
				+ "WHERE bch.tblBulbtblDeviceid = ? -- Replace ? with the specific bulb ID\r\n"
				+ "ORDER BY dch.starttime DESC\r\n"
				+ "";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Set to Vietnam time zone
		try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bulb.getId());
 
            ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				BulbControlHistory bch = new BulbControlHistory();
				bch.setId(rs.getInt("id"));
				bch.setMode(rs.getInt("mode"));
				bch.setState(rs.getInt("state"));
				Timestamp timestamp = rs.getTimestamp("starttime");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                bch.setStarttime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }
	            timestamp = rs.getTimestamp("endtime");
	            if (timestamp != null) {
	                String formattedDate = dateFormat.format(timestamp);
	                bch.setEndtime(formattedDate); // Assuming setTime accepts a String in SensorData class
	            }
				bch.setBulb(bulb);
				bchList.add(bch);
			}
            
        }catch(Exception e){
            e.printStackTrace();
        }
		
		return bchList;
	}
	
	
}
