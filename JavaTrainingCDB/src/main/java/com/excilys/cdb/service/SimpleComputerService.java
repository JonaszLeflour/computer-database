
package com.excilys.cdb.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.persistence.ComputerDAO;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;

/**
 * 
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
@Service
public class SimpleComputerService implements ComputerService {
	@Autowired
	ComputerDAO computerDAO;

	@Override
	public List<Computer> getComputers() throws DatabaseErrorException {
		List<Computer> result = computerDAO.getAllComputers();
		return result;
	}

	@Override
	public List<Computer> getComputers(long offset, long pageSize) throws DatabaseErrorException {
		List<Computer> result = computerDAO.getAllComputers(offset, pageSize);
		return result;
	}

	@Override
	public Computer getComputerById(int id) throws ObjectNotFoundException, DatabaseErrorException {
		return computerDAO.getComputerById(id);
	}

	@Override
	public List<Computer> getComputersByName(String name) throws DatabaseErrorException {
		List<Computer> result = computerDAO.getComputerByName(name);
		return result;
	}

	@Override
	public List<Computer> getComputersByName(String name, long offset, long length) throws DatabaseErrorException {
		List<Computer> result = computerDAO.getComputersByName(name, offset, length);
		return result;
	}



	@Override
	public long countComputersByName(String name) throws DatabaseErrorException {
		return computerDAO.countComputersByName(name);
	}
	


	@Override
	public void updateComputer(Computer computer)
			throws InvalidParameterException, DatabaseErrorException, ObjectNotFoundException {
		computerDAO.updateComputer(computer);
	}

	@Override
	public void addComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException {
		computerDAO.createComputer(computer);
	}

	@Override
	public void deleteComputerById(long id)
			throws ObjectNotFoundException, DatabaseErrorException, InvalidParameterException {
		computerDAO.deleteComputerById(id);
	}

	@Override
	public void deleteComputersByName(String name)
			throws DatabaseErrorException, InvalidParameterException, ObjectNotFoundException {
		computerDAO.deleteComputerByName(name);
	}


	@Override
	public List<Computer> getOrderedComputersByName(String name, long offset, long lenght,
			ComputerDAO.ComputerField orderBy, OrderDirection direction)
			throws DatabaseErrorException {
		return computerDAO.getOrderedComputers(name, offset, lenght, orderBy, direction);
	}
	

}
