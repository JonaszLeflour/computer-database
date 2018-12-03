package com.excilys.cdb.webservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.excilys.cdb.dto.ComputerDTOMapper;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.dto.OrderedComputerRequestPager;
import com.excilys.cdb.persistence.ComputerDAO.ComputerField;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.persistence.OrderDirection;
import com.excilys.cdb.service.ComputerService;

@RestController("computerController")
@RequestMapping("/webservice/computer")
public class ComputerWebSeviceController {
	@Autowired
	ComputerService service;
	@Autowired
	OrderedComputerRequestPager pager;

	@GetMapping({"/findbyname","/findbyname/{name}"})
	public ResponseEntity<List<DTOComputer>> findByName(@PathVariable("name")Optional<String> name,
			@RequestParam(required = false, defaultValue = "1") String page,
			@RequestParam(required = false, defaultValue = "10") String pageSize,
			@RequestParam(required = false, defaultValue = "true") String usePage,
			@RequestParam(required = false, defaultValue = "id") String orderBy,
			@RequestParam(required = false, defaultValue = "ASC") String orderDirection){
		String search = name.isPresent()?name.get():"";
		if (Boolean.parseBoolean(usePage)) {
			
			try {
				return new ResponseEntity<>(pager.name(search)
					.pageSize(Long.parseLong(pageSize))
					.orderBy(ComputerField.valueOf(orderBy))
					.direction(OrderDirection.valueOf(orderDirection))
					.getPage(Long.parseLong(page)),HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}else {// !userPage
			try {
				return new ResponseEntity<>(ComputerDTOMapper.toDTOComputer(service.getComputersByName(search))
						,HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping({ "/countbyname", "/countbyname/{name}" })
	public ResponseEntity<Long> countByName(@PathVariable("name")Optional<String> name,
			@RequestParam(required = false, defaultValue = "1") String page,
			@RequestParam(required = false, defaultValue = "10") String pageSize,
			@RequestParam(required = false, defaultValue = "true") String usePage){
		String search = name.isPresent()?name.get():"";
		if (Boolean.parseBoolean(usePage)) {
			
			try {
				return new ResponseEntity<>(pager.name(search)
					.pageSize(Long.parseLong(pageSize))
					.getNbComputers(),HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}else {// !userPage
			try {
				return new ResponseEntity<>(service.countComputersByName(search)
						,HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> update(@RequestBody DTOComputer computer){
		try {
			service.updateComputer(ComputerDTOMapper.toComputer(computer));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@DeleteMapping("deletebyid/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") String id){
			try {
				service.deleteComputerById(Long.parseLong(id));
			}catch(ObjectNotFoundException e) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			catch (NumberFormatException | DatabaseErrorException
					| InvalidParameterException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
