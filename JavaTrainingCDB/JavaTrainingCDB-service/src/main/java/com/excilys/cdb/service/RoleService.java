package com.excilys.cdb.service;

import com.excilys.cdb.model.Role;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.ObjectNotFoundException;

public interface RoleService {

	Role getRoleByName(String name) throws ObjectNotFoundException, DatabaseErrorException;

	Role getRoleById(long id) throws ObjectNotFoundException, DatabaseErrorException;

}
