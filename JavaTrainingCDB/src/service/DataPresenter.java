package service;
import model.Computer;
import persistence.DatabaseAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Company;

public class DataPresenter {
	private DatabaseAccessor dba = new DatabaseAccessor();
	
	public List<Computer> getComputers(){
		List<Computer> result = new ArrayList<Computer>();
		ResultSet rs = dba.getAllComputers();
		try {
			while(rs.next()) {
				Computer computer = new Computer();
				
				computer.setId(rs.getInt(1)); 
				if (rs.wasNull()) {computer.setId(0);}
				computer.setName(rs.getString(2)); 
				if (rs.wasNull()) {computer.setName(null);}
				
				java.sql.Date introduced = rs.getDate(3);
				if (rs.wasNull()) {
					computer.setIntroduced(null);
				}
				else {
					computer.setIntroduced(introduced);
				}
				
				java.sql.Date discontinued = rs.getDate(4);
				if (rs.wasNull()) {
					computer.setDiscontinued(null);
				}
				else {
					computer.setDiscontinued(discontinued);
				}
				
				Integer idCompany = rs.getInt(5);
				if (rs.wasNull()) {
					computer.setCompany(null);
				}
				else {
					computer.setCompany(getCompanyById(idCompany));
				}
				result.add(computer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
}
