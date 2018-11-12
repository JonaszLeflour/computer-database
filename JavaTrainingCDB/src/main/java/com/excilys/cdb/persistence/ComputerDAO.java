package com.excilys.cdb.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import com.excilys.cdb.controller.beans.DataConfig;
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
public class ComputerDAO {
	private static ComputerDAO dba = null;
	private  DataSource ds;
	@SuppressWarnings("unused")
	
	private  DataSourceTransactionManager manager;

	/**
	 * @param source
	 */
	public void setSource(DataSource source) {
		this.ds = source;
	}
	
	/**
	 * @param manager
	 */
	public void setManager(DataSource manager) {
		this.ds = manager;
	}
	
	
	/**
	 * @author Jonasz Leflour
	 *
	 */
	public static enum ComputerField {
		@SuppressWarnings("javadoc")
		id, @SuppressWarnings("javadoc")
		name, @SuppressWarnings("javadoc")
		introduced, @SuppressWarnings("javadoc")
		discontinued, @SuppressWarnings("javadoc")
		company_id;
	};





	/**
	 * Tries to connect to database on create
	 * 
	 * @throws ClassNotFoundException
	 */
	private ComputerDAO() {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(DataConfig.class);
		ds = ctx.getBean(DataSource.class);
		manager = ctx.getBean(DataSourceTransactionManager.class);
	}

	/**
	 * @return Singleton of DatabaseAccessor
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DatabaseErrorException
	 * @throws ClassNotFoundException
	 */
	public static ComputerDAO getInstance(){
		if (dba == null) {
			dba = new ComputerDAO();
		}
		return dba;
	}

