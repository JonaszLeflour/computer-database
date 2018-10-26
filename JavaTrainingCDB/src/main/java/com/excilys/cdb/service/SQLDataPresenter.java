
package com.excilys.cdb.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseAccessor;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;

/**
 * 
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
public class SQLDataPresenter implements DataPresenter{
	private DatabaseAccessor dba = null;
	
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DatabaseErrorException 
	 */
	public SQLDataPresenter() throws FileNotFoundException, IOException, DatabaseErrorException {
		dba = DatabaseAccessor.GetDatabaseAccessor();
	}

	@Override
	public List<Computer> getComputers() {
		List<Computer> result = dba.getAllComputers();
		return result;
	}

	@Override
	public Computer getComputerById(int id) throws ObjectNotFoundException, DatabaseErrorException{
		return dba.getComputerById(id);
	}

	@Override
	public List<Computer> getComputersByName(String name) throws DatabaseErrorException {
		List<Computer> result = dba.getComputerByName(name);
		return result;
	}

	@Override
	public List<Company> getCompanies() throws DatabaseErrorException{
		List<Company> result = dba.getAllCompanies();
		return result;
	}

	@Override
	public Company getCompanyById(int id) throws ObjectNotFoundException {
		return dba.getCompanybyId(id);
	}

	@Override
	public void updateComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException {
		dba.updateComputer(computer);
		
	}

	@Override
	public void addComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException {
		dba.createComputer(computer);
	}

	@Override
	public void removeComputerById(int id) throws ObjectNotFoundException, DatabaseErrorException {
		dba.deleteComputerById(id);
	}
	
	@Override
	public void removeComputersByName(String name) throws DatabaseErrorException, InvalidParameterException, ObjectNotFoundException {
		dba.deleteComputerByName(name);
	}
}
