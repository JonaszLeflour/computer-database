package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.QCompany;
import com.excilys.cdb.model.QComputer;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;

/**
 * @author Jonasz Leflour
 *
 */
@Repository
public class CompanyDAO {
	@SuppressWarnings("unused")
	@Autowired
	private DataSource dataSource;

	@Autowired
	CompanyResultSetMapper companyResultSetMapper;

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * @author Jonasz Leflour
	 *
	 */
	public static enum CompanyField {
		@SuppressWarnings("javadoc")
		id, @SuppressWarnings("javadoc")
		name;
	}

	private HibernateQuery<Company> order(HibernateQuery<Company> query, QCompany company, CompanyField orderBy,
			OrderDirection direction) {
		switch (orderBy) {
		case id:
			if (direction.equals(OrderDirection.ASC)) {
				query.orderBy(company.id.asc());
			} else {
				query.orderBy(company.id.desc());
			}
			break;

		case name:
			if (direction.equals(OrderDirection.ASC)) {
				query.orderBy(company.name.asc());
			} else {
				query.orderBy(company.name.desc());
			}
			break;

		default:
			break;
		}
		return query;
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

		QCompany company = QCompany.company;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			HibernateQuery<Company> query = factory.selectFrom(company)
					.where(company.name.toUpperCase()
							.like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")))
					.offset(offset).limit(lenght);
			order(query, company, orderBy, direction);

			return query.fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @return all companies from the database as a ResultSet
	 * @throws DatabaseErrorException
	 * @throws SQLException
	 */
	public List<Company> getAllCompanies() throws DatabaseErrorException {
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		QCompany company = QCompany.company;
		try {
			return factory.selectFrom(company).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @param id id of company
	 * @return single row of the company table with the specified id, if exists
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException
	 */
	public Company getCompanybyId(long id) throws ObjectNotFoundException, DatabaseErrorException {
		QCompany company = QCompany.company;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(company).where(company.id.eq(id)).orderBy(company.id.desc()).fetch().get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

	/**
	 * @param id
	 * @throws DatabaseErrorException
	 * @throws ObjectNotFoundException
	 * @throws InvalidParameterException
	 */
	public void deleteCompanyById(long id)
			throws DatabaseErrorException, ObjectNotFoundException, InvalidParameterException {
		getCompanybyId(id);
		QCompany company = QCompany.company;
		QComputer computer = QComputer.computer;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);

		long deleted = 0, deletedComputers = 0;
		s.beginTransaction();
		try {
			deletedComputers = factory.delete(computer).where(computer.company().id.eq(id)).execute();
		} catch (Exception e) {
			s.close();
			throw new DatabaseErrorException(e);
		}
		try {
			deleted = factory.delete(company).where(company.id.eq(id)).execute();

		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			if (deleted == 0 && deletedComputers == 0) {
				s.close();
				throw new ObjectNotFoundException();
			} else {
				s.getTransaction().commit();
				s.close();
			}
		}
		return;
	}

	/**
	 * @param name
	 * @return number of companies matching name in database
	 * @throws DatabaseErrorException
	 */
	public long countCompaniesByName(String name) throws DatabaseErrorException {
		QCompany company = QCompany.company;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			HibernateQuery<Company> query = factory.selectFrom(company).where(
					company.name.toUpperCase().like(Expressions.asString("%").concat(name.toUpperCase()).concat("%")));

			return query.fetchCount();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}
}
