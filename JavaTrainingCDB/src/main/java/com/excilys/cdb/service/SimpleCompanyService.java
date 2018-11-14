package com.excilys.cdb.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.persistence.OrderDirection;

/**
 * @author JonaszLeflour
 *
 */
@Service
public class SimpleCompanyService implements CompanyService {
	
	@Autowired
	private CompanyDAO companyDAO;
	
	@Override
	public List<Company> getCompanies() throws DatabaseErrorException {
		List<Company> result = companyDAO.getAllCompanies();
		return result;
	}
	
	@Override
	public long countCompaniesByName(String name) throws DatabaseErrorException {
		return companyDAO.countCompaniesByName(name);
	}

	@Override
	public Company getCompanyById(int id) throws ObjectNotFoundException, DatabaseErrorException {
		return companyDAO.getCompanybyId(id);
	}
	

	@Override
	public void deleteCompanyById(long id)
			throws DatabaseErrorException, ObjectNotFoundException, InvalidParameterException {
		companyDAO.deleteCompanyById(id);

	}
	
	@Override
	public List<Company> getOrderedCompaniesByName(String name, long offset, long lenght,
			CompanyDAO.CompanyField orderBy, OrderDirection direction)
			throws DatabaseErrorException {
		return companyDAO.getOrderedCompanies(name, offset, lenght, orderBy, direction);
	}

}
