package com.excilys.cdb.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.CachedDTOProvider;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.dto.DTOProvider;
import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * Servlet implementation class DashboardHttpServlet
 */
@WebServlet("/dashboard")
public class DashboardHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DTOProvider dto;
    private PagerWrapper<DTOComputer> requestResults;
    
    private String currentSearch;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
			dto = new CachedDTOProvider();
			requestResults = new PagerWrapper<DTOComputer>(dto.getAllComputers());
		} catch (ClassNotFoundException | IOException | DatabaseErrorException e) {
			throw new ServletException(e.toString()); 
		}
        
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int currentPage;
		boolean newSearch = false;
		if(request.getParameter("search") != null) {
			newSearch = true;
			currentSearch = request.getParameter("search");
		}
		
		
		
		((CachedDTOProvider)dto).updateCache();
		RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
		
		if(request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page").toString());
		}else {
			currentPage = 1;
		}
		try {
			if(newSearch) {
				requestResults = new PagerWrapper<DTOComputer>(dto.getComputersByName(currentSearch));
			}
		} catch (DatabaseErrorException e) {
			throw new ServletException("Error with database connexion");
		}
		request.setAttribute("nbcomputers",requestResults.getNbElements());
		request.setAttribute("page",requestResults.getPage(currentPage-1));
		request.setAttribute("currentPage",currentPage);
		request.setAttribute("nbPages", requestResults.getNumberOfPages());
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
