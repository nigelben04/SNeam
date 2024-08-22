package main.com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String HOST = "localhost:3306";
	private final String DATABASE = "sneam";
	private final String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection connection;
	private Statement statement;
	private ResultSet res;
	
	public Connect() {
        try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	public ResultSet runQuery(String query) throws Exception {
			res = statement.executeQuery(query);
		return res;
	}
	
	public void runUpdate(String query) throws Exception {
			statement.executeUpdate(query);
	}
	
	public void close() throws Exception {
        if (res != null) res.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }
}
