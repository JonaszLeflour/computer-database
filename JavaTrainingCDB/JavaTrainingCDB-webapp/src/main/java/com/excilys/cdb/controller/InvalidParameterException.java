package com.excilys.cdb.controller;

/**
 * @author Jonasz Leflour
 *
 */
public class InvalidParameterException extends Exception {

	/**
	 * @param e
	 */
	public InvalidParameterException(Exception e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
