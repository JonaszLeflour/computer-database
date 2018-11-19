package com.excilys.cdb.persistence;

import org.springframework.dao.EmptyResultDataAccessException;

/**
 * @author Jonasz Leflour
 *
 */
public class ObjectNotFoundException extends Exception {

	/**
	 * @param string
	 */
	public ObjectNotFoundException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	public ObjectNotFoundException() {
		super();
	}
	
	/**
	 * @param e
	 */
	public ObjectNotFoundException(EmptyResultDataAccessException e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
