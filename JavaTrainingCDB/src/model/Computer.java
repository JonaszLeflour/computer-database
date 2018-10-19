
package model;

import java.io.Serializable;
import java.time.LocalDate;
/** 
 * 
 * @author Jonasz Leflour
 * @version %I%
 */
public class Computer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
	private LocalDate introduced;
	
	private LocalDate discontinued;

	private Company company;
	
	//constructor from database
	
	/**
	 * 
	 */
	public Computer() {
	}
	
	/**
	 * @param id Computer id in database
	 * @param name Computer name
	 * @param introduced Date of introduction
	 * @param discontinued Date of discontinuation
	 * @param company Manufacturer
	 */
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
	
	/**
	 * @return Computer id in database
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id Computer id in database
	 */
	public void setId(int id) {
		this.id =id;
	}

	/**
	 * @return Computer manufacturer or null if none specified
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company Computer manufacturer 
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	/**
	 * @return Computer name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Computer name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Introduction date or null if none specified
	 */
	public LocalDate getIntroduced() {
		return introduced;
	}

	/**
	 * @param introduced Introduction date
	 */
	public void setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
	}

	/**
	 * @return Discontinuation date
	 */
	public LocalDate getDiscontinued() {
		return discontinued;
	}

	/**
	 * @param discontinued
	 */
	public void setDiscontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
	}
	
	/**
	 * @param b 
	 */
	public Computer(Builder b) {
		id = b.id;
		name = b.name;
		introduced = b.introduced;
		discontinued = b.discontinued;
		company = b.company;
	}
	
	/**
	 * Builder pattern
	 * @author Jonasz Leflour
	 *
	 */
	public static class Builder{
		private int id;
		
		private String name;
		
		private LocalDate introduced;
		
		private LocalDate discontinued;

		private Company company;
		
		/**
		 * @param id
		 * @return this
		 */
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		/**
		 * @param name
		 * @return this
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * @param introduced
		 * @return this
		 */
		public Builder introduced(LocalDate introduced) {
			this.introduced = introduced;
			return this;
		}
		
		/**
		 * @param discontinued
		 * @return this
		 */
		public Builder discontinued(LocalDate discontinued) {
			this.discontinued = discontinued;
			return this;
		}
		
		/**
		 * @param company
		 * @return this
		 */
		public Builder company(Company company) {
			this.company = company;
			return this;
		}
		
	}

	
}
