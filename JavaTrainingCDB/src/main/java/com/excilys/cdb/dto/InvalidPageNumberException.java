package com.excilys.cdb.dto;

/**
 * @author Jonasz Leflour
 *
 */
public class InvalidPageNumberException extends Exception {

	/**
	 * @param string
	 */
	public InvalidPageNumberException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
