package com.excilys.cdb.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.controller.beans.DataConfig;
import com.excilys.cdb.model.Company;


/**
 * @author Jonasz Leflour
 *
 */
public class CompanyDAO {
	private static CompanyDAO dba = null;
	private  DataSource ds;

	/**
	 * @author Jonasz Leflour
	 *
	 */
	public static enum CompanyField {
		@SuppressWarnings("javadoc")
		id, @SuppressWarnings("javadoc")
		name;
	}
	
	
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
	 * Tries to connect to database on create
	 * 
	 * @throws ClassNotFoundException
	 */
	private CompanyDAO() {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(DataConfig.class);
		ds = ctx.getBean(DataSource.class);
	}

	/**
	 * @return Singleton of DatabaseAccessor
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DatabaseErrorException
	 * @throws ClassNotFoundException
	 */
	public static CompanyDAO getInstance(){
		if (dba == null) {
			dba = new CompanyDAO();
		}
		return dba;
	}
	
	
	/**
	 * @param name
	 * @param offset
	 * @param lenght
	 * @param orderBy
	 * @param direction
	 * @return ordered list of companies
	 * @throws DatabaseErrorException
	 */
	public List<Company> getOrderedCompanies(String name, long offset, long lenght, CompanyDAO.CompanyField orderBy,
			OrderDirection direction) throws DatabaseErrorException {
		List<Company> companies = new ArrayList<>();
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			s = con.prepareStatement(
					"SELECT c.id, c.name " + "FROM company AS c " + "WHERE UPPER(c.name) LIKE UPPER(?) " + "ORDER BY c."
							+ orderBy.toString() + " " + direction.toString() + " " + "LIMIT ?, ?");
			if (name != null && !name.isEmpty()) {
				s.setString(1, "%" + name + "%");
			} else {
				s.setString(1, "%");
			}
			s.setLong(2, offset);
			s.setLong(3, lenght);
			rs = s.executeQuery();

			while (rs.next()) {
				companies.add(CompanyResultSetMapper.createCompanyWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException(e);
		} catch (EmptyResultSetException e) {
			companies.clear();
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
		return companies;
	}
	
	/**
	 * @return all companies from the database as a ResultSet
	 * @throws DatabaseErrorException
	 * @throws SQLException
	 */
	public List<Company> getAllCompanies() throws DatabaseErrorException {
		List<Company> companies = new ArrayList<>();
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			s = con.createStatement();
			rs = s.executeQuery("SELECT id, name FROM company");

			while (rs.next()) {
				companies.add(CompanyResultSetMapper.createCompanyWithResultSetRow(rs));
			}

		} catch (SQLException e) {
			throw new DatabaseErrorException();
		} catch (EmptyResultSetException e) {
			companies.clear();
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
				throw new DatabaseErrorException();
			}
		}
		return companies;
	}
	
	/**
	 * @param id id of company
	 * @return single row of the company table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public Company getCompanybyId(long id) throws ObjectNotFoundException, DatabaseErrorException {
		Company company = null;
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			s = con.createStatement();
			rs = s.executeQuery("SELECT id, name FROM company WHERE id=" + id);
			rs.next();
			company = CompanyResultSetMapper.createCompanyWithResultSetRow(rs);
		} catch (SQLException e) {
			throw new ObjectNotFoundException();
		} catch (EmptyResultSetException e) {
			throw new ObjectNotFoundException();
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
		return company;
	}
	
	/**
	 * @param id
	 * @throws DatabaseErrorException
	 * @throws ObjectNotFoundException
	 */
	public void deleteCompanyById(long id) throws DatabaseErrorException, ObjectNotFoundException {
		int status1 = 0, status2 = 0;
		Connection con = null;
		PreparedStatement s1 = null, s2 = null;
		Savepoint beforeDelete = null;
		try {
			con = ds.getConnection();
			beforeDelete = con.setSavepoint();
			con.setAutoCommit(false);
			s1 = con.prepareStatement("DELETE FROM computer WHERE company_id = ?");
			s1.setLong(1, id);
			status1 = s1.executeUpdate();

			s2 = con.prepareStatement("DELETE FROM company WHERE id = ?");
			s2.setLong(1, id);
			status2 = s2.executeUpdate();

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
				if (s1 != null) {
					s1.close();
				}
				if (s2 != null) {
					s2.close();
				}
			} catch (SQLException e) {
				throw new DatabaseErrorException();
			}
			if (status1 == 0 || status2 == 0) {
				throw new ObjectNotFoundException();
			}
		}

	}
}
