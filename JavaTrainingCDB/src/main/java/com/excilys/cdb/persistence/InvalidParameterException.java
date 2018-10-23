package com.excilys.cdb.persistence;

/**
 * @author Jonasz Leflour
 *
 */
public class InvalidParameterException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public InvalidParameterException(){
		super("Cannot use this computer (wrong id, empty name or incoherent dates)");
	}
}
