package service;

import java.util.List;
import java.util.Map;

import model.Company;
import model.Computer;
import model.ComputerField;

/**
 * Presents data as list of objects and issues request to change the databse
 * @author Jonasz Leflour
 *
 */
public interface DataPresenter {
	/**
	 * @return all computers
	 */
	public List<Computer> getComputers();

	/**
	 * @param id
	 * @return single computer with the specified id, or null
	 */
	public Computer getComputerById(int id);

	/**
	 * @param name
	 * @return all computers sharing the specified name
	 */
	public List<Computer> getComputersByName(String name);

	/**
	 * @return all companies
	 */
	public List<Company> getCompanies();

	/**
	 * @param id
	 * @return single company with the specified id, or null
	 */
	public Company getCompanyById(int id);

	/**
	 * @param id id of the computer to update
	 * @param updatedFields fields to update with their new value
	 */
	public void updateComputerById(int id, Map<ComputerField, String> updatedFields);

	/**
	 * @param updatedFields fields of the added computer
	 */
	public void addComputer(Map<ComputerField, String> updatedFields);

	/**
	 * @param computer data of the computer to add
	 */
	public void addComputer(Computer computer);

	/**
	 * @param id of the computer to remove from database
	 */
	public void removeComputerById(int id);

	/**
	 * @param name name shared by all computer to remove
	 */
	public void removeComputersByName(String name);
}
