package com.excilys.cdb.persistence;

import com.excilys.cdb.model.User;

public class UserValidator {
	public static Boolean isValid(User user)
			throws InvalidParameterException, InvalidUsernameException, InvalidPasswordException {
		if (user == null) {
			throw new InvalidParameterException("User is null");
		}
		if (user.getName() == null || "".equals(user.getName())) {
			throw new InvalidUsernameException();
		}
		if (user.getPassword() == null || "".equals(user.getPassword())){
			throw new InvalidPasswordException();
		}
		return true;
	}
}
