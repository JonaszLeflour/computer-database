package com.excilys.cdb.persistence;


/**
 * @author Jonasz Leflour
 *
 */
public class EmptyResultSetException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public EmptyResultSetException() {
		super("Query yielded no results");
	}
	/**
	 * @param e
	 */
	public EmptyResultSetException(Exception e) {
		super(e);
	}
}
