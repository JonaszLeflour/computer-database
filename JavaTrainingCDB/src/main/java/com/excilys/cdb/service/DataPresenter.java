package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;

/**
 * Presents data as list of objects and issues request to change the databse
 * @author Jonasz Leflour
 *
 */
public interface DataPresenter {
	/**
	 * @return all computers
	 * @throws DatabaseErrorException 
	 */
	public List<Computer> getComputers() throws DatabaseErrorException;

	/**
	 * @param id
	 * @return single computer with the specified id, or null
	 * @throws DatabaseErrorException 
	 * @throws ObjectNotFoundException 
	 */
	public Computer getComputerById(int id) throws DatabaseErrorException, ObjectNotFoundException;

	/**
	 * @param name
	 * @return all computers sharing the specified name
	 * @throws DatabaseErrorException 
	 * @throws ObjectNotFoundException 
	 */
	public List<Computer> getComputersByName(String name) throws DatabaseErrorException;

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
	 * @param computer computer to update with new values
	 * @throws InvalidParameterException 
	 * @throws DatabaseErrorException 
	 * @throws ObjectNotFoundException 
	 */
	public void updateComputer(Computer computer) throws InvalidParameterException, DatabaseErrorException, ObjectNotFoundException;

	/**
	 * @param computer data of the computer to add
	 * @throws DatabaseErrorException 
	 * @throws InvalidParameterException 
	 */
	public void addComputer(Computer computer) throws DatabaseErrorException, InvalidParameterException;

	/**
	 * @param id of the computer to remove from database
	 * @throws DatabaseErrorException 
	 * @throws ObjectNotFoundException 
	 */
	public void removeComputerById(int id) throws DatabaseErrorException, ObjectNotFoundException;

	/**
	 * @param name name shared by all computer to remove
	 * @throws DatabaseErrorException 
	 * @throws InvalidParameterException 
	 * @throws ObjectNotFoundException 
	 */
	public void removeComputersByName(String name) throws DatabaseErrorException, InvalidParameterException, ObjectNotFoundException;

	/**
	 * @param name
	 * @param l
	 * @param pageSize
	 * @return list of computer of max size length
	 * @throws DatabaseErrorException
	 */
	List<Computer> getComputersByName(String name, long l, long pageSize) throws DatabaseErrorException;

	/**
	 * @return nomber of computers stored in database
	 * @throws DatabaseErrorException
	 */
	long countAllComputers() throws DatabaseErrorException;

	/**
	 * @param name
	 * @return number of computers in database whose name match the string parametre
	 * @throws DatabaseErrorException 
	 */
	long countComputersByName(String name) throws DatabaseErrorException;

	/**
	 * @param offset
	 * @param pageSize
	 * @return subset of the complete computer list in database starting at offset and of max size lenght
	 * @throws DatabaseErrorException
	 */
	List<Computer> getComputers(long offset, long pageSize) throws DatabaseErrorException;
	
	/**
	 * @param id
	 */
	void deleteCompanyById(long id);
}
