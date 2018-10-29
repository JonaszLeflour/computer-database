package com.excilys.cdb.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.service.DataPresenter;
import com.excilys.cdb.service.SQLDataPresenter;

/**
 * @author Jonasz Leflour
 *
 */
public class CachedDaoProvider implements DaoProvider{
	DataPresenter dp = null;
	List<DaoComputer> allComputers = new ArrayList<DaoComputer>();
	boolean obsoleteData = true;
	
	
	/**
	 * @throws DatabaseErrorException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public CachedDaoProvider() throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
		dp = new SQLDataPresenter();
	}
	
	private DaoComputer computertoDaoComputer(Computer c) {
		DaoComputer daoC = new DaoComputer();
		daoC.setId(String.valueOf(c.getId()));
		daoC.setName(c.getName()!=null ? c.getName() : "");
		daoC.setIntroduced(c.getIntroduced()!=null ? c.getIntroduced().toString() : "");
		daoC.setDiscontinued(c.getDiscontinued()!=null ? c.getDiscontinued().toString() : "");
		daoC.setCompany(c.getCompany()!=null ? c.getCompany().getName().toString() : "");
		return daoC;
	}
	private void reloadAllComputers() throws DatabaseErrorException {
		allComputers.clear();
		for(Computer c :dp.getComputers()) {
			allComputers.add(computertoDaoComputer(c));
		}
		obsoleteData = false;
	}
	
	public List<DaoComputer> getAllComputers() throws DatabaseErrorException{
		reloadAllComputers();
		return allComputers;
	}
	
	/**
	 * @param pageNumber
	 * @param computersPerPage
	 * @return subList
	 */

	@Override
	public List<DaoComputer> getComputersByName(String nameFilter) throws DatabaseErrorException{
		List<DaoComputer> resultsByName = new ArrayList<DaoComputer>();
		if(obsoleteData) {
			List<Computer> result = dp.getComputersByName(nameFilter);
			for(Computer c : result) {
				resultsByName.add(this.computertoDaoComputer(c));
			}
		}else {
			for(DaoComputer c : allComputers) {
				if(nameFilter.equals(c.getName())) {
					resultsByName.add(c);
				}
			}
		}
		return resultsByName;
	}

	@Override
	public void editComputer(Computer c) throws DatabaseErrorException, InvalidParameterException{
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
	

}
