package com.excilys.cdb.dto;
import java.util.List;

import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * @author Jonasz Leflour
 * Classes implementing this interface should provide limited requests to the database by pahing the results
 */
public interface ComputerRequestPager {

	/**
	 * @param pageNumber 
	 * @return list of computers at page 1
	 * @throws DatabaseErrorException 
	 */
	List<DTOComputer> getPage(long pageNumber) throws DatabaseErrorException;
}
