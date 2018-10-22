package com.excilys.cdb.persistence;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

import junit.framework.TestCase;

/**
 * @author Jonasz Leflour
 * 
 *
 */
public class DatabaseAccessorTest extends TestCase {
	private DatabaseAccessor dba;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		try{
			dba = DatabaseAccessor.GetDatabaseAccessor();
			
		}catch(Exception e) {
			fail("Uncatched exception occured");
		}
		assert(dba != null);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		dba = null;
	}

	/**
	 * GetDatabaseAccessor test : test if singleton
	 */
	public void testGetDatabaseAccessor() {
		DatabaseAccessor dba2 = null;
		try {
			dba2 = DatabaseAccessor.GetDatabaseAccessor();
		} catch (Exception e) {
			fail(e.toString());
		}
		assert(dba == dba2);
	}

	/**
	 * Tests that the list object is not null
	 */
	public void testGetAllComputers() {
		List<Computer> computers = dba.getAllComputers();
		assert(computers != dba);
	}

	/**
	 * Tests that the list object is not null
	 */
	public void testGetAllCompanies() {
		List<Company> computers = dba.getAllCompanies();
		assert(computers != dba);
	}

	/**
	 * expected results of getCompanyById
	 */
	public void testGetCompanybyId() {
		Company company1 = null;
		Company company2 = null;
		boolean excpectedException = false;
		
		try {
			company1 = dba.getCompanybyId(1);
		} catch (ObjectNotFoundException e) {
			fail(e.toString());
		}
		assert(company1 != null);
		try {
			company2 = dba.getCompanybyId(0);
		} catch (ObjectNotFoundException e) {
			excpectedException = true;
		}
		assert(excpectedException);
		assert(company2 == null);
	}

	/**
	 * expected results of getComputerById
	 */
	public void testGetComputerById() {
		Computer computer1 = null;
		Computer computer2 = null;
		boolean excpectedException = false;
		
		try {
			computer1 = dba.getComputerById(1);
		} catch (ObjectNotFoundException e) {
			fail(e.toString());
		}
		assert(computer1 != null);
		try {
			computer2 = dba.getComputerById(0);
		} catch (ObjectNotFoundException e) {
			excpectedException = true;
		}
		assert(excpectedException);
		assert(computer2 == null);
	}

	/**
	 * expected results of getComputerByName
	 */
	public void testGetComputerByName() {
		String realComputerName = "CM-2a", fakeComputerName = "LOL C PAS DANS LA DB";
		List<Computer> computers = null;
		computers = dba.getComputerByName(realComputerName);
		assert(computers != null);
		assert(computers.size() == 1);
		assertEquals(computers.get(0).getName(),realComputerName);
		
		computers = null;
		computers = dba.getComputerByName(fakeComputerName);
		assert(computers != null);
		assert(computers.isEmpty());
		
	}

	/*public void testCreateComputer() {
		fail("Not yet implemented");
	}

	public void testDeleteComputerByName() {
		fail("Not yet implemented");
	}

	public void testDeleteComputerById() {
		fail("Not yet implemented");
	}

	public void testUpdateComputer() {
		fail("Not yet implemented");
	}*/

}
