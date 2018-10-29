package com.excilys.cdb.ui;

import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
public interface UserInterface {
	/**
	 * User interaction loop
	 * @param args
	 * @throws DatabaseErrorException 
	 */
	public void mainLoop(String[] args) throws DatabaseErrorException;
}
