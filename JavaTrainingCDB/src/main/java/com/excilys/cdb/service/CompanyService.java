package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.persistence.CompanyDAO.CompanyField;

/**
 * @author Jonasz Leflour
 *
 */
public interface CompanyService {
	/**
	 * @return all companies
	 * @throws DatabaseErrorException 
	 */
	public List<Company> getCompanies() throws DatabaseErrorException;
	
	/**
	 * @param id
	 * @return single company with the specified id, or null
	 * @throws DatabaseErrorException 
	 * @throws ObjectNotFoundException 
	 */
	public Company getCompanyById(int id) throws DatabaseErrorException, ObjectNotFoundException;
	
	/**
	 * @param id
	 * @throws ObjectNotFoundException 
	 * @throws DatabaseErrorException 
	 * @throws InvalidParameterException 
	 */
	void deleteCompanyById(long id) throws DatabaseErrorException, ObjectNotFoundException, InvalidParameterException;
	
	/**
	 * @param name pattern of companies to search, leave blank or null for all of them
	 * @param offset index of the first item in result
	 * @param lenght max size of result
	 * @param orderBy
	 * @param direction
	 * @return ordered list of companies
	 * @throws DatabaseErrorException
	 */
	List<Company> getOrderedCompaniesByName(String name, long offset, long lenght, CompanyField orderBy,
			OrderDirection direction) throws DatabaseErrorException;

	/**
	 * @param name pattern of companies to count, leave blank or null for all of them
	 * @return number of companies
	 * @throws DatabaseErrorException
	 */
	long countCompaniesByName(String name) throws DatabaseErrorException;
}
