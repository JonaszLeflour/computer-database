package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dto.CompanyDTOMapper;
import com.excilys.cdb.dto.ComputerDTOMapper;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * @author Jonasz Leflour
 *
 */
@Controller
@RequestMapping("addcomputer")
public class AddComputerController {
	@Autowired
	private ComputerService simpleComputerService;
	@Autowired
	private CompanyService simpleCompanyService;

	/**
	 * @param map
	 * @return addcomputer
	 * @throws DatabaseErrorException
	 */
	@GetMapping
	public String getRequest(ModelMap map) throws DatabaseErrorException {
		map.addAttribute("companies", CompanyDTOMapper.toDTOCompany(simpleCompanyService.getCompanies()));
		return "addComputer";
	}

	/**
	 * @param map
	 * @param name
	 * @param introduced
	 * @param discontinued
	 * @param companyId
	 * @return dashboard
	 * @throws InvalidParameterException 
	 */
	@PostMapping
	public String postRequest(ModelMap map,
			@RequestParam(required = false, value = "computername", defaultValue = "") String name,
			@RequestParam(required = false, value = "introduced", defaultValue = "") String introduced,
			@RequestParam(required = false, value = "discontinued", defaultValue = "") String discontinued,
			@RequestParam(required = false, value = "companyId", defaultValue = "") String companyId) throws InvalidParameterException {
		
		DTOComputer dtoC = new DTOComputer();
		dtoC.setName(name);
		dtoC.setIntroduced(introduced);
		dtoC.setDiscontinued(discontinued);
		dtoC.setCompanyId(companyId);
		Computer newComputer = ComputerDTOMapper.toComputer(dtoC);
		try {
			simpleComputerService.updateComputer(newComputer);
		} catch (InvalidParameterException | DatabaseErrorException | ObjectNotFoundException e) {
			throw new InvalidParameterException(e);
		}
		return "dashboard";

	}

}
