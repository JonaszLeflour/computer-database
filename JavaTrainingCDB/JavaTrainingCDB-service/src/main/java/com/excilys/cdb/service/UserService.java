package com.excilys.cdb.service;

import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.InvalidPasswordException;
import com.excilys.cdb.persistence.InvalidUsernameException;
import com.excilys.cdb.persistence.ObjectNotFoundException;

public interface UserService {
	public void registerUser(User user) throws DatabaseErrorException, InvalidParameterException, InvalidUsernameException, InvalidPasswordException;

	User connectToUser(String username, String rawPassword) throws ObjectNotFoundException, DatabaseErrorException, WrongPasswordException;

}
