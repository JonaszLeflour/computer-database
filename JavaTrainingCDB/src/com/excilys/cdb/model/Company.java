package com.excilys.cdb.model;
import java.io.Serializable;


/**
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	//not null unique
	private long id;
	
	private String name;

	/**
	 * @param id company id in database
	 * @param name company name
	 */
	public Company(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "{id="+id+", name="+name+"}";
	}
	
	/**
	 * @return id of company in database
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id of company in database
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return name of company
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name of company
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
