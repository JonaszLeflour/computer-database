
package com.excilys.cdb.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.persistence.CompanyDAO;
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
public class SQLDataPresenter implements DataPresenter {
	private ComputerDAO computerDAO = null;
	private CompanyDAO companyDAO = null;

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DatabaseErrorException
	 * @throws ClassNotFoundException
	 */
	public SQLDataPresenter()
			throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
		computerDAO = ComputerDAO.getInstance();
		companyDAO = CompanyDAO.getInstance();
	}

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
	public List<Company> getCompanies() throws DatabaseErrorException {
		List<Company> result = companyDAO.getAllCompanies();
		return result;
	}

	@Override
	public long countComputersByName(String name) throws DatabaseErrorException {
		return computerDAO.countComputersByName(name);
	}
	
	@Override
	public long countCompaniesByName(String name) throws DatabaseErrorException {
		return computerDAO.countCompaniesByName(name);
	}

	@Override
	public Company getCompanyById(int id) throws ObjectNotFoundException, DatabaseErrorException {
		return companyDAO.getCompanybyId(id);
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
	public void deleteCompanyById(long id)
			throws DatabaseErrorException, ObjectNotFoundException, InvalidParameterException {
		companyDAO.deleteCompanyById(id);

	}

	@Override
	public List<Computer> getOrderedComputersByName(String name, long offset, long lenght,
			ComputerDAO.ComputerField orderBy, OrderDirection direction)
			throws DatabaseErrorException {
		return computerDAO.getOrderedComputers(name, offset, lenght, orderBy, direction);
	}
	
	@Override
	public List<Company> getOrderedCompaniesByName(String name, long offset, long lenght,
			CompanyDAO.CompanyField orderBy, OrderDirection direction)
			throws DatabaseErrorException {
		return companyDAO.getOrderedCompanies(name, offset, lenght, orderBy, direction);
	}
}
