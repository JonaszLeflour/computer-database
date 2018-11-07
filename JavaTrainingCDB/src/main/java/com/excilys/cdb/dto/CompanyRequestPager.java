package com.excilys.cdb.dto;

import java.util.List;

import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * @author Jonasz Leflour
 *
 */
public interface CompanyRequestPager {
	/**
	 * @param pageNumber 
	 * @return list of computers at page 1
	 * @throws DatabaseErrorException 
	 * @throws InvalidPageNumberException 
	 */
	List<DTOCompany> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException;
	/**
	 * @return total number of pages
	 * @throws DatabaseErrorException 
	 */
	long getNbPages() throws DatabaseErrorException;
	
	/**
	 * @return total number of computers
	 * @throws DatabaseErrorException
	 */
	long getNbComputers() throws DatabaseErrorException;
}
