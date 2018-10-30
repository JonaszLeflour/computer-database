package com.excilys.cdb.dao;

/**
 * @author JOnasz Leflour
 *
 */
public class DaoCompany {
	private String name;
	private String id;
	/**
	 * @return name of company
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return id of company in database
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id of company in database
	 */
	public void setId(String id) {
		this.id = id;
	}
}
