package com.excilys.cdb.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.InvalidPasswordException;
import com.excilys.cdb.persistence.InvalidUsernameException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.persistence.UserDAO;

@Service
public class SimpleUserService implements UserService, UserDetailsService{
	UserDAO userDAO;
	PasswordEncoder passwordEncoder;
	
	public SimpleUserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.userDAO = userDAO;
	}
	
	@Override
	public void registerUser(User user) throws DatabaseErrorException, InvalidParameterException, InvalidUsernameException, InvalidPasswordException{
		userDAO.registerUser(user);
	}
	
	@Override
	public User connectToUser(String username, String rawPassword) throws ObjectNotFoundException, DatabaseErrorException, WrongPasswordException{
		User user = userDAO.getUserByName(username);
		if(passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new WrongPasswordException();
		}
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userDAO.getUserByName(username);
			return org.springframework.security.core.userdetails.User
					.withUsername(user.getName())
					.password(user.getPassword())
					.roles(user.getRole().getName()).build();
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.toString());
		}
	}

}
