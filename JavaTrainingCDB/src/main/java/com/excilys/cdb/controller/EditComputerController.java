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
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * @author Jonasz Leflour
 *
 */
@Controller
@RequestMapping("editcomputer")
public class EditComputerController {
	@Autowired
	private ComputerService simpleComputerService;
	@Autowired
	private CompanyService simpleCompanyService;

	/**
	 * @param map
	 * @param idParam
	 * @return editComputer
	 * @throws InvalidParameterException
	 */
	@GetMapping
	public String getRequest(ModelMap map, @RequestParam(required = true, value = "id") String idParam)
			throws InvalidParameterException {
		int id;
		Computer computer;
		id = Integer.parseInt(idParam);
		try {
			computer = simpleComputerService.getComputerById(id);
			map.addAttribute("computer", computer);
			map.addAttribute("companies", CompanyDTOMapper.toDTOCompany(simpleCompanyService.getCompanies()));
		} catch (DatabaseErrorException | ObjectNotFoundException e) {
			throw new InvalidParameterException(e);
		}
		return "editComputer";
	}

	/**
	 * @param map 
	 * @param id 
	 * @param name 
	 * @param introduced 
	 * @param discontinued 
	 * @param companyId 
	 * @return editcomputer
	 * @throws InvalidParameterException 
	 */
	@PostMapping
	public String postRequest(ModelMap map, @RequestParam(required = true, value = "id") String id,
			@RequestParam(required = false, value = "computername", defaultValue = "") String name,
			@RequestParam(required = false, value = "introduced", defaultValue = "") String introduced,
			@RequestParam(required = false, value = "discontinued", defaultValue = "") String discontinued,
			@RequestParam(required = false, value = "companyId", defaultValue = "") String companyId) throws InvalidParameterException {

		DTOComputer dtoC = new DTOComputer();
		dtoC.setId(id);
		dtoC.setName(name);
		dtoC.setIntroduced(introduced);
		dtoC.setDiscontinued(discontinued);
		dtoC.setCompanyId(companyId);

		Computer computer = ComputerDTOMapper.toComputer(dtoC);
		try {
			simpleComputerService.updateComputer(computer);
		} catch (com.excilys.cdb.persistence.InvalidParameterException | DatabaseErrorException
				| ObjectNotFoundException e) {
			throw new InvalidParameterException(e);
		}
		return "dashboard";
	}

}
