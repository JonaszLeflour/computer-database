package com.excilys.cdb.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.service.DataPresenter;
import com.excilys.cdb.service.SQLDataPresenter;

/**
 * @author Jonasz Leflour
 *
 */
public class CachedDTOProvider implements DTOProvider {
	DataPresenter dp = null;
	List<DTOComputer> allComputers = new ArrayList<DTOComputer>();
	boolean obsoleteData = true;

	/**
	 * @throws DatabaseErrorException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * 
	 */
	public CachedDTOProvider()
			throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
		dp = new SQLDataPresenter();
	}

	/**
	 * @param c
	 * @return DTO object version of c
	 */
	public static DTOComputer computertoDaoComputer(Computer c) {
		DTOComputer daoC = new DTOComputer();
		daoC.setId(String.valueOf(c.getId()));
		daoC.setName(c.getName() != null ? c.getName() : "");
		daoC.setIntroduced(c.getIntroduced() != null ? c.getIntroduced().toString() : "");
		daoC.setDiscontinued(c.getDiscontinued() != null ? c.getDiscontinued().toString() : "");
		daoC.setCompany(c.getCompany() != null ? c.getCompany().getName().toString() : "");
		return daoC;
	}

	/**
	 * @param c
	 * @return DTO object version of c
	 */
	public static DTOCompany companytoDaoCompany(Company c) {
		DTOCompany daoC = new DTOCompany();
		daoC.setId(String.valueOf(c.getId()));
		daoC.setName(c.getName() != null ? c.getName() : "");
		return daoC;
	}

	private void reloadAllComputers() throws DatabaseErrorException {
		allComputers.clear();
		for (Computer c : dp.getComputers()) {
			allComputers.add(computertoDaoComputer(c));
		}
		obsoleteData = false;
	}

	public List<DTOComputer> getAllComputers() throws DatabaseErrorException {
		reloadAllComputers();
		return allComputers;
	}

	/**
	 * @param pageNumber
	 * @param computersPerPage
	 * @return subList
	 */

	@Override
	public List<DTOComputer> getComputersByName(String nameFilter) throws DatabaseErrorException {
		List<DTOComputer> resultsByName = new ArrayList<DTOComputer>();
		if (obsoleteData) {
			List<Computer> result = dp.getComputersByName(nameFilter);
			for (Computer c : result) {
				resultsByName.add(computertoDaoComputer(c));
			}
		} else {
			for (DTOComputer c : allComputers) {
				if (nameFilter.equals(c.getName())) {
					resultsByName.add(c);
				}
			}
		}
		return resultsByName;
	}

	@Override
	public void editComputer(Computer c) throws DatabaseErrorException, InvalidParameterException {
		dp.addComputer(c);
		updateCache();
	}

	@Override
	public void addComputer(Computer c) throws DatabaseErrorException, InvalidParameterException {
		dp.addComputer(c);
		updateCache();
	}

	/**
	 * Mark data as obsolete so it will be loaded at next query
	 */
	public void updateCache() {
		obsoleteData = true;
	}

	@Override
	public List<DTOCompany> getAllCompanies() throws DatabaseErrorException {
		List<DTOCompany> result = new ArrayList<>();
		for (Company c : this.dp.getCompanies()) {
			result.add(companytoDaoCompany(c));
		}
		return result;
	}

}
