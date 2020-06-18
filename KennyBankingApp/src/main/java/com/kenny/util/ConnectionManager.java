package com.kenny.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static Connection conn = null;
	
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orclpdb";
	private static final String DB_USER = "kenny";
	private static final String DB_PASS = "oracle";
	
	
	private ConnectionManager() {
		super();
	}
	
	public static Connection getConnection() {
		try {
			Class.forName(DRIVER);
			
			try {
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			} catch(SQLException e) {
				e.printStackTrace();
			} 
		} catch(ClassNotFoundException e) {
			System.out.println("Unable to find Oracle JDBC Driver Class.");
		}
		
		return conn;
	}
}
