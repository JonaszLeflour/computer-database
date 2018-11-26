package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dto.InvalidPageNumberException;
import com.excilys.cdb.dto.OrderedComputerRequestPager;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.ComputerService;

/**
 * @author Jonasz Leflour
 *
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {
    @Autowired
    private OrderedComputerRequestPager orderedComputerRequestPager;
    
    @Autowired
    private ComputerService simpleComputerService;
    /**
     * @param map
     * @param search 
     * @param page
     * @return dashboard
     * @throws InvalidParameterException 
     */
    @GetMapping
    public String getRequest(ModelMap map,
    		@RequestParam(required=false, value="search",defaultValue="")String search,
    		@RequestParam(required=false, value="page", defaultValue="1")String page) throws InvalidParameterException {
    	long currentPage = Long.parseLong(page);
    	orderedComputerRequestPager.name(search);
    	
    	try {
			orderedComputerRequestPager.getPage(Long.parseLong(page)-1);
		} catch (NumberFormatException | DatabaseErrorException | InvalidPageNumberException e) {
			throw new InvalidParameterException(e);
		}
    	try {
			map.addAttribute("nbcomputers", orderedComputerRequestPager.getNbComputers());
			map.addAttribute("page", orderedComputerRequestPager.getPage(currentPage-1));
			map.addAttribute("currentPage", currentPage);
			map.addAttribute("nbPages", orderedComputerRequestPager.getNbPages());
    	} catch (DatabaseErrorException | InvalidPageNumberException e) {
    		throw new InvalidParameterException(e);
		}
    	return "dashboard";
    }
    
    /**
     * @param map
     * @param search 
     * @param page 
     * @param deleteComputers
     * @return dashboard
     * @throws InvalidParameterException 
     */
    @PostMapping
    public String postRequest(ModelMap map,
    		@RequestParam(required=false, value="search",defaultValue="")String search,
    		@RequestParam(required=false, value="page", defaultValue="1")String page,
    		@RequestParam(required=true, value="deletecomputers[]")String[] deleteComputers) throws InvalidParameterException{
    	for(String idString : deleteComputers) {
			long id = Long.parseLong(idString);
			try {
				simpleComputerService.deleteComputerById(id);
			} catch (DatabaseErrorException | ObjectNotFoundException | InvalidParameterException e) {
				throw new InvalidParameterException(e);
			}
		}
		return getRequest(map,search,page);
    	
    }
}