	/**
	 * @return all computers from the database as a ResultSet
	 * @throws DatabaseErrorException
	 * @throws SQLException
	 */
	public List<Computer> getAllComputers() throws DatabaseErrorException {
		List<Computer> computers = new ArrayList<>();
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			s = con.createStatement();
			rs = s.executeQuery("SELECT id, name, introduced, discontinued, company_id FROM computer");

			while (rs.next()) {
				computers.add(ComputerResultSetMapper.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			computers.clear();
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}

			} catch (SQLException e) {
				throw new DatabaseErrorException(e);
			}
		}
		return computers;
	}

	/**
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getAllComputers(long offset, long lenght) throws DatabaseErrorException {
		List<Computer> computers = new ArrayList<>();
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			s = con.prepareStatement("SELECT id, name, introduced, discontinued, company_id FROM computer LIMIT ?, ?");
			s.setLong(1, offset);
			s.setLong(2, lenght);
			rs = s.executeQuery();

			while (rs.next()) {
				computers.add(ComputerResultSetMapper.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			return computers;
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException(e);
			}
		}
		return computers;
	}

	/**
	 * @param name
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getComputersByName(String name, long offset, long lenght) throws DatabaseErrorException {
		List<Computer> computers = new ArrayList<>();
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			s = con.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id FROM computer AS c WHERE UPPER(c.name) LIKE UPPER(?) LIMIT ?, ?");
			s.setString(1, "%" + name + "%");
			s.setLong(2, offset);
			s.setLong(3, lenght);
			rs = s.executeQuery();

			while (rs.next()) {
				computers.add(ComputerResultSetMapper.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			computers.clear();
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException(e);
			}
		}
		return computers;
	}

	/**
	 * @param id
	 * @return single row of the computer table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public Computer getComputerById(long id) throws ObjectNotFoundException, DatabaseErrorException {

		Computer computer = null;
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		try {// id name intro disc idcomp
			con = ds.getConnection();
			s = con.prepareStatement("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id=?");
			s.setLong(1, id);
			rs = s.executeQuery();
			rs.next();
			computer = ComputerResultSetMapper.createComputerWithResultSetRow(rs);
		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			throw new ObjectNotFoundException("Couldn't find computer with id=" + id);
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
		if (computer == null || computer.getId() == 0 || computer.getName() == null || computer.getName().isEmpty()) {
			throw new ObjectNotFoundException();
		}
		return computer;
	}

	/**
	 * @param name      name searched. Leave empty or null for all computers
	 * @param offset    index of for item in the resulting list
	 * @param lenght    max size of result
	 * @param orderBy
	 * @param direction
	 * @return ordered list of computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getOrderedComputers(String name, long offset, long lenght, ComputerField orderBy,
			OrderDirection direction) throws DatabaseErrorException {
		List<Computer> computers = new ArrayList<>();
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			s = con.prepareStatement("SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id "
					+ "FROM computer AS c " + "WHERE UPPER(c.name) LIKE UPPER(?) " + "ORDER BY c." + orderBy.toString()
					+ " " + direction.toString() + " " + "LIMIT ?, ?");
			if (name != null && !name.isEmpty()) {
				s.setString(1, "%" + name + "%");
			} else {
				s.setString(1, "%");
			}
			s.setLong(2, offset);
			s.setLong(3, lenght);
			rs = s.executeQuery();

			while (rs.next()) {
				computers.add(ComputerResultSetMapper.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			computers.clear();
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException(e);
			}
		}
		return computers;
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
			con = ds.getConnection();
			s = con.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id FROM computer AS c WHERE UPPER(c.name) LIKE UPPER(?)");
			s.setString(1, "%" + name + "%");

			rs = s.executeQuery();

			while (rs.next()) {
				computers.add(ComputerResultSetMapper.createComputerWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			computers.clear();
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
		return computers;
	}

	/**
	 * @param name name pattern of computers to count, or empty/null for all of them
	 * @return number of computers in database
	 * @throws DatabaseErrorException
	 */
	public long countComputersByName(String name) throws DatabaseErrorException {
		Connection con = null;
		PreparedStatement s = null;
		ResultSet res = null;
		try {
			con = ds.getConnection();

			s = con.prepareStatement("SELECT COUNT(c.id) FROM computer AS c WHERE UPPER(c.name) LIKE UPPER(?)");
			if (name != null && !name.isEmpty()) {
				s.setString(1, "%" + name + "%");
			} else {
				s.setString(1, "%");
			}
			res = s.executeQuery();
			res.next();
			return res.getLong(1);
		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (res != null) {
					res.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
	}

	/**
	 * @param name
	 * @return number of companies matching name in database
	 * @throws DatabaseErrorException
	 */
	public long countCompaniesByName(String name) throws DatabaseErrorException {
		Connection con = null;
		PreparedStatement s = null;
		ResultSet res = null;
		try {
			con = ds.getConnection();

			s = con.prepareStatement("SELECT COUNT(c.id) FROM company AS c WHERE UPPER(c.name) LIKE UPPER(?)");
			if (name != null && !name.isEmpty()) {
				s.setString(1, "%" + name + "%");
			} else {
				s.setString(1, "%");
			}
			res = s.executeQuery();
			res.next();
			return res.getLong(1);
		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (s != null) {
					s.close();
				}
				if (res != null) {
					res.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
	}

	/**
	 * @param computer
	 * @throws InvalidParameterException
	 * @throws DatabaseErrorException
	 */
	public void createComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException {
		if (computer == null) {
			throw new InvalidParameterException("Computer is null");

		} else if (computer.getName() == null) {
			throw new InvalidParameterException("Computer name is null");

		} else if (computer.getName().equals("")) {
			throw new InvalidParameterException("Computer name is empty");
		}
		if ((computer.getIntroduced() != null && computer.getDiscontinued() != null)
				&& computer.getIntroduced().isAfter(computer.getDiscontinued())) {
			throw new InvalidParameterException("Incoherent introduced and discontinued dates");
		}
		Connection con = null;
		PreparedStatement s = null;
		Savepoint beforeUpdate = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			beforeUpdate = con.setSavepoint();
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
			if (beforeUpdate != null) {
				try {
					con.rollback(beforeUpdate);
				} catch (SQLException e1) {
					throw new DatabaseErrorException(e1);
				}
			}
			throw new DatabaseErrorException(e);
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException(e);
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
	public void deleteComputerByName(String name)
			throws InvalidParameterException, ObjectNotFoundException, DatabaseErrorException {
		if (name == null) {
			throw new InvalidParameterException("Name provided is null");
		} else if (name.isEmpty()) {
			throw new InvalidParameterException("Name provided is empty");
		}

		Connection con = null;
		PreparedStatement s = null;
		int ret = 0;
		Savepoint beforeDelete = null;
		try {
			con = ds.getConnection();
			beforeDelete = con.setSavepoint();
			con.setAutoCommit(false);
			s = con.prepareStatement("DELETE FROM computer WHERE name = ?");
			s.setString(1, name);
			ret = s.executeUpdate();
		} catch (SQLException e) {
			if (beforeDelete != null) {
				try {
					con.rollback(beforeDelete);
				} catch (SQLException e1) {
					throw new DatabaseErrorException(e1);
				}
			}
			throw new DatabaseErrorException();
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
		if (ret == 0) {
			throw new ObjectNotFoundException();
		}

	}

	/**
	 * deletes all computers in table with specified name
	 * 
	 * @param id
	 * @throws InvalidParameterException
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public void deleteComputerById(long id)
			throws InvalidParameterException, ObjectNotFoundException, DatabaseErrorException {
		if (id <= 0) {
			throw new InvalidParameterException("Id provided must be strictly positive");
		}

		Connection con = null;
		PreparedStatement s = null;
		int ret = 0;
		Savepoint beforeDelete = null;
		try {
			con = ds.getConnection();
			beforeDelete = con.setSavepoint();
			con.setAutoCommit(false);
			s = con.prepareStatement("DELETE FROM computer WHERE id = ?");
			s.setLong(1, id);
			ret = s.executeUpdate();
		} catch (SQLException e) {
			if (beforeDelete != null) {
				try {
					con.rollback(beforeDelete);
				} catch (SQLException e1) {
					throw new DatabaseErrorException(e1);
				}
			}
			throw new DatabaseErrorException();
		} finally {
			try {
				if (con != null) {
					con.commit();
					con.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
		}
		if (ret == 0) {
			throw new ObjectNotFoundException();
		}

	}

	/**
	 * @param computer to update
	 * @throws ObjectNotFoundException
	 * @throws InvalidParameterException if no updated fields or field "name" is
	 *                                   left blank
	 * @throws DatabaseErrorException
	 */
	public void updateComputer(Computer computer)
			throws InvalidParameterException, ObjectNotFoundException, DatabaseErrorException {
		if (computer == null) {
			throw new InvalidParameterException("Computer object is null");
		} else if (computer.getName() != null && computer.getName().isEmpty()) {
			throw new InvalidParameterException("Computer name cannot be empty");
		}

		if (computer.getId() == 0) {
			throw new InvalidParameterException("Computer id isn't provided");
		}
		Computer oldComputer = null;
		oldComputer = getComputerById(computer.getId());
		LocalDate newIntroduced = computer.getIntroduced();
		LocalDate newDiscontinued = computer.getDiscontinued();
		if (newIntroduced == null) {
			newIntroduced = oldComputer.getIntroduced();
		}
		if (newDiscontinued == null) {
			newDiscontinued = oldComputer.getDiscontinued();
		}
		if (newIntroduced != null && newDiscontinued != null) {
			if (newIntroduced.isAfter(newDiscontinued)) {
				throw new InvalidParameterException("Invalid dates");
			}
		}

		Connection con = null;
		PreparedStatement s = null;
		boolean hasParameters = false;
		Savepoint beforeUpdate = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			beforeUpdate = con.setSavepoint();
			String sql = "UPDATE computer SET";
			if (computer.getName() != null) {
				sql += "  name=? ";
				hasParameters = true;
			}
			if (computer.getIntroduced() != null) {
				if (hasParameters) {
					sql += ",";
				}
				sql += " introduced=?";
				hasParameters = true;

			}
			if (computer.getDiscontinued() != null) {
				if (hasParameters) {
					sql += ",";
				}
				sql += " discontinued=?";
				hasParameters = true;
			}
			if (computer.getCompany() != null) {
				if (hasParameters) {
					sql += ",";
				}
				sql += " company_id=?";
				hasParameters = true;
			}

			if (!hasParameters) {
				con.close();
				throw new InvalidParameterException("No updated parameters provided");
			}
			sql += " WHERE id=?";

			s = con.prepareStatement(sql);
			int i = 1;
			if (computer.getName() != null) {
				s.setString(i, computer.getName());
				i++;
			}

			if (computer.getIntroduced() != null) {
				s.setDate(i, Date.valueOf(computer.getIntroduced()));
				i++;
			}
			if (computer.getDiscontinued() != null) {
				s.setDate(i, Date.valueOf(computer.getDiscontinued()));
				i++;
			}

			if (computer.getCompany() != null) {
				s.setLong(i, computer.getCompany().getId());
				i++;
			}

			s.setLong(i, computer.getId());

			s.executeUpdate();
		} catch (SQLException e) {
			if (beforeUpdate != null) {
				try {
					con.rollback(beforeUpdate);
					con.close();
				} catch (SQLException e1) {
					throw new DatabaseErrorException(e1);
				}
			}
			throw new DatabaseErrorException();
		} finally {
			try {
				if (con != null) {
					con.commit();
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