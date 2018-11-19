package com.excilys.cdb.persistence;

/**
 * @author Jonasz Leflour
 *
 */
public class InvalidParameterException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * @param string Error message
	 */
	public InvalidParameterException(String string) {
		super(string);
	}

	/**
	 * @param e
	 */
	public InvalidParameterException(Exception e) {
		super(e);
	}
}
