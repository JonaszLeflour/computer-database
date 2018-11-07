
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
	 * @throws ClassNotFoundException 
	 */
	public SQLDataPresenter() throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
		dba = DatabaseAccessor.GetDatabaseAccessor();
	}

	@Override
	public List<Computer> getComputers() throws DatabaseErrorException {
		List<Computer> result = dba.getAllComputers();
		return result;
	}
	
	@Override
	public List<Computer> getComputers(long offset, long pageSize) throws DatabaseErrorException {
		List<Computer> result = dba.getAllComputers(offset, pageSize);
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
	public List<Computer> getComputersByName(String name, long offset, long length) throws DatabaseErrorException {
		List<Computer> result = dba.getComputersByName(name, offset, length);
		return result;
	}

	@Override
	public List<Company> getCompanies() throws DatabaseErrorException{
		List<Company> result = dba.getAllCompanies();
		return result;
	}
	
	@Override
	public long countAllComputers() throws DatabaseErrorException {
		return dba.countComputersByName("");
	}

	@Override
	public long countComputersByName(String name) throws DatabaseErrorException {
		return dba.countComputersByName(name);
	}
	
	@Override
	public Company getCompanyById(int id) throws ObjectNotFoundException, DatabaseErrorException {
		return dba.getCompanybyId(id);
	}

	@Override
	public void updateComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException, ObjectNotFoundException {
		dba.updateComputer(computer);
	}

	@Override
	public void addComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException {
		dba.createComputer(computer);
	}

	@Override
	public void deleteComputerById(long id) throws ObjectNotFoundException, DatabaseErrorException, InvalidParameterException {
		dba.deleteComputerById(id);
	}
	
	@Override
	public void deleteComputersByName(String name) throws DatabaseErrorException, InvalidParameterException, ObjectNotFoundException {
		dba.deleteComputerByName(name);
	}

	@Override
	public void deleteCompanyById(long id) throws DatabaseErrorException, ObjectNotFoundException, InvalidParameterException {
		dba.deleteCompanyById(id);
		
	}
	
	@Override
	public List <Computer> getOrderedComputersByName(String name, long offset, long lenght, DatabaseAccessor.ComputerField orderBy, DatabaseAccessor.OrderDirection direction ) throws DatabaseErrorException {
		return dba.getOrderedComputers(name, offset, lenght, orderBy, direction);
		
	}
}
