package com.excilys.cdb.controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.CompanyDTOMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.SimpleCompanyService;
import com.excilys.cdb.service.SimpleComputerService;

/**
 * Servlet implementation class AddComputerHttpServlet
 */
@WebServlet("/addcomputer")
public class AddComputerHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ComputerService computerService;
	private CompanyService companyService;
	
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
    		computerService = new SimpleComputerService();
			companyService = new SimpleCompanyService();
		} catch (ClassNotFoundException | IOException | DatabaseErrorException e) {
			throw new ServletException(e.toString()); 
		}
        
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/WEB-INF/views/addComputer.jsp");
		
		try {
			request.setAttribute("companies", CompanyDTOMapper.toDTOCompany(companyService.getCompanies()));
		} catch (DatabaseErrorException e) {
			throw new ServletException(e);
		}
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		Computer newComputer = new Computer();
		
		if(request.getParameter("computername") != null && !request.getParameter("computername").isEmpty()) {
			newComputer.setName(request.getParameter("computername").toString());
		}
		if(request.getParameter("introduced") != null && !request.getParameter("introduced").isEmpty()) {
			newComputer.setIntroduced(LocalDate.parse(request.getParameter("introduced").toString()));
		}
		if(request.getParameter("discontinued") != null && !request.getParameter("discontinued").isEmpty()) {
			newComputer.setDiscontinued(LocalDate.parse(request.getParameter("discontinued").toString()));
		}
		if(request.getParameter("companyId") != null 
				&& !request.getParameter("companyId").isEmpty()
				&& Integer.parseInt(request.getParameter("companyId").toString())>0 ){
			long companyId =Integer.parseInt(request.getParameter("companyId").toString());
			newComputer.setCompany(new Company(companyId,""));
		}
		try {
			computerService.addComputer(newComputer);
			request.setAttribute("RequestStatus","Ok");
		} catch (DatabaseErrorException e) {
			request.setAttribute("RequestStatus",e.toString());
			throw new ServletException(e);
		} catch (InvalidParameterException e) {
			request.setAttribute("RequestStatus",e.toString());
			throw new ServletException(e);
		}
		getServletContext().getRequestDispatcher("/dashboard").forward(request, response);
	}

}
