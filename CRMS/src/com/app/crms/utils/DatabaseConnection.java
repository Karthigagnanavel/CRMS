package com.app.crms.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/crms"; 
    private static final String USER = "test"; 
    private static final String PASSWORD = "test@123"; 
    private static Connection connection = null;
    
    public static Connection getConnection() {
    	try {
    		 Class.forName("com.mysql.cj.jdbc.Driver");
    		 if(connection == null) {
    			 connection = DriverManager.getConnection(URL, USER, PASSWORD); 
    			 System.out.println("Database connection created successfully");
    		 }
    	}catch (Exception e) {
			System.out.println("Exception occurs "+e);
		}
    	return connection;
    }
}
