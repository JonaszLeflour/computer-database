package com.excilys.cdb.dto;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;

/**
 * @author Jonasz Leflour
 *
 */
public class CompanyDTOMapper {

	/**
	 * @param c
	 * @return DTO object version of c
	 */
	public static DTOCompany toDTOCompany(Company c) {
		DTOCompany daoC = new DTOCompany();
		daoC.setId(String.valueOf(c.getId()));
		daoC.setName(c.getName() != null ? c.getName() : "");
		return daoC;
	}
	
	/**
	 * @param companyList
	 * @return company dto list
	 */
	public static List<DTOCompany> toDTOCompany(List<Company> companyList) {
		List<DTOCompany> list = new ArrayList<DTOCompany>();
		for(Company c : companyList) {
			list.add(toDTOCompany(c));
		}
		return list;
	}
	
	/**
	 * @param dtoC
	 * @return company
	 */
	public static Company toCompany(DTOCompany dtoC) {
		
		return new Company(Company.getBuilder()
				.id(Long.parseLong(dtoC.getId()))
				.name(dtoC.getName()));
		
	}
	
	/**
	 * @param companyList
	 * @return company list
	 */
	public static List<Company> toCompany(List<DTOCompany> companyList) {
		List<Company> list = new ArrayList<Company>();
		for(DTOCompany c : companyList) {
			list.add(toCompany(c));
		}
		return list;
	}

}
