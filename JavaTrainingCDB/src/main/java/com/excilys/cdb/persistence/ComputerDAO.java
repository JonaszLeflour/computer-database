package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.QComputer;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;

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
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SessionFactory sessionFactory;

	//@Autowired
	//private ComputerResultSetMapper computerResultSetMapper;

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
	 * @return all computers from the database as a ResultSet
	 * @throws DatabaseErrorException
	 * @throws SQLException
	 */
	public List<Computer> getAllComputers() throws DatabaseErrorException {

		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		return factory.selectFrom(QComputer.computer).fetch();
	}

	/**
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getAllComputers(long offset, long lenght) throws DatabaseErrorException {
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());

		try {
			return factory.selectFrom(QComputer.computer).offset(offset).limit(lenght).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}

	}

	/**
	 * @param name
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getComputersByName(String name, long offset, long lenght) throws DatabaseErrorException {
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		try {
			return factory.selectFrom(QComputer.computer)
					.where(QComputer.computer.name.like(Expressions.asString("%").concat(name).concat("%")))
					.offset(offset).limit(lenght).orderBy(QComputer.computer.id.desc()).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
	}

	/**
	 * @param id
	 * @return single row of the computer table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public Computer getComputerById(long id) throws ObjectNotFoundException, DatabaseErrorException {

		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		try {
			return factory.selectFrom(QComputer.computer).where(QComputer.computer.id.eq(id))
					.orderBy(QComputer.computer.id.desc()).fetch().get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
	}

	private HibernateQuery<Computer> order(HibernateQuery<Computer> query, QComputer computer, ComputerField orderBy,
			OrderDirection direction) {
		switch (orderBy) {
		case id:
			if (direction.equals(OrderDirection.ASC)) {
				query.orderBy(computer.id.asc());
			} else {
				query.orderBy(computer.id.desc());
			}
			break;

		case name:
			if (direction.equals(OrderDirection.ASC)) {
				query.orderBy(computer.name.asc());
			} else {
				query.orderBy(computer.name.desc());
			}
			break;

		case introduced:
			if (direction.equals(OrderDirection.ASC)) {
				query.orderBy(computer.introduced.asc());
			} else {
				query.orderBy(computer.introduced.desc());
			}
			break;

		case discontinued:
			if (direction.equals(OrderDirection.ASC)) {
				query.orderBy(computer.discontinued.asc());
			} else {
				query.orderBy(computer.discontinued.desc());
			}
			break;

		default:
			break;
		}
		return query;
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
		QComputer computer = QComputer.computer;
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		try {
			HibernateQuery<Computer> query = factory.selectFrom(computer)
					.where(computer.name.like(Expressions.asString("%").concat(name).concat("%"))).offset(offset)
					.limit(lenght);
			order(query, computer, orderBy, direction);

			return query.fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
	}

	/**
	 * @param name
	 * @return all rows of the computer table with the specified name, if any
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getComputerByName(String name) throws DatabaseErrorException {
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		try {
			return factory.selectFrom(QComputer.computer)
					.where(QComputer.computer.name.like(Expressions.asString("%").concat(name).concat("%")))
					.orderBy(QComputer.computer.id.desc()).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
	}

	/**
	 * @param name name pattern of computers to count, or empty/null for all of them
	 * @return number of computers in database
	 * @throws DatabaseErrorException
	 */
	public long countComputersByName(String name) throws DatabaseErrorException {
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		try {
			return factory.selectFrom(QComputer.computer)
					.where(QComputer.computer.name.like(Expressions.asString("%").concat(name).concat("%")))
					.fetchCount();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}
	}

	/**
	 * @param computer
	 * @throws InvalidParameterException
	 * @throws DatabaseErrorException
	 */
	public void createComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException {
		// HibernateQueryFactory factory = new
		// HibernateQueryFactory(sessionFactory.openSession());

		/*
		 * try { return factory.update(QComputer.computer).
		 * .where(QComputer.computer.id.eq(id)).orderBy(QComputer.computer.id.desc())
		 * .fetch() .get(0); }catch(IndexOutOfBoundsException e) { throw new
		 * ObjectNotFoundException(e); } catch (Exception e) { throw new
		 * DatabaseErrorException(e); }
		 */

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
