package com.homeautomation.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.homeautomation.model.HomeOwner;

public class HomeOwnerDAO extends DAO {

	public HomeOwnerDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HomeOwner getHomeOwnerByHomeOwnerId(Integer id) {
		HomeOwner homeOwner = new HomeOwner();
		String sql = "SELECT id, name FROM tblHomeOwner WHERE id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				homeOwner.setId(rs.getInt("id"));
				homeOwner.setName(rs.getString("name"));
			}
		} catch(Exception e){
            e.printStackTrace();
		}
		return homeOwner; 
	}
}
