package com.excilys.cdb.dto;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.*;
import com.excilys.cdb.persistence.ComputerDAO.ComputerField;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.service.ComputerService;

/**
 * @author Jonasz Leflour
 *
 */
@Component
public class OrderedComputerRequestPager {
	@Autowired
	private ComputerService simpleComputerService;
	private String searchName = "";
	private long pageSize = 10;
	private ComputerField orderBy = ComputerField.id;
	private OrderDirection direction = OrderDirection.DESC;
	
	/**
	 * @param pageSize
	 * @return instance of this
	 * @throws InvalidPageSizeException 
	 */
	public OrderedComputerRequestPager pageSize(long pageSize) throws InvalidPageSizeException{
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
	public OrderedComputerRequestPager orderBy(ComputerField orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	/**
	 * @param name
	 * @return instance of this
	 */
	public OrderedComputerRequestPager name(String name) {
		this.searchName = name;
		return this;
	}
	
	/**
	 * @param direction
	 * @return OrderedComputerRequestPager
	 */
	public OrderedComputerRequestPager direction(OrderDirection direction) {
		this.direction = direction;
		return this;
	}
	
	/**
	 * @param pageNumber
	 * @return page of computers
	 * @throws DatabaseErrorException
	 * @throws InvalidPageNumberException
	 */
	public List<DTOComputer> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException {
		if (pageNumber < 0) {
			throw new InvalidPageNumberException("Page number must be positive");
		}
		List<DTOComputer> list = new ArrayList<DTOComputer>();
		for (Computer c : simpleComputerService.getOrderedComputersByName(searchName, pageNumber * pageSize, pageSize, orderBy,
				direction)) {
			list.add(ComputerDTOMapper.toDTOComputer(c));
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
		return (long) Math.ceil(simpleComputerService.countComputersByName(searchName) / ((double) pageSize));
	}

	/**
	 * @return number of computers
	 * @throws DatabaseErrorException
	 */
	public long getNbComputers() throws DatabaseErrorException {
		return simpleComputerService.countComputersByName(searchName);
	}

}
