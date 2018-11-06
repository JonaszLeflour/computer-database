package com.excilys.cdb.persistence;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.*;
import com.excilys.cdb.persistence.InvalidParameterException;

/**
 * @author Jonasz Leflour
 * 
 *
 */

public class DatabaseAccessorTest {
	private DatabaseAccessor dba;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		dba = DatabaseAccessor.GetDatabaseAccessor();
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		dba = null;
	}

	/**
	 * GetDatabaseAccessor test : test if singleton
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DatabaseErrorException
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testGetDatabaseAccessor() throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
		DatabaseAccessor dba2 = null;
		dba2 = DatabaseAccessor.GetDatabaseAccessor();
		assertEquals(dba2, dba);
	}

	/**
	 * Tests that the list object is not null
	 * @throws DatabaseErrorException 
	 */
	@Test
	public void testGetAllComputers() throws DatabaseErrorException {
		List<Computer> computers = dba.getAllComputers();
		assertNotNull(computers);
	}
	
	
	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetAllComputers2()  throws DatabaseErrorException{
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		
		List<Computer> computers = dba.getAllComputers(0,10);
		try {
			computers = dba.getAllComputers(-10,0);
		} catch (DatabaseErrorException e) {
			expectedFailure1 = true;
		}
		
		try {
			computers = dba.getAllComputers(0,-10);
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
	 * 
	 */
	@Test
	public void testCountComputers() throws DatabaseErrorException {
		assertTrue(dba.countComputers()>0);
	}
	
	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testCountComputersByName() throws DatabaseErrorException{
		assertTrue(dba.countComputersByName("mac")>0);
		assertEquals(0,dba.countComputersByName("This computer shouldn't be in the database"));
	}
	
	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetComputersByName2() throws DatabaseErrorException{
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		
		List<Computer> computers = dba.getComputersByName("Apple",0,10);
		try {
			computers = dba.getAllComputers(-10,0);
		} catch (DatabaseErrorException e) {
			expectedFailure1 = true;
		}
		
		try {
			computers = dba.getAllComputers(0,-10);
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
	 * Tests that the list object is not null
	 * @throws DatabaseErrorException 
	 */
	@Test
	public void testGetAllCompanies() throws DatabaseErrorException{
		List<Company> companies = dba.getAllCompanies();
		assertNotNull(companies);
	}

	/**
	 * expected results of getCompanyById
	 * 
	 * @throws ObjectNotFoundException
	 * @throws DatabaseErrorException 
	 */
	@Test
	public void testGetCompanybyId() throws ObjectNotFoundException, DatabaseErrorException {
		Company company1 = null;
		Company company2 = null;
		boolean excpectedException = false;

		company1 = dba.getCompanybyId(1);

		assertNotNull(company1);
		try {
			company2 = dba.getCompanybyId(0);
		} catch (ObjectNotFoundException e) {
			excpectedException = true;
		}
		assertTrue(excpectedException);
		assertNull(company2);
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

		long validId = dba.getComputerByName("CM-2a").get(0).getId();

		computer1 = dba.getComputerById(validId);

		assertNotNull(computer1);
		assertNotNull(computer1.getName());
		assertEquals(false, computer1.getName().isEmpty());
		try {
			computer2 = dba.getComputerById(0);
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
		computers = dba.getComputerByName(realComputerName);
		assertNotNull(computers);
		assertEquals(1, computers.size());
		assertEquals(realComputerName, computers.get(0).getName());

		computers = null;
		computers = dba.getComputerByName(fakeComputerName);
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

		String validName = "Test Valid Name.";
		String invalidName = "";

		try {
			dba.createComputer(new Computer());
		} catch (InvalidParameterException e) {
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);

		try {
			dba.createComputer(new Computer(new Computer.Builder().name(invalidName)));
		} catch (InvalidParameterException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure2);

		try {
			dba.createComputer(
					new Computer(new Computer.Builder().name(validName).introduced(now).discontinued(previously)));
		} catch (InvalidParameterException e) {
			expectedFailure3 = true;
		}
		assertTrue(expectedFailure3);

		dba.createComputer(
				new Computer(new Computer.Builder().name(validName).introduced(previously).discontinued(now)));
		dba.deleteComputerByName(validName);
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

		dba.createComputer(new Computer(new Computer.Builder().name(validName)));

		try {
			dba.deleteComputerByName(notExistsName);
		} catch (ObjectNotFoundException e) {
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);

		try {
			dba.deleteComputerByName(nullName);
		} catch (InvalidParameterException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure2);

		try {
			dba.deleteComputerByName(emptyName);
		} catch (InvalidParameterException e) {
			expectedFailure3 = true;
		}
		assertTrue(expectedFailure3);

		dba.deleteComputerByName(validName);

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
				computer = dba.getComputerById(id);
			} catch (ObjectNotFoundException e) {

			}

		}

		try {
			dba.deleteComputerById(invalidId);
		} catch (ObjectNotFoundException e) {
			expectedError = true;
		}
		assertTrue(expectedError);

		dba.deleteComputerById(computer.getId());

		// restore db
		dba.createComputer(computer);
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

		String origName = "Valid Computer";
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

		dba.createComputer(dbComputer);
		dbComputer = dba.getComputerByName(origName).get(0);
		assertNotNull(dbComputer);

		nullName = new Computer(new Computer.Builder().id(dbComputer.getId()).introduced(previously));
		nullName.setName(null);
		emptyName = new Computer(new Computer.Builder().id(dbComputer.getId()));
		emptyName.setName("");
		incoherentDates = new Computer(
				new Computer.Builder().id(dbComputer.getId()).introduced(now).discontinued(previously));
		validComputer = new Computer(
				new Computer.Builder().name(newName).id(dbComputer.getId()).introduced(previously).discontinued(now));

		try {
			dba.updateComputer(nullComputer);
		} catch (InvalidParameterException e) {
			expectedFailure1 = true;
		}
		assertTrue(expectedFailure1);

		// ok with no name : no update
		dba.updateComputer(nullName);

		try {
			dba.updateComputer(emptyName);
		} catch (InvalidParameterException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure2);

		try {
			dba.updateComputer(incoherentDates);
		} catch (InvalidParameterException e) {
			expectedFailure3 = true;
		}
		assertTrue(expectedFailure3);
		dba.updateComputer(validComputer);
		returnedComputer = dba.getComputerById(dbComputer.getId());

		assertEquals(returnedComputer.getName(), validComputer.getName());
		assertEquals(returnedComputer.getIntroduced(), validComputer.getIntroduced());
		assertEquals(returnedComputer.getDiscontinued(), validComputer.getDiscontinued());

		dba.deleteComputerById(validComputer.getId());

	}
	
	/**
	 * @throws DatabaseErrorException 
	 * 
	 */
	@Test
	public void getOrderedComputersTest() throws DatabaseErrorException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		
		
		for(DatabaseAccessor.ComputerFields orderBy : DatabaseAccessor.ComputerFields.values()) {
			for(DatabaseAccessor.OrderDirection direction : DatabaseAccessor.OrderDirection.values()) {
				assertTrue(!dba.getOrderedComputers("mac", 0, 10, orderBy, direction).isEmpty());
				assertTrue(!dba.getOrderedComputers("", 10, 10, orderBy, direction).isEmpty());
			}
		}
		
		
		List<Computer> res = dba.getOrderedComputers("", 0, 10, DatabaseAccessor.ComputerFields.id, DatabaseAccessor.OrderDirection.DESC);
		assertTrue(res.get(0).getId() > res.get(1).getId());
		
		res = dba.getOrderedComputers("", 0, 10, DatabaseAccessor.ComputerFields.id, DatabaseAccessor.OrderDirection.ASC);
		
		assertTrue(res.get(0).getId() < res.get(1).getId());
		
		try {
			dba.getOrderedComputers("", -1, 10, DatabaseAccessor.ComputerFields.id, DatabaseAccessor.OrderDirection.ASC);
		}catch (DatabaseErrorException e){
			expectedFailure1 = true;
		}
		
		try {
			dba.getOrderedComputers("", -1, 10, DatabaseAccessor.ComputerFields.id, DatabaseAccessor.OrderDirection.ASC);
		}catch (DatabaseErrorException e){
			expectedFailure2 = true;
		}
		
		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
		
		
	}

}
