package com.excilys.cdb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role", schema = "computer-database-db")
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id", nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="name", nullable=false)
	private String name;
	@Column(name="select", nullable=false)
	private boolean select;
	@Column(name="edit", nullable=false)
	private boolean edit;
	@Column(name="insert", nullable=false)
	private boolean insert;
	@Column(name="delete", nullable=false)
	private boolean delete;
	@Column(name="create", nullable=false)
	private boolean create;
	@Column(name="drop", nullable=false)
	private boolean drop;
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
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	public boolean isInsert() {
		return insert;
	}
	public void setInsert(boolean insert) {
		this.insert = insert;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public boolean isCreate() {
		return create;
	}
	public void setCreate(boolean create) {
		this.create = create;
	}
	public boolean isDrop() {
		return drop;
	}
	public void setDrop(boolean drop) {
		this.drop = drop;
	}
	
	@SuppressWarnings("all")
	private static class Builder {
		private long id = '0';
		private String name = null;
		private boolean select = false;
		private boolean edit = false;
		private boolean insert = false;
		private boolean delete = false;
		private boolean create = false;
		private boolean drop = false;
		
		public Builder id(long id) {
			this.id = id;
			return this;
		}
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder select(boolean select) {
			this.select = select;
			return this;
		}
		public Builder edit(boolean edit) {
			this.edit = edit;
			return this;
		}
		public Builder insert(boolean insert) {
			this.insert = insert;
			return this;
		}
		public Builder delete(boolean delete) {
			this.delete = delete;
			return this;
		}
		public Builder create(boolean create) {
			this.create = create;
			return this;
		}
		public Builder drop(boolean drop) {
			this.drop = drop;
			return this;
		}
	}
	
	public static Builder getBuilder() {
		return new Builder();
	}
	
	public Role(){
		
	}
	
	Role(Builder b){
		id = b.id;
		name = b.name;
		select = b.select;
		edit = b.edit;
		insert = b.insert;
		delete = b.delete;
		create = b.create;
		drop = b.drop;
	}
	
	

}
