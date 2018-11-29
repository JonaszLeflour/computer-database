package com.excilys.cdb.service;

import org.springframework.stereotype.Service;

import com.excilys.cdb.model.Role;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.persistence.RoleDAO;

@Service
public class SimpleRoleService implements RoleService {
	private RoleDAO roleDAO;
	
	SimpleRoleService(RoleDAO roleDAO){
		this.roleDAO = roleDAO;
	}
	
	@Override
	public Role getRoleByName(String name) throws ObjectNotFoundException, DatabaseErrorException{
		return roleDAO.getRoleByName(name);
	}
	
	@Override
	public Role getRoleById(long id) throws ObjectNotFoundException, DatabaseErrorException{
		return roleDAO.getRoleById(id);
	}

}
