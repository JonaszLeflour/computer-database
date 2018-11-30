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
import com.excilys.cdb.model.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {RootConfig.class})
public class RoleDAOTest {
	@Autowired
	RoleDAO roleDAO;
	
	@Test
	public void testGetAllUsers() throws DatabaseErrorException, ObjectNotFoundException {
		List<Role> roles = roleDAO.getRoles();
		assertNotNull(roles);
		assertTrue(roles.size()>0);
	}
	
	@Test
	public void testGetRoleByName() throws DatabaseErrorException, ObjectNotFoundException {
		boolean expectedFailure = false;
		Role admin = roleDAO.getRoleByName("admin");
		Role guest = roleDAO.getRoleByName("guest");
		assertEquals("admin",admin.getName());
		assertEquals("guest",guest.getName());
		
		assertTrue(admin.isSelect());
		assertTrue(admin.isEdit());
		assertTrue(admin.isDelete());
		assertTrue(admin.isInsert());
		
		assertTrue(guest.isSelect());
		assertTrue(!guest.isEdit());
		assertTrue(!guest.isDelete());
		assertTrue(!guest.isInsert());
		
		try {
			//TODO: find a role that isn't in the database procedurally
			roleDAO.getRoleByName("this role isn't in the database");
		}catch(ObjectNotFoundException e) {
			expectedFailure = true;
		}
		assertTrue(expectedFailure);
	}
}
