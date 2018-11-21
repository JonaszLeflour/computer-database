package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
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
			long id = rs.getInt(1);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			String name = rs.getString(2);
			if (rs.wasNull()) {
				throw new EmptyResultSetException();
			}
			return new Company(Company.getBuilder()
					.id(id)
					.name(name));
		} catch (SQLException e) {
			throw new EmptyResultSetException();
		}
	}
	
	RowMapper<Company> getRowMapper(){
		return new RowMapper<Company>() {
			public Company mapRow(ResultSet result, int pRowNum) throws SQLException {
				try {
					return createCompanyWithResultSetRow(result);
				} catch (EmptyResultSetException e) {
					throw new SQLException(e);
				}
			};
		};
	}
}
