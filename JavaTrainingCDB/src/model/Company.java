package model;
import java.io.Serializable;


/**
 * @author root
 * @version %I%
 * 
 */
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	//not null unique
	private int id;
	
	private String name;

	/**
	 * @param id company id in database
	 * @param name company name
	 */
	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "{id="+id+", name="+name+"}";
	}
	
	/**
	 * @return id of company in database
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id of company in database
	 */
	public void setId(int id) {
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
}
