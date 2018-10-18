package model;

/**
 * @author Jonasz Leflour
 * @version %I%
 */
public enum ComputerField {
	/**
	 * String of the computer name, can't be null
	 */
	name, 
	/**
	 * Date of introduction
	 */
	introduced, 
	/**
	 * Date of decommission
	 */
	discontinued, 
	/**
	 * int of company id foreign key
	 */
	company_id
}
