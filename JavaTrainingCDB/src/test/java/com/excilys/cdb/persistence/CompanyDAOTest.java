package com.excilys.cdb.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.controller.beans.DAOConfig;
import com.excilys.cdb.model.Company;

/**
 * @author Jonasz Leflour
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { DAOConfig.class })
public class CompanyDAOTest {
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
		companyDAO = null;
	}

	/**
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetOrderedCompanies() throws DatabaseErrorException {
		boolean expectedFailure1 = false;
		boolean expectedFailure2 = false;

		for (CompanyDAO.CompanyField orderBy : CompanyDAO.CompanyField.values()) {
			for (OrderDirection direction : OrderDirection.values()) {
				assertTrue(!companyDAO.getOrderedCompanies("a", 0, 10, orderBy, direction).isEmpty());
				assertTrue(!companyDAO.getOrderedCompanies("", 5, 5, orderBy, direction).isEmpty());
			}
		}

		List<Company> res = companyDAO.getOrderedCompanies("", 0, 10, CompanyDAO.CompanyField.id, OrderDirection.DESC);
		assertTrue(res.get(0).getId() > res.get(1).getId());

		res = companyDAO.getOrderedCompanies("", 0, 10, CompanyDAO.CompanyField.id, OrderDirection.ASC);

		assertTrue(res.get(0).getId() < res.get(1).getId());

		try {
			companyDAO.getOrderedCompanies("", -1, 10, CompanyDAO.CompanyField.id, OrderDirection.ASC);
		} catch (DatabaseErrorException e) {
			expectedFailure1 = true;
		}

		try {
			companyDAO.getOrderedCompanies("", 1, -1, CompanyDAO.CompanyField.id, OrderDirection.ASC);
		} catch (DatabaseErrorException e) {
			expectedFailure2 = true;
		}

		assertTrue(expectedFailure1);
		assertTrue(expectedFailure2);
	}

	/**
	 * Tests that the list object is not null
	 * 
	 * @throws DatabaseErrorException
	 */
	@Test
	public void testGetAllCompanies() throws DatabaseErrorException {
		List<Company> companies = companyDAO.getAllCompanies();
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

		company1 = companyDAO.getCompanybyId(1);

		assertNotNull(company1);
		try {
			company2 = companyDAO.getCompanybyId(0);
		} catch (ObjectNotFoundException e) {
			excpectedException = true;
		}
		assertTrue(excpectedException);
		assertNull(company2);
	}
}
