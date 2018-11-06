package com.excilys.cdb.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.service.*;

/**
 * @author Jonasz Leflour
 */
public class AllComputerRequestPager implements ComputerRequestPager {
	long pageSize;
	DataPresenter dp;
	/**
	 * @param pageSize 
	 * @throws DatabaseErrorException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 * @throws InvalidPageSizeException 
	 * 
	 */
	public AllComputerRequestPager(long pageSize) throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException, InvalidPageSizeException{
		if(pageSize <= 0) {
			throw new InvalidPageSizeException("Page size must be strictly positive");
		}
		
		this.pageSize = pageSize;
		this.dp = new SQLDataPresenter();
		
	}

	@Override
	public List<DTOComputer> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException{
		if(pageNumber < 0) {
			throw new InvalidPageNumberException("Page number must be positive");
		}
		List<DTOComputer> list = new ArrayList<DTOComputer>();
		for(Computer c : dp.getComputers(pageNumber*pageSize, pageSize)) {
			list.add(CachedDTOProvider.computertoDaoComputer(c));
		}
		if(list.isEmpty() ) {
			throw new InvalidPageNumberException("This page doesn't exist");
		}
		return list;
	}

	@Override
	public long getNbPages() throws DatabaseErrorException {
		return (long)Math.ceil(dp.countAllComputers()/((double)pageSize));
	}

	@Override
	public long getNbComputers() throws DatabaseErrorException {
		return dp.countAllComputers();
	}

}
