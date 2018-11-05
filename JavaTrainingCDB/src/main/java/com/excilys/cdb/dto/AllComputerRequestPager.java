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
	 * 
	 */
	public AllComputerRequestPager(long pageSize) throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException {
		this.pageSize = pageSize;
		this.dp = new SQLDataPresenter();
		
	}

	@Override
	public List<DTOComputer> getPage(long pageNumber) throws DatabaseErrorException {
		List<DTOComputer> list = new ArrayList<DTOComputer>();
		for(Computer c : dp.getComputers(pageNumber*pageSize, pageSize)) {
			list.add(CachedDTOProvider.computertoDaoComputer(c));
		}
		return list;
	}

}
