package com.excilys.cdb.dao;

/**
 * @author Jonasz Leflour
 * 
 */
public class DaoComputer {
	private String id ="";
	private String name ="";
	private String introduced="";
	private String discontinued="";
	private String company="";
	
	/**
	 * 
	 */
	public DaoComputer() {
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return name
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
	 * @return introduced date
	 */
	public String getIntroduced() {
		return introduced;
	}

	/**
	 * @param introduced date
	 */
	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	/**
	 * @return discontinued date
	 */
	public String getDiscontinued() {
		return discontinued;
	}

	/**
	 * @param discontinued date
	 */
	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	/**
	 * @return company name
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company name
	 */
	public void setCompany(String company) {
		this.company = company;
	}

}
