package com.excilys.cdb.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.RootConfig;
import com.excilys.cdb.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {RootConfig.class})
public class UserDAOTest {
	@Autowired
	UserDAO userDAO;
	
	@Test
	public void testGetAllUsers() throws DatabaseErrorException {
		List<User> users = userDAO.getUsers();
		assertNotNull(users);
		assertTrue(users.size()>0);
	}
	
	@Test
	public void testGetUser() throws DatabaseErrorException, ObjectNotFoundException {
		boolean expectedFailure = false;
		User admin = userDAO.getUserByName("admin");
		User user = userDAO.getUserByName("user");
		assertEquals("admin",admin.getName());
		assertEquals("user",user.getName());
		
		try {
			//TODO: find an username that isn't in the database procedurally
			userDAO.getUserByName("this username isn't in the database");
		}catch(ObjectNotFoundException e) {
			expectedFailure = true;
		}
		assertTrue(expectedFailure);
	}
}
