package com.excilys.cdb.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
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
	private static String propertiesURL = "config.properties";
	
	private String URL = null;
	private String user = null;
	private String password = null;
	
	/**
	 * Tries to connect to database on create
	 * @throws ClassNotFoundException 
	 */
	private DatabaseAccessor() throws FileNotFoundException,IOException, DatabaseErrorException, ClassNotFoundException{
		Properties prop = new Properties();
		Class.forName("com.mysql.cj.jdbc.Driver");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(propertiesURL);
		if(input == null) {
			throw new FileNotFoundException();
		}
		prop.load(input);
		URL = prop.getProperty("database");
		user = prop.getProperty("user");
		password = prop.getProperty("password");
	}
	
	/**
	 * @return Singleton of DatabaseAccessor 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws DatabaseErrorException 
	 * @throws ClassNotFoundException 
	 */
	public static DatabaseAccessor GetDatabaseAccessor() throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
		if(dba == null) {
			dba = new DatabaseAccessor();
		}
		return dba;
	}

	private Computer createComputerWithResultSetRow(ResultSet rs) throws EmptyResultSetException {
		Computer computer = new Computer();
		try {
			long id = rs.getLong(1);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			computer.setId(id);
			
			String name = rs.getString(2);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			computer.setName(name);

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
	 * @throws DatabaseErrorException 
	 * @throws SQLException
	 */
	public List<Computer> getAllComputers() throws DatabaseErrorException{
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
			throw new DatabaseErrorException("Error occured during data query");
		} catch (EmptyResultSetException e) {
			return computers;
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
	 * @throws DatabaseErrorException 
	 * @throws SQLException
	 */
	public List<Company> getAllCompanies() throws DatabaseErrorException{
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
			throw new DatabaseErrorException();
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
				throw new DatabaseErrorException();
			}
		}
		return companies;
	}

	/**
	 * @param id id of company
	 * @return single row of the company table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 */
	public Company getCompanybyId(long id) throws ObjectNotFoundException {
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
	 * @throws DatabaseErrorException 
	 */
	public Computer getComputerById(long id) throws ObjectNotFoundException, DatabaseErrorException{

		Computer computer = null;
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement("SELECT * FROM computer WHERE id=?");
			s.setLong(1, id);
			rs = s.executeQuery();
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
				throw new DatabaseErrorException();
			}
		}
		if(computer == null || computer.getId() == 0 || computer.getName() == null ||  computer.getName().isEmpty()) {
			throw new ObjectNotFoundException();
		}
		return computer;
	}

	/**
	 * @param name
	 * @return all rows of the computer table with the specified name, if any
	 * @throws DatabaseErrorException 
	 */
	public List<Computer> getComputerByName(String name) throws DatabaseErrorException {
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

		} catch (SQLException | EmptyResultSetException e) {
			throw new DatabaseErrorException(); 
		}finally {
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
				throw new DatabaseErrorException();
			}
		}
		return computers;
	}

	/**
	 * @param computer
	 * @throws InvalidParameterException 
	 * @throws DatabaseErrorException 
	 */
	public void createComputer(Computer computer) throws InvalidParameterException,DatabaseErrorException {
		if(computer == null) {
			throw new InvalidParameterException("Computer is null");
			
		}else if(computer.getName()== null) {
			throw new InvalidParameterException("Computer name is null");
			
		}else if(computer.getName().equals("")) {
			throw new InvalidParameterException("Computer name is empty");
		}
		if((computer.getIntroduced() != null && computer.getDiscontinued() != null )
				&& computer.getIntroduced().isAfter(computer.getDiscontinued())) {
			throw new InvalidParameterException("Incoherent introduced and discontinued dates");
		}
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
			throw new DatabaseErrorException(e.toString());
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
	}

	/**
	 * deletes all computers in table with specified name
	 * 
	 * @param name
	 * @throws InvalidParameterException 
	 * @throws ObjectNotFoundException 
	 * @throws DatabaseErrorException 
	 */
	public void deleteComputerByName(String name) throws InvalidParameterException, ObjectNotFoundException, DatabaseErrorException{
		if(name == null) {
			throw new InvalidParameterException("Name provided is null");
		}else if (name.isEmpty()){
			throw new InvalidParameterException("Name provided is empty");
		}
		
		Connection con = null;
		PreparedStatement s = null;
		int ret = 0;
		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement("DELETE FROM computer WHERE name = ?");
			s.setString(1, name);
			ret = s.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseErrorException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
		if(ret == 0) {
			throw new ObjectNotFoundException();
		}

	}

	/**
	 * @param id
	 * @throws ObjectNotFoundException 
	 * @throws DatabaseErrorException 
	 */
	public void deleteComputerById(long id) throws DatabaseErrorException, ObjectNotFoundException{
		Connection con = null;
		PreparedStatement s = null;
		int status = 0;
		try {
			con = DriverManager.getConnection(URL, user, password);
			s = con.prepareStatement("DELETE FROM computer WHERE id = ?");
			s.setLong(1, id);
			status = s.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseErrorException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
			if(status == 0) {
				throw new ObjectNotFoundException();
			}
		}
	}

	/**
	 * @param computer to update
	 * @throws InvalidParameterException if no updated fields or field "name" is
	 *                                   left blank
	 * @throws DatabaseErrorException 
	 */
	public void updateComputer(Computer computer) throws InvalidParameterException,DatabaseErrorException {
		if (computer == null) {
			throw new InvalidParameterException("Computer object is null");
		}else if(computer.getName()!=null && computer.getName().isEmpty()) {
			throw new InvalidParameterException("Computer name cannot be empty");
		}
		
		if(computer.getId() == 0){
			throw new InvalidParameterException("Computer id isn't provided");
		}
		Computer oldComputer = null;
		try {
			oldComputer = getComputerById(computer.getId());
		} catch (ObjectNotFoundException e1) {
			throw new InvalidParameterException("Computer id doesn't exist in database");
		}
		LocalDate newIntroduced = computer.getIntroduced();
		LocalDate newDiscontinued = computer.getDiscontinued();
		if(newIntroduced == null) {
			newIntroduced = oldComputer.getIntroduced();
		}
		if(newDiscontinued == null) {
			newDiscontinued = oldComputer.getDiscontinued();
		}
		if(newIntroduced != null && newDiscontinued != null) {
			if(newIntroduced.isAfter(newDiscontinued)) {
				throw new InvalidParameterException("Invalid dates");
			}
		}
		

		Connection con = null;
		PreparedStatement s = null;
		boolean hasParameters = false;
		try {
			con = DriverManager.getConnection(URL, user, password);
			
			String sql = "UPDATE computer SET";
			if(computer.getName() != null) {
				sql+="  name=? ";
				hasParameters = true;
			}	
			if(computer.getIntroduced() != null) {
				if(hasParameters) {
					sql+=",";
				}
				sql+= " introduced=?";
				hasParameters = true;
				
			}
			if(computer.getDiscontinued() != null) { 
				if(hasParameters) {
					sql+=",";
				}
				sql+= " discontinued=?";
				hasParameters = true;
			}
			if(computer.getCompany() != null) { 
				if(hasParameters) {
					sql+=",";
				}
				sql+= " company_id=?";
				hasParameters = true;
			}
			
			if(!hasParameters) {
				con.close();
				throw new InvalidParameterException("No updated parameters provided");
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
			throw new DatabaseErrorException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
	}

}
