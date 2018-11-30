package com.excilys.cdb.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.QUser;
import com.excilys.cdb.model.User;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;

@Repository
public class UserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public User getUserByName(String name) throws ObjectNotFoundException, DatabaseErrorException {
		QUser user = QUser.user;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(user).where(user.name.eq(name)).fetch().get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException("No user with name="+name);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}
	
	public List<User> getUsers() throws DatabaseErrorException {
		QUser user = QUser.user;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(user).fetch();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}
	
	public void registerUser(User user) throws DatabaseErrorException, InvalidParameterException, InvalidUsernameException, InvalidPasswordException {
		UserValidator.isValid(user);	
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			session.close();
		}
	}
}
