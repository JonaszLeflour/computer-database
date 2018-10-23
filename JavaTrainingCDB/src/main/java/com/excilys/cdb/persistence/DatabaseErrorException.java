package com.excilys.cdb.persistence;

/**
 * @author JonaszLeflour
 *
 */
public class DatabaseErrorException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public DatabaseErrorException() {
		super("Something wrong happened while using the database");
	}
}
