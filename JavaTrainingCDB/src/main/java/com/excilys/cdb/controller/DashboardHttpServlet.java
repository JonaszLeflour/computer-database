package com.excilys.cdb.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.AllComputerRequestPager;
import com.excilys.cdb.dto.NameComputerRequestPager;
import com.excilys.cdb.dto.ComputerRequestPager;
import com.excilys.cdb.dto.InvalidPageNumberException;
import com.excilys.cdb.dto.InvalidPageSizeError;
import com.excilys.cdb.persistence.DatabaseErrorException;

/**
 * Servlet implementation class DashboardHttpServlet
 */
@WebServlet("/dashboard")
public class DashboardHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ComputerRequestPager pager;
    private final long defaultPageSize = 10L;
    
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
			pager = new AllComputerRequestPager(defaultPageSize);
		} catch (ClassNotFoundException | IOException | DatabaseErrorException | InvalidPageSizeError e) {
			throw new ServletException(e); 
		}
        
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int currentPage;
		try {
			if(request.getParameter("search") != null) {
				pager = new NameComputerRequestPager(request.getParameter("search"),defaultPageSize);
			}else if(request.getParameter("reset") != null) {
				pager = new AllComputerRequestPager(defaultPageSize);
			}
			
			
			RequestDispatcher dispatcher = getServletContext()
	                .getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
			
			if(request.getParameter("page") != null) {
				currentPage = Integer.parseInt(request.getParameter("page").toString());
			}else {
				currentPage = 1;
			}
			request.setAttribute("nbcomputers",pager.getNbComputers());
			request.setAttribute("page",pager.getPage(currentPage-1));
			request.setAttribute("currentPage",currentPage);
			request.setAttribute("nbPages", pager.getNbPages());
			dispatcher.forward(request, response);
		} catch (DatabaseErrorException | ClassNotFoundException | InvalidPageSizeError | InvalidPageNumberException e) {
			throw new ServletException(e);
		}
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
