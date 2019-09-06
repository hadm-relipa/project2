package src;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBUtils {
	String user, url, password;
	Properties p = new Properties();
	Connection connection = null;
	Statement statement;
	PreparedStatement query;

	public boolean init() {
		try {    
			p.load(new FileInputStream("database.properties"));
			user = p.getProperty("user");
			url = p.getProperty("url");
			password = p.getProperty("password");

			// lay DriverManagement
			Class.forName("com.mysql.jdbc.Driver");

			// tao ket noi
			connection = (Connection) DriverManager.getConnection(url, user, password);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (connection == null) {
			throw new NullPointerException();
		}

		return true;
	}

	public void createStatement() {
		if (statement == null) {
			try {
				statement = (Statement) connection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ResultSet retrieveData(String sql) {
		try {
			createStatement();
			ResultSet result = statement.executeQuery(sql);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public int executeUpdate(String sql, String[] values) throws SQLException {
		int result = 0;

		query = connection.prepareStatement(sql);

		for (int i = 0; i < values.length; i++) {
			query.setString(i + 1, values[i]);
		}
		result = query.executeUpdate();

		if (result == 0) {
			throw new SQLException();
		}
		return result;

	}

	public boolean isExist(String table, String column, String value) {
		boolean result = false;
		try {
			query = connection
					.prepareStatement("SELECT * FROM " + table + " WHERE " + column + " LIKE '%" + value + "%'");
			result = query.executeQuery().next();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public boolean isIdentify(String table, String column, String value) {
		boolean result = false;
		try {
			query = connection
					.prepareStatement("SELECT * FROM " + table + " WHERE " + column + " = " + value);
			result = query.executeQuery().next();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public boolean isUnique(String table, String column, String value) {
		int count = 0;
		try {
			ResultSet result = retrieveData("SELECT * FROM " + table + " WHERE " + column + " = " + value);
			while (result.next()) {
				count++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return (count == 1) ? true : false;
	}

	public void selectCol(String table, String col1, String col2, String col3) {
		ResultSet result = retrieveData("SELECT * FROM " + table);
		try {
			while (result.next()) {
				System.out.printf("%-10s%-50s%s\n", result.getString(col1), result.getString(col2),
						result.getString(col3));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void selectCol(String table, String col1, String col2, String col3, String col4) {
		ResultSet result = retrieveData("SELECT * FROM " + table);
		try {
			while (result.next()) {
				System.out.printf("%-10s%-30s%-15s%s\n", result.getString(col1), result.getString(col2),
						result.getString(col3), result.getString(col4));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void selectColLike(String table, String field, String value, String col1, String col2, String col3) {
		ResultSet result = retrieveData("SELECT * FROM " + table + " WHERE " + field + " LIKE '%" + value + "%'");
		try {
			while (result.next()) {
				System.out.printf("%-10s%-50s%s\n", result.getString(col1), result.getString(col2),
						result.getString(col3));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void selectColLike(String table, String field, String value, String col1, String col2, String col3,
			String col4) {
		ResultSet result = retrieveData("SELECT * FROM " + table + " WHERE " + field + " LIKE '%" + value + "%'");
		try {
			while (result.next()) {
				System.out.printf("%-10s%-30s%-15s%s\n", result.getString(col1), result.getString(col2),
						result.getString(col3), result.getString(col4));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void innerJoinAndSelect(String table1, String table2, String field1, String field2, String col1, String col2,
			String col3, String col4) {
		ResultSet result = retrieveData(
				"SELECT * FROM " + table1 + " INNER JOIN " + table2 + " ON " + field1 + " = " + field2);
		try {
			while (result.next()) {
				System.out.printf("%-10s%-30s%-15s%s\n", result.getString(col1), result.getString(col2),
						result.getString(col3), result.getString(col4));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
