package me.lagbug.xprotect.global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MySQL {

	private static String host, database, username, password, statement;
	private static List<String> tables;
	private static int port;
	private static Connection connection;

	public static void initiate(String host, String database, String table, String username, String password, String statement, int port) {
		initiate(host, database, Arrays.asList(table), username, password, statement, port);
	}
	
	public static void initiate(String hostNew, String databaseNew, List<String> tablesNew, String usernameNew, String passwordNew, String statementNew, int portNew) {
		host = hostNew;
		database = databaseNew;
		tables = tablesNew;
		username = usernameNew;
		password = passwordNew;
		statement = statementNew;
		port = portNew;
	}

	public static boolean connect() {
		System.out.println("Attempting to connect to the MySQL database");

		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + statement,
					username, password);

			System.out.println("Successfully connected to the MySQL database");
			for (String table : tables) {
				prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (player_uuid VARCHAR(36), player_name TEXT, player_ip TEXT, date TEXT, PRIMARY KEY(player_uuid))").execute();	
			}
			return true;
		} catch (SQLException ex) {
			System.out.println("Could not connect to the MySQL database. Stacktrace is below");
			ex.printStackTrace();
			return false;
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}

	public static PreparedStatement prepareStatement(String query) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query);
			ps.setQueryTimeout(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	public static ResultSet executeQuery(String query) {
		ResultSet result = null;
		try {
			result = connection.createStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int executeUpdate(String query) {
		int result = 0;
		try {
			result = connection.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}