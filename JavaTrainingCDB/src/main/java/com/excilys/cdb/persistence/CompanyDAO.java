package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.excilys.cdb.model.Company;

/**
 * @author Jonasz Leflour
 *
 */
@Repository
public class CompanyDAO {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	CompanyResultSetMapper companyResultSetMapper;
	
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT c.id, c.name " + "FROM company AS c " + "WHERE UPPER(c.name) LIKE UPPER(?) "
				+ "ORDER BY c." + orderBy.toString() + " " + direction.toString() + " " + "LIMIT ?, ?";
		Object[] params = { new SqlParameterValue(Types.VARCHAR, "%" + name + "%"),
				new SqlParameterValue(Types.BIGINT, offset), new SqlParameterValue(Types.BIGINT, lenght) };
		try {
			companies = jdbcTemplate.query(sql, params, companyResultSetMapper.getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			companies.clear();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT id, name FROM company";
		Object[] params = {};
		try {
			companies = jdbcTemplate.query(sql, params, companyResultSetMapper.getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			companies.clear();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT id, name FROM company WHERE id=?";
		Object[] params = { new SqlParameterValue(Types.BIGINT, id) };
		try {
			company = jdbcTemplate.queryForObject(sql, params, companyResultSetMapper.getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
		return company;
	}

	/**
	 * @param id
	 * @throws DatabaseErrorException
	 * @throws ObjectNotFoundException
	 */
	public void deleteCompanyById(long id) throws DatabaseErrorException, ObjectNotFoundException {
		getCompanybyId(id);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] params = { new SqlParameterValue(Types.BIGINT, id) };
		PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus beforeDelete = platformTransactionManager.getTransaction(paramTransactionDefinition);
		TransactionStatus afterDelete = null;

		String sql1 = "DELETE FROM computer WHERE company_id = ?";
		String sql2 = "DELETE FROM company WHERE id = ?";

		try {
			jdbcTemplate.update(sql1, params);
			jdbcTemplate.update(sql2, params);
			afterDelete = platformTransactionManager.getTransaction(paramTransactionDefinition);
		} catch (Exception e) {
			platformTransactionManager.rollback(beforeDelete);
			throw new DatabaseErrorException(e);
		}
		platformTransactionManager.commit(afterDelete);
	}

	/**
	 * @param name
	 * @return number of companies matching name in database
	 * @throws DatabaseErrorException
	 */
	public long countCompaniesByName(String name) throws DatabaseErrorException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(c.id) FROM company AS c WHERE UPPER(c.name) LIKE UPPER(?)";
		Object[] params = { new SqlParameterValue(Types.VARCHAR, "%" + name + "%") };
		long result;
		try {
			result = jdbcTemplate.queryForObject(sql, params, Long.class);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
		return result;
	}
}
