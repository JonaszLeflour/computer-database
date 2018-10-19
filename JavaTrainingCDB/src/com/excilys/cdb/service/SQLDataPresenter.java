
package com.excilys.cdb.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseAccessor;
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
	 */
	public SQLDataPresenter() throws FileNotFoundException, IOException {
		dba = DatabaseAccessor.GetDatabaseAccessor();
	}

	@Override
	public List<Computer> getComputers() {
		List<Computer> result = dba.getAllComputers();
		return result;
	}

	@Override
	public Computer getComputerById(int id) {
		try {
			return dba.getComputerById(id);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Computer> getComputersByName(String name) {
		List<Computer> result = dba.getComputerByName(name);
		return result;
	}

	@Override
	public List<Company> getCompanies() {
		List<Company> result = dba.getAllCompanies();
		return result;
	}

	@Override
	public Company getCompanyById(int id) {
		try {
			return dba.getCompanybyId(id);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateComputer(Computer computer) {
		dba.updateComputer(computer);
	}

	@Override
	public void addComputer(Computer computer) {
		dba.createComputer(computer);
	}

	@Override
	public void removeComputerById(int id) {
		dba.deleteComputerById(id);
	}
	
	@Override
	public void removeComputersByName(String name) {
		dba.deleteComputerByName(name);
	}
}
