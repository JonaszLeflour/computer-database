package com.excilys.cdb.persistence;

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
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
