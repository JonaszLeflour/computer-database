package com.excilys.cdb.controller;

import java.io.FileNotFoundException;
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
	
    
    
    /**
     * @throws DatabaseErrorException 
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public DashboardHttpServlet() throws FileNotFoundException, IOException, DatabaseErrorException, ClassNotFoundException {
        super();
		dao = new CachedDaoProvider();
        requestResults = new Pager<DaoComputer>(dao.getAllComputers());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = 1;
		
		RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
		if(request.getAttribute("page") != null) {
			page = Integer.parseInt(request.getAttribute("page").toString());
		}
		request.setAttribute("page",requestResults.getPage(page));
		request.setAttribute("maxpages", requestResults.getNumberOfPages());
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
