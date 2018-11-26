package com.excilys.cdb.dto;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.CompanyDAO.CompanyField;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.service.CompanyService;

/**
 * @author Jonasz Leflour
 *
 */
@Component
public class OrderedCompanyRequestPager{
	@Autowired
	private CompanyService service;
	private String searchName;
	private long pageSize;
	private CompanyField orderBy;
	private OrderDirection direction;
	/**
	 * @param pageSize
	 * @return instance of this
	 * @throws InvalidPageSizeException 
	 */
	public OrderedCompanyRequestPager pageSize(long pageSize) throws InvalidPageSizeException{
		if(pageSize<=0) {
			throw new InvalidPageSizeException("Must have a positive page size");
		}
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * @param orderBy
	 * @return instance of this
	 */
	public OrderedCompanyRequestPager orderBy(CompanyField orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	/**
	 * @param name
	 * @return instance of this
	 */
	public OrderedCompanyRequestPager name(String name) {
		this.searchName = name;
		return this;
	}
	
	/**
	 * @param direction
	 * @return OrderedComputerRequestPager
	 */
	public OrderedCompanyRequestPager direction(OrderDirection direction) {
		this.direction = direction;
		return this;
	}
	
	
	/**
	 * @param pageNumber
	 * @return page of companies
	 * @throws DatabaseErrorException
	 * @throws InvalidPageNumberException
	 */
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

	/**
	 * @return number of pages
	 * @throws DatabaseErrorException
	 */
	public long getNbPages() throws DatabaseErrorException {
		return (long) Math.ceil(service.countCompaniesByName(searchName) / ((double) pageSize));
	}

	/**
	 * @return number
	 * @throws DatabaseErrorException
	 */
	public long getNbCompanies() throws DatabaseErrorException {
		return service.countCompaniesByName(searchName);
	}

}
