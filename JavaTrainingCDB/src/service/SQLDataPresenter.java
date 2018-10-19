
package service;

import model.Computer;
import persistence.DatabaseAccessor;
import persistence.ObjectNotFoundException;
import java.util.List;

import model.Company;

/**
 * 
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
public class SQLDataPresenter implements DataPresenter{
	private DatabaseAccessor dba = DatabaseAccessor.GetDatabaseAccessor();

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
