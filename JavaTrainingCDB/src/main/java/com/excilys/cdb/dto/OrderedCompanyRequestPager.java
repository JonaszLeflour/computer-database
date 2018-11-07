package com.excilys.cdb.dto;

import java.util.List;

import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * @author Jonasz Leflour
 *
 */
public class OrderedCompanyRequestPager implements CompanyRequestPager {
	String searchName;
	
	
	@Override
	public List<DTOCompany> getPage(long pageNumber) throws DatabaseErrorException, InvalidPageNumberException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getNbPages() throws DatabaseErrorException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNbComputers() throws DatabaseErrorException {
		// TODO Auto-generated method stub
		return 0;
	}

}
