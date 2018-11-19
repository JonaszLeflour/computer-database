package com.excilys.cdb.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
@Repository
public class ComputerDAO {
	private static ComputerDAO dba = null;
	@SuppressWarnings("javadoc")
	@Autowired
	public DataSource dataSource;

	@Autowired
	ComputerResultSetMapper computerResultSetMapper;

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

	/**
	 * @return Singleton of DatabaseAccessor
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DatabaseErrorException
	 * @throws ClassNotFoundException
	 */
	public static ComputerDAO getInstance() {
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
		return this.getComputerByName("");
	}

	/**
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getAllComputers(long offset, long lenght) throws DatabaseErrorException {
		return getComputersByName("", offset, lenght);
	}

	/**
	 * @param name
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getComputersByName(String name, long offset, long lenght) throws DatabaseErrorException {
		return getOrderedComputers(name, offset, lenght, ComputerField.id, OrderDirection.DESC);
	}

	/**
	 * @param id
	 * @return single row of the computer table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public Computer getComputerById(long id) throws ObjectNotFoundException, DatabaseErrorException {

		String sql = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id=?";
		Computer computer = null;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] params = { new SqlParameterValue(Types.BIGINT, id) };
		try {
			computer = jdbcTemplate.queryForObject(sql, params, computerResultSetMapper.getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
		if (computer == null) {
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
		List<Computer> computers;
		String sql = "SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id " + "FROM computer AS c "
				+ "WHERE UPPER(c.name) LIKE UPPER(?) " + "ORDER BY c." + orderBy.toString() + " " + direction.toString()
				+ " " + "LIMIT ?, ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object[] params = {
				new SqlParameterValue(Types.VARCHAR, name != null && !name.isEmpty() ? "%" + name + "%" : "%"),
				new SqlParameterValue(Types.BIGINT, offset), new SqlParameterValue(Types.BIGINT, lenght) };
		try {
			computers = jdbcTemplate.query(sql, params, computerResultSetMapper.getRowMapper());
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
		return computers;
	}

	/**
	 * @param name
	 * @return all rows of the computer table with the specified name, if any
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getComputerByName(String name) throws DatabaseErrorException {
		List<Computer> computers;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id FROM computer AS c WHERE UPPER(c.name) LIKE UPPER(?)";
		Object[] params = { new SqlParameterValue(Types.VARCHAR, "%" + name + "%") };
		try {
			computers = jdbcTemplate.query(sql, params, computerResultSetMapper.getRowMapper());
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
		return computers;
	}

	/**
	 * @param name name pattern of computers to count, or empty/null for all of them
	 * @return number of computers in database
	 * @throws DatabaseErrorException
	 */
	public long countComputersByName(String name) throws DatabaseErrorException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(c.id) FROM computer AS c WHERE UPPER(c.name) LIKE UPPER(?)";
		Object[] params = { new SqlParameterValue(Types.VARCHAR, "%" + name + "%") };
		long result;
		try {
			result = jdbcTemplate.queryForObject(sql, params, Long.class);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
		return result;
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

		String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		Object[] params = { new SqlParameterValue(Types.VARCHAR, computer.getName()),
				new SqlParameterValue(computer.getIntroduced() != null ? Types.DATE : Types.NULL,
						computer.getIntroduced()),
				new SqlParameterValue(computer.getDiscontinued() != null ? Types.DATE : Types.NULL,
						computer.getDiscontinued()),
				computer.getCompany() != null ? new SqlParameterValue(Types.BIGINT, computer.getCompany().getId())
						: new SqlParameterValue(Types.NULL, null) };

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		TransactionStatus afterDelete = null;
		try {
			jdbcTemplate.update(sql, params);
			afterDelete = platformTransactionManager.getTransaction(paramTransactionDefinition);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			throw new DatabaseErrorException(e);
		}
		platformTransactionManager.commit(afterDelete);
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
		}
		if (name.isEmpty()) {
			throw new InvalidParameterException("Name provided is empty");
		}
		if (getComputerByName(name).isEmpty()) {
			throw new ObjectNotFoundException("No computer named " + name);
		}

		PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		TransactionStatus afterDelete = null;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "DELETE FROM computer WHERE name = ?";
		Object[] params = { new SqlParameterValue(Types.VARCHAR, "%" + name + "%") };
		try {
			jdbcTemplate.update(sql, params);
			afterDelete = platformTransactionManager.getTransaction(paramTransactionDefinition);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			throw new DatabaseErrorException(e);
		}

		platformTransactionManager.commit(afterDelete);

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

		PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		TransactionStatus afterDelete = null;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String sql = "DELETE FROM computer WHERE id = ?";
		Object[] params = { new SqlParameterValue(Types.BIGINT, id) };
		try {
			jdbcTemplate.update(sql, params);
			afterDelete = platformTransactionManager.getTransaction(paramTransactionDefinition);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			throw new DatabaseErrorException(e);
		}

		platformTransactionManager.commit(afterDelete);

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

		// !\\ TODO: move to validator class
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

		PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		TransactionStatus afterUpdate = null;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Object> params = new ArrayList<Object>();
		String sql = "UPDATE computer SET";

		if (computer.getName() != null) {
			sql += "  name=? ";
			params.add(new SqlParameterValue(Types.VARCHAR, computer.getName()));
		}
		if (computer.getIntroduced() != null) {
			if (!params.isEmpty()) {
				sql += ",";
			}
			sql += " introduced=?";
			params.add(new SqlParameterValue(Types.DATE, computer.getIntroduced()));

		}
		if (computer.getDiscontinued() != null) {
			if (!params.isEmpty()) {
				sql += ",";
			}
			sql += " discontinued=?";
			params.add(new SqlParameterValue(Types.DATE, computer.getDiscontinued()));
		}
		if (computer.getCompany() != null) {
			if (!params.isEmpty()) {
				sql += ",";
			}
			sql += " company_id=?";
			params.add(new SqlParameterValue(Types.BIGINT, computer.getCompany().getId()));
		}

		if (params.isEmpty()) {
			throw new InvalidParameterException("No updated parameters provided");
		}
		sql += " WHERE id=?";
		params.add(new SqlParameterValue(Types.BIGINT, computer.getId()));

		try {
			jdbcTemplate.update(sql, params.toArray());
			afterUpdate = platformTransactionManager.getTransaction(paramTransactionDefinition);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			throw new DatabaseErrorException(e);
		}

		platformTransactionManager.commit(afterUpdate);
	}

}
