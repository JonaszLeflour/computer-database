package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

/**
 * @author Jonasz Leflour
 *
 */
@Component
public class CompanyResultSetMapper {
	/**
	 * @param rs
	 * @return company
	 * @throws EmptyResultSetException
	 */
	public Company createCompanyWithResultSetRow(ResultSet rs) throws EmptyResultSetException {
		try {
			int id = rs.getInt(1);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			String name = rs.getString(2);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			return new Company(id, name);
		} catch (SQLException e) {
			throw new EmptyResultSetException();
		}
	}
}
