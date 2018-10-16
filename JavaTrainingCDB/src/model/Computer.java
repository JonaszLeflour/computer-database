package model;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Computer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//not null unique
	private int id;
	
	private Company company;
	
	//not null
	private String name;
	
	private LocalDateTime introduced;
	
	private LocalDateTime discontinued;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}

	public LocalDateTime getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}
	

	
}
