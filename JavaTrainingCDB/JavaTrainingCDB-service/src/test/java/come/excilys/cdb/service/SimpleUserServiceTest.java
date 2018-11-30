package come.excilys.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.RootConfig;
import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.SimpleUserService;
import com.excilys.cdb.service.WrongPasswordException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {RootConfig.class})
public class SimpleUserServiceTest {
	@Autowired
	SimpleUserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void connectToUserTest() throws ObjectNotFoundException, DatabaseErrorException, WrongPasswordException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		
		try {
			userService.connectToUser("user", "incorret password of existing user");
		}catch(WrongPasswordException e) {
			expectedFailure1 = true;
		}
		
		try {
			userService.connectToUser("nonexistent user", "password");
		}catch(ObjectNotFoundException e) {
			expectedFailure2 = true;
		}
		
		User user = userService.connectToUser("user", "userpassword");
		
		assertEquals("user",user.getName());
		assertEquals("guest",user.getRole().getName());
		
		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
		
	}
	
	@Test
	public void loadUserByUsernameTest() throws UsernameNotFoundException {
		UserDetails detailUser = userService.loadUserByUsername("user");
		UserDetails detailAdmin = userService.loadUserByUsername("admin");
		boolean expectedFailure1 = false;
		
		try {
			userService.loadUserByUsername("this username does not exist");
		}catch(UsernameNotFoundException e){
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);
		assertEquals("user",detailUser.getUsername());
		assertEquals("admin",detailAdmin.getUsername());
		assertTrue(passwordEncoder.matches("userpassword", detailUser.getPassword()));
		
	}
	

}
