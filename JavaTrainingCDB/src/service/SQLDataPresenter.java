
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
import model.ComputerField;

/**
 * 
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
public class SQLDataPresenter implements DataPresenter{
	private DatabaseAccessor dba = new DatabaseAccessor();

	private Computer createComputerWithResultSetRow(ResultSet rs) {
		Computer computer = new Computer();

		try {
			computer.setId(rs.getInt(1));
			if (rs.wasNull()) {
				computer.setId(0);
			}
			computer.setName(rs.getString(2));
			if (rs.wasNull()) {
				computer.setName(null);
			}

			java.sql.Date introduced = rs.getDate(3);
			if (rs.wasNull()) {
				computer.setIntroduced(null);
			} else {
				computer.setIntroduced(introduced.toLocalDate());
			}

			java.sql.Date discontinued = rs.getDate(4);
			if (rs.wasNull()) {
				computer.setDiscontinued(null);
			} else {
				computer.setDiscontinued(discontinued.toLocalDate());
			}

			Integer idCompany = rs.getInt(5);
			if (rs.wasNull()) {
				computer.setCompany(null);
			} else {
				computer.setCompany(getCompanyById(idCompany));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return computer;

	}

	@Override
	public List<Computer> getComputers() {
		List<Computer> result = new ArrayList<Computer>();
		ResultSet rs = dba.getAllComputers();
		try {
			while (rs.next()) {
				result.add(createComputerWithResultSetRow(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
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

	@Override
	public List<Computer> getComputersByName(String name) {
		List<Computer> result = new ArrayList<Computer>();
		ResultSet rs = dba.getComputerByName(name);
		try {
			while (rs.next()) {
				result.add(createComputerWithResultSetRow(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Company> getCompanies() {
		List<Company> result = new ArrayList<Company>();
		ResultSet rs = dba.getAllCompanies();
		try {
			while (rs.next()) {
				Company company = new Company(rs.getInt(1), rs.getString(2));
				result.add(company);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public Company getCompanyById(int id) {
		ResultSet rs = dba.getCompanybyId(id);
		try {
			rs.next();
			return new Company(rs.getInt(1), rs.getString(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateComputerById(int id, Map<ComputerField, String> updatedFields) {
		dba.updateComputerById(id, updatedFields);
	}

	@Override
	public void addComputer(Map<ComputerField, String> updatedFields) {
		dba.createComputer(updatedFields);
	}

	@Override
	public void addComputer(Computer computer) {
		Map<ComputerField, String> fields = new EnumMap<ComputerField, String>(ComputerField.class);

		if (computer.getCompany() != null) {
			fields.put(ComputerField.company_id, String.valueOf(computer.getCompany().getId()));
		}
		if (computer.getIntroduced() != null) {
			fields.put(ComputerField.introduced, computer.getIntroduced().toString());
		}
		if (computer.getDiscontinued() != null) {
			fields.put(ComputerField.discontinued, computer.getDiscontinued().toString());
		}
		if ((computer.getName() != null) && (!computer.getName().isEmpty())) {
			fields.put(ComputerField.name, computer.getName());
		}

		addComputer(fields);
	}

	@Override
	public void removeComputerById(int id) {
		dba.deleteComputerById(id);
	}
	
	@Override
	public void removeComputersByName(String name) {
		dba.deleteComputerByName(name);
	}
}
