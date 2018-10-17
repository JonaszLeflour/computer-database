package model;

import java.io.Serializable;
import java.time.LocalDate;


public class Computer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//not null unique
	private int id;
	
	//not null
	private String name;
	
	private LocalDate introduced;
	
	private LocalDate discontinued;

	private Company company;
	
	//constructor from database
	
	public Computer() {
	}
	
	public Computer(int id, String name, LocalDate introduced, LocalDate discontinued, Company company) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
	
	public String toString() {
		
		StringBuffer def = new StringBuffer();
		def.append("{id="+id);
		if(name != null) {
			def.append(", name="+name);
		}
		if(introduced != null) {
			def.append(", introduced="+introduced);
		}
		if(discontinued != null) {
			def.append(", discontinued="+discontinued);
		}
		if(company != null) {
			def.append(", company="+company.getName());
		}
		def.append("}");
		return def.toString();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id =id;
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

	public LocalDate getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
	}

	public LocalDate getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
	}
	

	
}
