package com.excilys.cdb.controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dao.CachedDaoProvider;
import com.excilys.cdb.dao.DaoProvider;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;

/**
 * Servlet implementation class AddComputerHttpServlet
 */
@WebServlet("/addcomputer")
public class AddComputerHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DaoProvider dao;
	
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
			dao = new CachedDaoProvider();
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
			request.setAttribute("companies", dao.getAllCompanies());
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
		
		if(request.getAttribute("computername") != null) {
			newComputer.setName(request.getAttribute("computername").toString());
		}
		if(request.getAttribute("introduced") != null) {
			newComputer.setIntroduced(LocalDate.parse(request.getAttribute("introduced").toString()));
		}
		if(request.getAttribute("discontinued") != null) {
			newComputer.setDiscontinued(LocalDate.parse(request.getAttribute("discontinued").toString()));
		}
		if(request.getAttribute("companyId") != null 
				&& Integer.parseInt(request.getAttribute("companyId").toString())>0 ){
			long companyId =Integer.parseInt(request.getAttribute("companyId").toString());
			newComputer.setCompany(new Company(companyId,""));
		}
		try {
			dao.addComputer(newComputer);
			request.setAttribute("RequestStatus","Ok");
		} catch (DatabaseErrorException e) {
			request.setAttribute("RequestStatus",e.toString());
			throw new ServletException(e);
		} catch (InvalidParameterException e) {
			request.setAttribute("RequestStatus",e.toString());
			throw new ServletException(e);
		}
		doGet(request, response);
	}

}
