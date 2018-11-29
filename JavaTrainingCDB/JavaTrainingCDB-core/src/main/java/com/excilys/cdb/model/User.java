package com.excilys.cdb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "computer-database-db")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "password", nullable = false)
	private String password;
	@ManyToOne
    @JoinColumn(name = "id_role")
	private Role role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@SuppressWarnings("unused")
	private static class Builder {
		private long id = '0';
		private String name = null;
		private String password = null;

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}
	}

	public static Builder getBuilder() {
		return new Builder();
	}

	public User() {

	}

	public User(Builder b) {
		id = b.id;
		name = b.name;
		password = b.password;
	}
}
