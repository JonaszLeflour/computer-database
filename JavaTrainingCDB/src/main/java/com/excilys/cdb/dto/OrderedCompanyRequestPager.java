package com.excilys.cdb.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.CompanyDAO.CompanyField;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.SimpleCompanyService;

/**
 * @author Jonasz Leflour
 *
 */
public class OrderedCompanyRequestPager implements CompanyRequestPager {
	CompanyService service;
	String searchName;
	long pageSize;
	CompanyField orderBy;
	OrderDirection direction;

	/**
	 * @param name
	 * @param pageSize
	 * @param orderBy
	 * @param direction
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws DatabaseErrorException
	 * @throws InvalidPageSizeException
	 */
	public OrderedCompanyRequestPager(String name, long pageSize, CompanyField orderBy, OrderDirection direction)
			throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException,
			InvalidPageSizeException {

		if (pageSize <= 0) {
			throw new InvalidPageSizeException("Page size must be strictly positive");
		}

		service = new SimpleCompanyService();
		this.searchName = name;
		this.pageSize = pageSize;
		this.orderBy = orderBy;
		this.direction = direction;
	}
	
	
	@Override
	public List<DTOCompany> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException {
		if (pageNumber < 0) {
			throw new InvalidPageNumberException("Page number must be positive");
		}
		List<DTOCompany> list = new ArrayList<DTOCompany>();
		for (Company c : service.getOrderedCompaniesByName(searchName, pageNumber * pageSize, pageSize, orderBy,
				direction)) {
			list.add(CompanyDTOMapper.toDTOCompany(c));
		}
		if (list.isEmpty()) {
			throw new InvalidPageNumberException("This page doesn't exist");
		}
		return list;
	}

	@Override
	public long getNbPages() throws DatabaseErrorException {
		return (long) Math.ceil(service.countCompaniesByName(searchName) / ((double) pageSize));
	}

	@Override
	public long getNbComputers() throws DatabaseErrorException {
		return service.countCompaniesByName(searchName);
	}

}
