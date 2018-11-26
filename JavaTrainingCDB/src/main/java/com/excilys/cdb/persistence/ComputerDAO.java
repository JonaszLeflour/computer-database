package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.QComputer;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
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
	@SuppressWarnings("unused")
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SessionFactory sessionFactory;

	// @Autowired
	// private ComputerResultSetMapper computerResultSetMapper;

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

		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		QComputer computer = QComputer.computer;
		try {
			return factory.selectFrom(computer).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}

	}

	/**
	 * @param offset
	 * @param lenght max size of the array (less if no more computers in database)
	 * @return array of lenght or less computers
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getAllComputers(long offset, long lenght) throws DatabaseErrorException {
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		QComputer computer = QComputer.computer;
		try {
			return factory.selectFrom(computer).offset(offset).limit(lenght).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
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
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		QComputer computer = QComputer.computer;
		try {
			return factory.selectFrom(QComputer.computer)
					.where(computer.name.toUpperCase()
							.like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")))
					.offset(offset).limit(lenght).orderBy(computer.id.desc()).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @param id
	 * @return single row of the computer table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public Computer getComputerById(long id) throws ObjectNotFoundException, DatabaseErrorException {
		QComputer computer = QComputer.computer;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(computer).where(computer.id.eq(id)).orderBy(computer.id.desc()).fetch().get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
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
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			HibernateQuery<Computer> query = factory.selectFrom(computer)
					.where(computer.name.toUpperCase()
							.like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")))
					.offset(offset).limit(lenght);
			order(query, computer, orderBy, direction);

			return query.fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @param name
	 * @return all rows of the computer table with the specified name, if any
	 * @throws DatabaseErrorException
	 */
	public List<Computer> getComputerByName(String name) throws DatabaseErrorException {
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		QComputer computer = QComputer.computer;
		try {
			return factory.selectFrom(computer)
					.where(computer.name.toUpperCase()
							.like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")))
					.orderBy(computer.id.desc()).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @param name name pattern of computers to count, or empty/null for all of them
	 * @return number of computers in database
	 * @throws DatabaseErrorException
	 */
	public long countComputersByName(String name) throws DatabaseErrorException {
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		QComputer computer = QComputer.computer;
		try {
			return factory.selectFrom(computer).where(
					computer.name.toUpperCase().like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")))
					.fetchCount();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @param computer
	 * @throws InvalidParameterException
	 * @throws DatabaseErrorException
	 */
	// insert unsuported by querydsl. Must use hibernate directly
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

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.save(computer);
			session.getTransaction().commit();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			session.close();
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
		}
		if (name.isEmpty()) {
			throw new InvalidParameterException("Name provided is empty");
		}

		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());

		long deleted = 0;
		try {
			HibernateDeleteClause deleteClause = factory.delete(QComputer.computer).where(QComputer.computer.name
					.toUpperCase().like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")));
			deleted = deleteClause.execute();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}

		if (deleted == 0) {
			throw new ObjectNotFoundException("No computer named " + name);
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
		HibernateQueryFactory factory = new HibernateQueryFactory(sessionFactory.openSession());
		long deleted = 0;
		try {
			deleted = factory.delete(QComputer.computer).where(QComputer.computer.id.eq(id)).execute();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		}

		if (deleted == 0) {
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

		// !\\ TODO: move to validator class
		if (computer == null) {
			throw new InvalidParameterException("Computer object is null");
		} else if (computer.getName() != null && computer.getName().isEmpty()) {
			throw new InvalidParameterException("Computer name cannot be empty");
		}

		if (computer.getId() <= 0) {
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

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		// Save employee
		try {
			session.update(computer);
			session.getTransaction().commit();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			session.close();
		}
	}

}
