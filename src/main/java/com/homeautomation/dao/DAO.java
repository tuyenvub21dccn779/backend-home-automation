package com.homeautomation.dao;

import java.sql.Connection;
import java.sql.DriverManager;
 
public class DAO {
public static Connection con;
     
    public DAO(){
        if(con == null){
            String dbUrl = "jdbc:mysql://localhost:3307/automationhome?autoReconnect=true&useSSL=false";
            String dbClass = "com.mysql.cj.jdbc.Driver";
 
            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection (dbUrl, "root", "root123");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
