package com.excilys.cdb.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
//@Config(entityAccessors=true)
@Entity
@Table(name = "company", schema = "computer-database-db")
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", nullable=false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private long id;
	
	@Column(name="name")
	private String name;

	
	/**
	 * 
	 */
	public Company() {
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
	
	/**
	 * @param b
	 */
	public Company(Builder b) {
		this.id = b.id;
		this.name = b.name;
	}
	
	/**
	 * @return instance of the builder of this class
	 */
	public static Builder getBuilder() {
		return new Builder();
	}
	
	/**
	 * @author Jonasz Leflour
	 *
	 */
	public static class Builder{
		private long id;
		private String name;
		/**
		 * @param id
		 * @return reference to this builder
		 */
		public Builder id(long id) {
			this.id = id;
			return this;
		}
		/**
		 * @param name
		 * @return reference to this builder
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
	}
	
}
