package com.excilys.cdb.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.service.SQLDataPresenter;
import com.excilys.cdb.service.DataPresenter;

/**
 * @author Jonasz Leflour
 *
 */
public class NameComputerRequestPager implements ComputerRequestPager {
	private long pageSize;
	private String searchName;
	private DataPresenter dp;
	/**
	 * @param name 
	 * @param pageSize max number of resultats per page
	 * @throws DatabaseErrorException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 * 
	 */
	
	public NameComputerRequestPager(String name, long pageSize) throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException {
		searchName = name;
		this.pageSize = pageSize;
		this.dp = new SQLDataPresenter();
	}

	@Override
	public List<DTOComputer> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException{
		if(pageNumber < 0) {
			throw new InvalidPageNumberException("Page number must be positive");
		}
		List<DTOComputer> list = new ArrayList<DTOComputer>();
		for(Computer c : dp.getComputersByName(searchName,pageNumber*pageSize, pageSize)) {
			list.add(CachedDTOProvider.computertoDaoComputer(c));
		}
		if(list.isEmpty() ) {
			throw new InvalidPageNumberException("This page doesn't exist");
		}
		return list;
	}

	@Override
	public long getNbPages() throws DatabaseErrorException{
		return (long)Math.ceil(dp.countComputersByName(searchName)/((double)pageSize));
	}

	@Override
	public long getNbComputers() throws DatabaseErrorException {
		return dp.countComputersByName(searchName);
	}

}
