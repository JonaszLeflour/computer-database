package com.excilys.cdb.dto;

/**
 * @author Jonasz Leflour
 * 
 */
public class DTOComputer {
	private String id ="";
	private String name ="";
	private String introduced="";
	private String discontinued="";
	private String company="";
	private String companyId="";
	
	/**
	 * 
	 */
	public DTOComputer() {
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

	/**
	 * @return company id
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
