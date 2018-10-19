package com.excilys.cdb.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * 
 * Connects to the database and executes sql statements to return information or
 * to update the database.
 * 
 * @author Jonasz Leflour
 * @version %I%
 *
 */
public class DatabaseAccessor {
	private static DatabaseAccessor dba = null;
	
	private String URL = "jdbc:mysql://localhost:3306/computer-database-db"
			+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET&useSSL=false";
	private String user = "admincdb";
	private String password = "qwerty1234";

	/**
	 * Tries to connect to database on create
	 */
	private DatabaseAccessor() throws FileNotFoundException,IOException{
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		URL = prop.getProperty("database");
		user = prop.getProperty("user");
		password = prop.getProperty("password");
	}
	
	/**
	 * @return Singleton of DatabaseAccessor 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static DatabaseAccessor GetDatabaseAccessor() throws FileNotFoundException, IOException {
		if(dba == null) {
			dba = new DatabaseAccessor();
		}
		return dba;
	}

	/**
	 * @param user
	 * @param password
	 * @throws SQLException
	 */
	public void setupDatabase(String user, String password) throws SQLException {
		Connection con = DriverManager.getConnection(URL, user, password);
		if (con != null) {
			con.close();
		}
		con = DriverManager.getConnection(URL, user, password);
		this.user = user;
		this.password = password;

		Statement s = con.createStatement();
		s.execute("ALTER TABLE computer " + "ADD CONSTRAINT check_dates check (introduced < discontinued)");
		s.executeUpdate("DELETE FROM computer WHERE name IS NULL");
		s.execute("ALTER TABLE computer MODIFY name VARCHAR(255) NOT NULL");
		s.close();

	}

	private Computer createComputerWithResultSetRow(ResultSet rs) throws EmptyResultSetException {
		Computer computer = new Computer();
		try {
			int id = rs.getInt(1);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			computer.setId(id);
			
			String name = rs.getString(2);
			if (!rs.wasNull()) {
				computer.setName(name);
			}

			Date introduced = rs.getDate(3);
			if (!rs.wasNull()) {
				computer.setIntroduced(introduced.toLocalDate());
			}

			Date discontinued = rs.getDate(4);
			if (!rs.wasNull()) {
				computer.setDiscontinued(discontinued.toLocalDate());
			}

			Integer idCompany = rs.getInt(5);
			if (!rs.wasNull()) {
				computer.setCompany(getCompanybyId(idCompany));
			}
		} catch (SQLException e) {
			new EmptyResultSetException();
		} catch (ObjectNotFoundException e) {
			computer.setCompany(null);
		}
		return computer;
	}

	private Company createCompanyWithResultSetRow(ResultSet rs) throws EmptyResultSetException {
		try {
			int id = rs.getInt(1);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			String name = rs.getString(2);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			return new Company(id, name);
		} catch (SQLException e) {
			throw new EmptyResultSetException();
		}
	}

