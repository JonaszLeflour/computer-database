package com.excilys.cdb.dao;

import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;

/**
 * @author JonaszLeflour
 *
 */
public interface DaoProvider {
	/**
	 * @return all computers in database
	 * @throws DatabaseErrorException 
	 */
	public List<DaoComputer> getAllComputers() throws DatabaseErrorException;
	/**
	 * @param nameFilter
	 * @return all computers matching filter
	 * @throws DatabaseErrorException 
	 */
	public List<DaoComputer> getComputersByName(String nameFilter) throws DatabaseErrorException;
	
	/**
	 * @param c updated computer
	 * @throws InvalidParameterException 
	 * @throws DatabaseErrorException 
	 */
	public void editComputer(Computer c) throws DatabaseErrorException, InvalidParameterException;
	
	/**
	 * @param c
	 * @throws InvalidParameterException 
	 * @throws DatabaseErrorException 
	 */
	public void addComputer(Computer c) throws DatabaseErrorException, InvalidParameterException;
}
