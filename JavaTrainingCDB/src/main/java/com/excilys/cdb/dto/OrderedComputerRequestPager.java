package com.excilys.cdb.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.*;
import com.excilys.cdb.persistence.ComputerDAO.ComputerField;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.SimpleComputerService;

/**
 * @author Jonasz Leflour
 *
 */
public class OrderedComputerRequestPager implements ComputerRequestPager {
	ComputerService dp;
	String searchName;
	long pageSize;
	ComputerField orderBy;
	OrderDirection direction;

	/**
	 * @param name
	 * @param pageSize
	 * @param orderBy
	 * @param direction
	 * @throws DatabaseErrorException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 * @throws InvalidPageSizeException
	 * 
	 */
	public OrderedComputerRequestPager(String name, long pageSize, ComputerField orderBy, OrderDirection direction)
			throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException,
			InvalidPageSizeException {

		if (pageSize <= 0) {
			throw new InvalidPageSizeException("Page size must be strictly positive");
		}

		dp = new SimpleComputerService();
		this.searchName = name;
		this.pageSize = pageSize;
		this.orderBy = orderBy;
		this.direction = direction;
	}

	@Override
	public List<DTOComputer> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException {
		if (pageNumber < 0) {
			throw new InvalidPageNumberException("Page number must be positive");
		}
		List<DTOComputer> list = new ArrayList<DTOComputer>();
		for (Computer c : dp.getOrderedComputersByName(searchName, pageNumber * pageSize, pageSize, orderBy,
				direction)) {
			list.add(ComputerDTOMapper.toDTOComputer(c));
		}
		if (list.isEmpty()) {
			throw new InvalidPageNumberException("This page doesn't exist");
		}
		return list;
	}

	@Override
	public long getNbPages() throws DatabaseErrorException {
		return (long) Math.ceil(dp.countComputersByName(searchName) / ((double) pageSize));
	}

	@Override
	public long getNbComputers() throws DatabaseErrorException {
		return dp.countComputersByName(searchName);
	}

}
