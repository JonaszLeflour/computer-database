package model;

import java.io.Serializable;
import java.sql.Date;


public class Computer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//not null unique
	private int id;
	
	//not null
	private String name;
	
	private Date introduced;
	
	private Date discontinued;

	private Company company;
	
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

	public Date getIntroduced() {
		return introduced;
	}

	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}

	public Date getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}
	

	
}
