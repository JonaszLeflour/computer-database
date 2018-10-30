package com.excilys.cdb.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dao.CachedDaoProvider;
import com.excilys.cdb.dao.DaoComputer;
import com.excilys.cdb.dao.DaoProvider;
import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * Servlet implementation class DashboardHttpServlet
 */
@WebServlet("/dashboard")
public class DashboardHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DaoProvider dao;
    private Pager<DaoComputer> requestResults;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
			dao = new CachedDaoProvider();
			requestResults = new Pager<DaoComputer>(dao.getAllComputers());
		} catch (ClassNotFoundException | IOException | DatabaseErrorException e) {
			throw new ServletException(e.toString()); 
		}
        
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int page = 1;
		RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
		
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page").toString());
		}
		try {
			/*if(request.getAttribute("reset") != null) {
				requestResults = new Pager<DaoComputer>(dao.getAllComputers());
			}*/if(request.getParameter("search") != null) {
				requestResults = new Pager<DaoComputer>(dao.getComputersByName(request.getParameter("search").toString()));
				System.out.println(requestResults.getNbElements());
			}
		} catch (DatabaseErrorException e) {
			throw new ServletException("Error with database connexion");
		}
		request.setAttribute("nbcomputers",requestResults.getNbElements());
		request.setAttribute("page",requestResults.getPage(page-1));		
		request.setAttribute("pagesNumber", requestResults.getNumberOfPages());
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
