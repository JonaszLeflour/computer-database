package service;
import model.Computer;
import persistence.DatabaseAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import model.Company;

public class DataPresenter {
	private DatabaseAccessor dba = new DatabaseAccessor();
	
	
	private Computer createComputerWithResultSetRow(ResultSet rs) {
		Computer computer = new Computer();
		
		try {
			computer.setId(rs.getInt(1));
			if (rs.wasNull()) {computer.setId(0);}
			computer.setName(rs.getString(2)); 
			if (rs.wasNull()) {computer.setName(null);}
			
			java.sql.Date introduced = rs.getDate(3);
			if (rs.wasNull()) {
				computer.setIntroduced(null);
			}
			else {
				computer.setIntroduced(introduced.toLocalDate());
			}
			
			java.sql.Date discontinued = rs.getDate(4);
			if (rs.wasNull()) {
				computer.setDiscontinued(null);
			}
			else {
				computer.setDiscontinued(discontinued.toLocalDate());
			}
			
			Integer idCompany = rs.getInt(5);
			if (rs.wasNull()) {
				computer.setCompany(null);
			}
			else {
				computer.setCompany(getCompanyById(idCompany));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return computer;
		
	}
	
	public List<Computer> getComputers(){
		List<Computer> result = new ArrayList<Computer>();
		ResultSet rs = dba.getAllComputers();
		try {
			while(rs.next()) {
				result.add(createComputerWithResultSetRow(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public Computer getComputerById(int id) {
		ResultSet rs = dba.getComputerById(id);
		try {
			rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return createComputerWithResultSetRow(rs);
	}
	
	public List<Computer> getComputersByName(String name) {
		List<Computer> result = new ArrayList<Computer>();
		ResultSet rs = dba.getComputerByName(name);
		try {
			while(rs.next()) {
				result.add(createComputerWithResultSetRow(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Company> getCompanies(){
		List<Company> result = new ArrayList<Company>();
		ResultSet rs = dba.getAllCompanies();
		try {
			while(rs.next()) {
				Company company = new Company(rs.getInt(1),rs.getString(2));
				result.add(company);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public Company getCompanyById(int id) {
		ResultSet rs = dba.getCompanybyId(id);
		try {
			rs.next();
			return new Company(rs.getInt(1),rs.getString(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateComputerById(int id, Map<DatabaseAccessor.ComputerField,String> updatedFields) {
		dba.updateComputerById(id, updatedFields);
	}
	
	public void addComputer(Map<DatabaseAccessor.ComputerField,String> updatedFields) {
		dba.createComputer(updatedFields);
	}
	
	public void addComputer(Computer computer) {
		Map<DatabaseAccessor.ComputerField,String> fields 
			= new EnumMap<DatabaseAccessor.ComputerField,String>(DatabaseAccessor.ComputerField.class);
		
		if(computer.getCompany() != null){fields.put(DatabaseAccessor.ComputerField.company_id, String.valueOf(computer.getCompany().getId()));}
		if(computer.getIntroduced() != null){fields.put(DatabaseAccessor.ComputerField.introduced, computer.getIntroduced().toString());}
		if(computer.getDiscontinued() != null){fields.put(DatabaseAccessor.ComputerField.discontinued, computer.getDiscontinued().toString());}
		if((computer.getName() != null)&&(!computer.getName().isEmpty()) ){fields.put(DatabaseAccessor.ComputerField.name, computer.getName());}
		
		
		addComputer(fields);
	}
	
	public void removeComputeById(int id) {
		dba.deleteComputerById(id);
	}
	
	public void removeComputeByName(String name) {
		dba.deleteComputerByName(name);
	}
}
