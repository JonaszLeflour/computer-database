package com.excilys.cdb.dto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
/**
 * @author Jonasz Leflour
 *
 */
public class ComputerDTOMapper{

	/**
	 * @param c
	 * @return DTO object version of c
	 */
	public static DTOComputer toDTOComputer(Computer c) {
		DTOComputer dtoC = new DTOComputer();
		dtoC.setId(String.valueOf(c.getId()));
		dtoC.setName(c.getName() != null ? c.getName() : "");
		dtoC.setIntroduced(c.getIntroduced() != null ? c.getIntroduced().toString() : "");
		dtoC.setDiscontinued(c.getDiscontinued() != null ? c.getDiscontinued().toString() : "");
		dtoC.setCompany(c.getCompany() != null ? c.getCompany().getName().toString() : "");
		dtoC.setCompanyId(c.getCompany() != null ? String.valueOf(c.getCompany().getId()) : "");
		return dtoC;
	}
	
	/**
	 * @param computerList
	 * @return list of DTO computers
	 */
	public static List<DTOComputer> toDTOComputer(List<Computer> computerList) {
		List<DTOComputer> list = new ArrayList<DTOComputer>();
		for(Computer c : computerList) {
			list.add(toDTOComputer(c));
		}
		return list;
	}
	
	/**
	 * @param dtoC
	 * @return mapped computer object
	 */
	public static Computer toComputer(DTOComputer dtoC) {
		Computer c = new Computer();
		c.setId(Integer.parseInt(dtoC.getId()));
		c.setName(dtoC.getName());
		c.setIntroduced(LocalDate.parse(dtoC.getIntroduced()));
		c.setDiscontinued(LocalDate.parse(dtoC.getDiscontinued()));
		c.setCompany(new Company(Company.getBuilder()
				.id(Long.parseLong(dtoC.getCompanyId()))
				.name(dtoC.getCompany())));
		return c;
	}
	
	/**
	 * @param computerList
	 * @return computer list
	 */
	public static List<Computer> toComputer(List<DTOComputer> computerList) {
		List<Computer> list = new ArrayList<Computer>();
		for(DTOComputer c : computerList) {
			list.add(toComputer(c));
		}
		return list;
	}
}
