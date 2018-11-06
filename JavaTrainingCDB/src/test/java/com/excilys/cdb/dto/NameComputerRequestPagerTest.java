package com.excilys.cdb.dto;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.dto.DTOComputer;

/**
 * @author Jonasz Leflour
 *
 */
public class NameComputerRequestPagerTest {
	NameComputerRequestPager pager;
	int maxSize;
	
	/**
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws DatabaseErrorException
	 * @throws InvalidPageSizeException
	 */
	@Before
	public void setUp() throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException, InvalidPageSizeException {
		maxSize = 10;
		pager = new NameComputerRequestPager("Apple", maxSize);
	}
	
	/**
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws DatabaseErrorException
	 * @throws InvalidPageSizeException
	 */
	@Test
	public void creationTest() throws FileNotFoundException, ClassNotFoundException, IOException, DatabaseErrorException, InvalidPageSizeException {
		AllComputerRequestPager pagerTest = new AllComputerRequestPager(maxSize);
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		
		try {
			new AllComputerRequestPager(-5);
		} catch (InvalidPageSizeException e) {
			expectedFailure1 = true;
		}
		
		try {
			new AllComputerRequestPager(0);
		} catch (InvalidPageSizeException e) {
			expectedFailure2 = true;
		}
		
		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
		assertTrue(pagerTest!=null);
		pagerTest = null;
	}
	
	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void getNbComputerTest() throws DatabaseErrorException {
		assertTrue(pager.getNbComputers() > 0);
	}
	
	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void getNbPages() throws DatabaseErrorException {
		assertTrue(pager.getNbPages() > 0);
	}
	
	/**
	 * @throws DatabaseErrorException
	 * @throws InvalidPageNumberException 
	 */
	@Test
	public void getPageTest() throws DatabaseErrorException, InvalidPageNumberException {
		List<DTOComputer> list;
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;
		
		
		list = pager.getPage(0);
		try {
			pager.getPage(-1);
		} catch (InvalidPageNumberException e) {
			expectedFailure1 = true;
		}
		try {
			pager.getPage(pager.getNbPages());
		} catch (InvalidPageNumberException e) {
			expectedFailure2 = true;
		}
		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
		
		assertTrue(!list.isEmpty());
		assertTrue(list.size()<=maxSize);
	}
	
	/**
	 * 
	 */
	@After
	public void tearDown() {
		pager = null;
	}
}
