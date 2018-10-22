package com.excilys.cdb.persistence;

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

	/*public void testSetupDatabase() {
		fail("Not yet implemented");
	}

	public void testGetAllComputers() {
		fail("Not yet implemented");
	}

	public void testGetAllCompanies() {
		fail("Not yet implemented");
	}

	public void testGetCompanybyId() {
		fail("Not yet implemented");
	}

	public void testGetComputerById() {
		fail("Not yet implemented");
	}

	public void testGetComputerByName() {
		fail("Not yet implemented");
	}

	public void testCreateComputer() {
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
