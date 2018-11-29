package com.excilys.cdb.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.QRole;
import com.excilys.cdb.model.Role;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;

@Repository
public class RoleDAO {
private SessionFactory sessionFactory;
	
	public RoleDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<Role> getRoles() throws ObjectNotFoundException, DatabaseErrorException{
		QRole role = QRole.role;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(role).fetch();
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}
	
	public Role getRoleByName(String name) throws ObjectNotFoundException, DatabaseErrorException{
		QRole role = QRole.role;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(role).where(role.name.eq(name)).fetch().get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}
	
	public Role getRoleById(long id) throws ObjectNotFoundException, DatabaseErrorException{
		QRole role = QRole.role;
		Session s = sessionFactory.openSession();
		HibernateQueryFactory factory = new HibernateQueryFactory(s);
		try {
			return factory.selectFrom(role).where(role.id.eq(id)).fetch().get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new ObjectNotFoundException(e);
		} catch (Exception e) {
			throw new DatabaseErrorException(e);
		} finally {
			s.close();
		}
	}

}
