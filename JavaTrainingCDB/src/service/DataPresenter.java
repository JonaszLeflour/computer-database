package service;
import model.Computer;
import persistence.DatabaseAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import model.Company;

public class DataPresenter {
	private DatabaseAccessor dba = new DatabaseAccessor();
	
	
	
	
	public List<Computer> getComputerList(){
		List<Computer> result = new ArrayList<Computer>();
		ResultSet rs = dba.getAllComputers();
		try {
			while(rs.next()) {
				System.out.println(rs.getRow());
				Computer computer = new Computer();
				computer.setId(rs.getInt(1));
				computer.setName(rs.getString(2));; 
				computer.setIntroduced(rs.getDate(3)
						.toInstant()
						.atZone(ZoneId.of("CET"))
						.toLocalDateTime());
				computer.setDiscontinued((rs.getDate(4).toInstant().atZone(ZoneId.of("CET")).toLocalDateTime()));
				computer.setCompany(getCompanyById(rs.getInt(5)));
				result.add(computer);
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
