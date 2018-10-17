package persistence;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseAccessor {
	private String URL = "jdbc:mysql://localhost:3306/computer-database-db"
			+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET&useSSL=false";
	private String user = "admincdb";
	private String password = "qwerty1234";

	private Connection con = null;

	public static enum ComputerField {
		name, introduced, discontinued, company_id
	}

	public static enum CompanyField {
		name
	}

	public DatabaseAccessor() {
		try {
			reconnect(user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void reconnect(String user, String password) throws SQLException {
		if (con != null) {
			con.close();
		}
		con = DriverManager.getConnection(URL, user, password);
		this.user = user;
		this.password = password;
		
		Statement s = con.createStatement();
		s.execute("ALTER TABLE computer "
				+ "ADD CONSTRAINT check_dates check (introduced < discontinued)");
		s.executeUpdate("DELETE FROM computer WHERE name IS NULL");
		s.execute("ALTER TABLE computer MODIFY name VARCHAR(255) NOT NULL");
		
	}

	public ResultSet executeQuery(String SQL) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			return rs;
		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void executeUpdate(String SQL) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			System.err.println("Error : invalid request");
			e.printStackTrace();
		}
	}

	public ResultSet getAllComputers() {
		return executeQuery("SELECT * FROM computer");
	}

	public ResultSet getAllCompanies() {
		return executeQuery("SELECT * FROM company");
	}

	public ResultSet getCompanybyId(int id) {
		return executeQuery("SELECT * FROM company WHERE id=" + id);
	}

	public ResultSet getComputerById(int id) {
		return executeQuery("SELECT * FROM computer WHERE id=" + id);
	}

	public ResultSet getComputerByName(String name) {
		return executeQuery("SELECT * FROM computer WHERE name='" + name + "'");
	}

	public void createComputer(Map<ComputerField, String> columns) throws InvalidParameterException{
		if(columns.isEmpty()) {
			throw new InvalidParameterException("No updated fields");
		}
		if(columns.get(ComputerField.name)!=null && columns.get(ComputerField.name).isEmpty()) {
			throw new InvalidParameterException("Name is empty");
		}
		
		
		StringBuffer requestBuf = new StringBuffer();
		StringBuffer requestBuf_p2 = new StringBuffer();
		requestBuf.append("INSERT INTO computer ");
		requestBuf_p2.append(" VALUES ");

		boolean firstField = true;
		for (Map.Entry<ComputerField, String> entry : columns.entrySet()) {
			if (!firstField) {
				requestBuf.append(",");
				requestBuf_p2.append(",");
			} else {
				requestBuf.append("(");
				requestBuf_p2.append("(");
			}
			requestBuf.append(entry.getKey());
			// cast datetime if field is date like so : CAST('YYYY-MM-DD' AS DATETIME)
			if (entry.getKey().equals(ComputerField.introduced) || entry.getKey().equals(ComputerField.discontinued)) {
				requestBuf_p2.append("CAST('").append(entry.getValue()).append("' AS DATETIME)");
			} else if (entry.getKey().equals(ComputerField.name)) {
				requestBuf_p2.append("'").append(entry.getValue()).append("'");
			} else {
				requestBuf_p2.append(entry.getValue());
			}

			firstField = false;
		}
		if (!firstField) {
			requestBuf.append(")");
			requestBuf_p2.append(")");
		}
		requestBuf.append(requestBuf_p2);
		System.out.println(requestBuf.toString());
		executeUpdate(requestBuf.toString());
	}

	public void deleteComputerByName(String name) {
		executeUpdate("DELETE FROM computer WHERE name = '" + name + "'");
	}
	
	public void deleteComputerById(int id) {
		executeUpdate("DELETE FROM computer WHERE id ="+id);
	}

	public void updateComputerById(int id, Map<ComputerField, String> updatedColumns) throws InvalidParameterException{
		if(updatedColumns.isEmpty()) {
			throw new InvalidParameterException("No updated fields");
		}
		if(updatedColumns.get(ComputerField.name)!=null && updatedColumns.get(ComputerField.name).isEmpty()) {
			throw new InvalidParameterException("Name is empty");
		}
		
		StringBuffer request = new StringBuffer();
		request.append("UPDATE computer SET ");

		boolean firstField = true;
		for (Map.Entry<ComputerField, String> entry : updatedColumns.entrySet()) {
			if (!firstField) {
				request.append(", ");
			}
			request.append(entry.getKey()).append("=");

			// cast datetime if field is date like so : CAST('YYYY-MM-DD' AS DATETIME)
			if (entry.getKey().equals(ComputerField.introduced) || entry.getKey().equals(ComputerField.discontinued)) {
				request.append("CAST('").append(entry.getValue()).append("' AS DATETIME)");
			} else if (entry.getKey().equals(ComputerField.name)) {
				request.append("'").append(entry.getValue()).append("'");
			} else {
				request.append(entry.getValue());
			}
			firstField = false;
		}
		request.append(" WHERE id = " + id);
		executeUpdate(request.toString());
	}

}
