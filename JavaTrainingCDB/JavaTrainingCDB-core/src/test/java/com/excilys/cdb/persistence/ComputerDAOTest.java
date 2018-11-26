package com.excilys.cdb.persistence;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.RootConfig;
import com.excilys.cdb.model.*;
import com.excilys.cdb.persistence.InvalidParameterException;

/**
 * @author Jonasz Leflour
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {RootConfig.class})
public class ComputerDAOTest {
	@Autowired
	ComputerDAO computerDAO;
	@Autowired
	CompanyDAO companyDAO;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		computerDAO = null;
		companyDAO = null;
	}
	
	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetAllComputers() throws DatabaseErrorException {
		List<Computer> computers = computerDAO.getAllComputers();
		assertNotNull(computers);
	}

	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetAllComputers2() throws DatabaseErrorException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;

		List<Computer> computers = computerDAO.getAllComputers(0, 10);
		try {
			computers = computerDAO.getAllComputers(-10, 0);
		} catch (DatabaseErrorException e) {
			expectedFailure1 = true;
		}

		try {
			computers = computerDAO.getAllComputers(0, -10);
		} catch (DatabaseErrorException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
		assertNotNull(computers);
		assertTrue(computers.size() <= 10);
		assertTrue(!computers.isEmpty());
	}

	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testCountComputersByName() throws DatabaseErrorException {
		assertTrue(computerDAO.countComputersByName("mac") > 0);
		assertEquals(0, computerDAO.countComputersByName("This computer shouldn't be in the database"));
	}

	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testCountCompaniesByName() throws DatabaseErrorException {
		assertTrue(companyDAO.countCompaniesByName("apple") > 0);
		assertEquals(0, computerDAO.countComputersByName("This computer shouldn't be in the database"));
	}

	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetComputersByName2() throws DatabaseErrorException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;

		List<Computer> computers = computerDAO.getComputersByName("Apple", 0, 10);
		try {
			computers = computerDAO.getAllComputers(-10, 0);
		} catch (DatabaseErrorException e) {
			expectedFailure1 = true;
		}

		try {
			computers = computerDAO.getAllComputers(0, -10);
		} catch (DatabaseErrorException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
		assertNotNull(computers);
		assertTrue(computers.size() <= 10);
		assertTrue(!computers.isEmpty());
	}

	/**
	 * expected results of getComputerById
	 * 
	 * @throws DatabaseErrorException
	 * @throws ObjectNotFoundException
	 */
	@Test
	public void testGetComputerById() throws ObjectNotFoundException, DatabaseErrorException {
		Computer computer1 = null;
		Computer computer2 = null;
		boolean excpectedException = false;

		long validId = computerDAO.getComputerByName("CM-2a").get(0).getId();

		computer1 = computerDAO.getComputerById(validId);

		assertNotNull(computer1);
		assertNotNull(computer1.getName());
		assertEquals(false, computer1.getName().isEmpty());
		try {
			computer2 = computerDAO.getComputerById(0);
		} catch (ObjectNotFoundException e) {
			excpectedException = true;
		}

		assertTrue(excpectedException);
		assertNull(computer2);
	}

	/**
	 * expected results of getComputerByName
	 * 
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetComputerByName() throws DatabaseErrorException {
		String realComputerName = "CM-2a", fakeComputerName = "LOL C PAS DANS LA DB";
		List<Computer> computers = null;
		computers = computerDAO.getComputerByName(realComputerName);
		assertNotNull(computers);
		assertEquals(1, computers.size());
		assertEquals(realComputerName, computers.get(0).getName());

		computers = null;
		computers = computerDAO.getComputerByName(fakeComputerName);
		assertNotNull(computers);
		assertTrue(computers.isEmpty());

	}

	/**
	 * @throws DatabaseErrorException
	 * @throws InvalidParameterException
	 * @throws ObjectNotFoundException
	 * 
	 */
	@Test
	public void testCreateComputer() throws DatabaseErrorException, InvalidParameterException, ObjectNotFoundException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		boolean expectedFailure3 = false;

		LocalDate now = LocalDate.now();
		LocalDate previously = LocalDate.now().minusWeeks(2);

		String validName = "Test Valid Name 2";
		String invalidName = "";

		try {
			computerDAO.createComputer(new Computer());
		} catch (InvalidParameterException e) {
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);

		try {
			computerDAO.createComputer(new Computer(new Computer.Builder().name(invalidName)));
		} catch (InvalidParameterException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure2);

		try {
			computerDAO.createComputer(
					new Computer(new Computer.Builder().name(validName).introduced(now).discontinued(previously)));
		} catch (InvalidParameterException e) {
			expectedFailure3 = true;
		}
		assertTrue(expectedFailure3);

		computerDAO.createComputer(
				new Computer(new Computer.Builder().name(validName).introduced(previously).discontinued(now)));
		computerDAO.deleteComputerByName(validName);
	}

	/**
	 * @throws DatabaseErrorException
	 * @throws InvalidParameterException
	 * @throws ObjectNotFoundException
	 * 
	 */
	@Test
	public void testDeleteComputerByName()
			throws DatabaseErrorException, InvalidParameterException, ObjectNotFoundException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		boolean expectedFailure3 = false;

		String validName = "ValidName";
		String notExistsName = "nO SucH cOmPuTER In THiS dATaBAse";
		String nullName = null;
		String emptyName = "";

		computerDAO.createComputer(new Computer(new Computer.Builder().name(validName)));

		try {
			computerDAO.deleteComputerByName(notExistsName);
		} catch (ObjectNotFoundException e) {
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);

		try {
			computerDAO.deleteComputerByName(nullName);
		} catch (InvalidParameterException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure2);

		try {
			computerDAO.deleteComputerByName(emptyName);
		} catch (InvalidParameterException e) {
			expectedFailure3 = true;
		}
		assertTrue(expectedFailure3);

		computerDAO.deleteComputerByName(validName);

	}

	/**
	 * @throws DatabaseErrorException
	 * @throws ObjectNotFoundException
	 * @throws InvalidParameterException
	 * 
	 */
	@Test
	public void testDeleteComputerById()
			throws ObjectNotFoundException, DatabaseErrorException, InvalidParameterException {
		boolean expectedError = false;

		Computer computer = null;
		int id = 0;
		int invalidId = 0;
		// get one valid computer
		while (computer == null) {
			id++;
			try {
				computer = computerDAO.getComputerById(id);
			} catch (ObjectNotFoundException e) {

			}

		}

		try {
			computerDAO.deleteComputerById(invalidId);
		} catch (ObjectNotFoundException | InvalidParameterException e) {
			expectedError = true;
		}
		assertTrue(expectedError);

		computerDAO.deleteComputerById(computer.getId());

		// restore db
		computerDAO.createComputer(computer);
	}

	/**
	 * @throws DatabaseErrorException
	 * @throws ObjectNotFoundException
	 * @throws InvalidParameterException
	 * 
	 */
	@Test
	public void testUpdateComputer() throws ObjectNotFoundException, DatabaseErrorException, InvalidParameterException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		boolean expectedFailure3 = false;

		String origName = "original name";
		String newName = "new name";

		LocalDate now = LocalDate.now();
		LocalDate previously = LocalDate.now().minusWeeks(2);

		Computer nullComputer = null;
		Computer nullName = null;
		Computer emptyName = null;
		Computer incoherentDates = null;
		Computer validComputer = null;
		Computer returnedComputer = null;

		Computer dbComputer = new Computer(new Computer.Builder().name(origName).introduced(now));

		computerDAO.createComputer(dbComputer);
		dbComputer = computerDAO.getComputerByName(origName).get(0);
		assertNotNull(dbComputer);

		nullName = new Computer(Computer.getBuilder()
					.id(dbComputer.getId())
					.introduced(previously));
		nullName.setName(null);
		emptyName = new Computer(new Computer.Builder().id(dbComputer.getId()));
		emptyName.setName("");
		incoherentDates = new Computer(
				new Computer.Builder().name(dbComputer.getName()).id(dbComputer.getId()).introduced(now).discontinued(previously));
		validComputer = new Computer(
				new Computer.Builder().name(newName).id(dbComputer.getId()).introduced(previously).discontinued(now));

		// ok with no name : no update
		computerDAO.updateComputer(nullName);
		
		try {
			computerDAO.updateComputer(nullComputer);
		} catch (InvalidParameterException e) {
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);
		

		try {
			computerDAO.updateComputer(emptyName);
		} catch (InvalidParameterException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure2);

		try {
			computerDAO.updateComputer(incoherentDates);
		} catch (InvalidParameterException e) {
			expectedFailure3 = true;
		}
		assertTrue(expectedFailure3);
		computerDAO.updateComputer(validComputer);
		returnedComputer = computerDAO.getComputerById(dbComputer.getId());

		assertEquals(validComputer.getName(),returnedComputer.getName());
		assertEquals(validComputer.getIntroduced(),returnedComputer.getIntroduced());
		assertEquals(validComputer.getDiscontinued(), returnedComputer.getDiscontinued());

		computerDAO.deleteComputerById(validComputer.getId());

	}

	/**
	 * @throws DatabaseErrorException
	 * 
	 */
	@Test
	public void testGetOrderedComputers() throws DatabaseErrorException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;

		for (ComputerDAO.ComputerField orderBy : ComputerDAO.ComputerField.values()) {
			for (OrderDirection direction : OrderDirection.values()) {
				assertTrue(!computerDAO.getOrderedComputers("mac", 0, 10, orderBy, direction).isEmpty());
				assertTrue(!computerDAO.getOrderedComputers("", 10, 10, orderBy, direction).isEmpty());
			}
		}

		List<Computer> res = computerDAO.getOrderedComputers("", 0, 10, ComputerDAO.ComputerField.id,
				OrderDirection.DESC);
		assertTrue(res.get(0).getId() > res.get(1).getId());

		res = computerDAO.getOrderedComputers("", 0, 10, ComputerDAO.ComputerField.id,
				OrderDirection.ASC);

		assertTrue(res.get(0).getId() < res.get(1).getId());

		try {
			computerDAO.getOrderedComputers("", -1, 10, ComputerDAO.ComputerField.id, OrderDirection.ASC);
		} catch (DatabaseErrorException e) {
			expectedFailure1 = true;
		}

		try {
			computerDAO.getOrderedComputers("", 0, -1, ComputerDAO.ComputerField.id, OrderDirection.ASC);
		} catch (DatabaseErrorException e) {
			expectedFailure2 = true;
		}

		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);

	}



}