	/**
	 * @return all computers from the database as a ResultSet
	 * @throws SQLException
	 */
	public List<Computer> getAllComputers() {
		List<Computer> computers = new ArrayList<>();
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.createStatement();
			rs = s.executeQuery("SELECT * FROM computer");

			while (rs.next()) {
				computers.add(this.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmptyResultSetException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return computers;
	}

	/**
	 * @return all companies from the database as a ResultSet
	 * @throws SQLException
	 */
	public List<Company> getAllCompanies() {
		List<Company> companies = new ArrayList<>();
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.createStatement();
			rs = s.executeQuery("SELECT * FROM company");

			while (rs.next()) {
				companies.add(this.createCompanyWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyResultSetException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return companies;
	}

	/**
	 * @param id id of company
	 * @return single row of the company table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 */
	public Company getCompanybyId(int id) throws ObjectNotFoundException {
		Company company = null;
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.createStatement();
			rs = s.executeQuery("SELECT * FROM company WHERE id=" + id);
			rs.next();
			company = this.createCompanyWithResultSetRow(rs);
		} catch (SQLException e) {
			throw new ObjectNotFoundException();
		} catch (EmptyResultSetException e) {
			throw new ObjectNotFoundException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return company;
	}

	/**
	 * @param id
	 * @return single row of the computer table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 */
	public Computer getComputerById(int id) throws ObjectNotFoundException {

		Computer computer = null;
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.createStatement();
			rs = s.executeQuery("SELECT * FROM computer WHERE id=" + id);
			rs.next();
			computer = this.createComputerWithResultSetRow(rs);
		} catch (SQLException e) {
			throw new ObjectNotFoundException();
		} catch (EmptyResultSetException e) {
			throw new ObjectNotFoundException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return computer;
	}

	/**
	 * @param name
	 * @return all rows of the computer table with the specified name, if any
	 */
	public List<Computer> getComputerByName(String name) {
		List<Computer> computers = new ArrayList<>();
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement("SELECT * FROM computer WHERE name=?");
			s.setString(1, name);

			rs = s.executeQuery();

			while (rs.next()) {
				computers.add(this.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmptyResultSetException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return computers;
	}

	/**
	 * @param computer
	 * @throws InvalidParameterException
	 */
	public void createComputer(Computer computer) throws InvalidParameterException {

		Connection con = null;
		PreparedStatement s = null;
		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement(
					"INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)");
			s.setString(1, computer.getName());
			if (computer.getIntroduced() != null) {
				s.setDate(2, Date.valueOf(computer.getIntroduced()));
			} else {
				s.setNull(2, Types.DATE);
			}
			if (computer.getDiscontinued() != null) {
				s.setDate(3, Date.valueOf(computer.getDiscontinued()));
			} else {
				s.setNull(3, Types.DATE);
			}
			if (computer.getCompany() != null) {
				s.setLong(4, computer.getCompany().getId());
			} else {
				s.setNull(4, Types.BIGINT);
			}
			s.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidParameterException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * deletes all computers in table with specified name
	 * 
	 * @param name
	 */
	public void deleteComputerByName(String name) {
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement("DELETE FROM computer WHERE name = ?");
			s.setString(1, name);
			s.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidParameterException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param id
	 */
	public void deleteComputerById(long id) {
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement("DELETE FROM computer WHERE id = ?");
			s.setLong(1, id);
			s.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidParameterException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param computer to update
	 * @throws InvalidParameterException if no updated fields or field "name" is
	 *                                   left blank
	 */
	public void updateComputer(Computer computer) throws InvalidParameterException {
		if (computer.getId() == 0) {
			throw new InvalidParameterException("Invalid Id");
		}

		Connection con = null;
		PreparedStatement s = null;
		try {
			con = DriverManager.getConnection(URL, user, password);
			String sql = "UPDATE computer";
			if(computer.getName() != null) {
				sql+=" SET name=?,";
			}
			
			if(computer.getIntroduced() != null) { 
				sql+= " SET introduced=?,";
			}
			if(computer.getDiscontinued() != null) { 
				sql+= " SET discontinued=?,";
			}
			if(computer.getCompany() != null) { 
				sql+= " SET company_id=?,";
			}
			sql += " WHERE id=?";
			
			
			s = con.prepareStatement(sql);
			int i = 1;
			if(computer.getName() != null) {
				s.setString(i, computer.getName());
				i++;
			}
			
			if(computer.getIntroduced() != null) { 
				s.setDate(i, Date.valueOf(computer.getIntroduced()));
				i++;
			}
			if(computer.getDiscontinued() != null) { 
				s.setDate(i, Date.valueOf(computer.getDiscontinued()));
				i++;
			}
			
			if(computer.getCompany() != null) { 
				s.setLong(i, computer.getCompany().getId());
				i++;
			}
			
			s.setLong(i, computer.getId());
			
			
			s.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidParameterException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
