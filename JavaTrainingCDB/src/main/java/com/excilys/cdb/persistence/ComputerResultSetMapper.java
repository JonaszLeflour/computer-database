package com.excilys.cdb.persistence;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;

/**
 * @author Jonasz Lefour
 *
 */
@Component
public class ComputerResultSetMapper {
	@Autowired
	CompanyDAO companyDAO;
	/**
	 * @param rs resulset with index on the description of a computer
	 * @return Computer
	 * @throws EmptyResultSetException
	 * @throws DatabaseErrorException
	 */
	public Computer createComputerWithResultSetRow(ResultSet rs)
			throws EmptyResultSetException, DatabaseErrorException {
		Computer computer = new Computer();
		try {
			long id = rs.getLong(1);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			computer.setId(id);

			String name = rs.getString(2);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			computer.setName(name);

			Date introduced = rs.getDate(3);
			if (!rs.wasNull()) {
				computer.setIntroduced(introduced.toLocalDate());
			}

			Date discontinued = rs.getDate(4);
			if (!rs.wasNull()) {
				computer.setDiscontinued(discontinued.toLocalDate());
			}

			Integer idCompany = rs.getInt(5);
			if (!rs.wasNull()) {
				computer.setCompany(companyDAO.getCompanybyId(idCompany));
			}
		} catch (SQLException e) {
			new EmptyResultSetException(e);
		} catch (ObjectNotFoundException e) {
			computer.setCompany(null);
		}
		return computer;
	}
	RowMapper<Computer> getRowMapper(){
		return new RowMapper<Computer>() {
			public Computer mapRow(ResultSet result, int pRowNum) throws SQLException {
				try {
					return createComputerWithResultSetRow(result);
				} catch (EmptyResultSetException | DatabaseErrorException e) {
					throw new SQLException(e);
				}
			};
		};
	}
		
}
