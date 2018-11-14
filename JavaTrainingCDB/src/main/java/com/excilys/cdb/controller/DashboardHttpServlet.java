package com.excilys.cdb.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.excilys.cdb.dto.OrderedComputerRequestPager;
import com.excilys.cdb.dto.InvalidPageNumberException;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class DashboardHttpServlet
 */
@WebServlet("/dashboard")
public class DashboardHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Autowired
    private OrderedComputerRequestPager orderedComputerRequestPager;
    @Autowired
    private ComputerService simpleComputerService;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	    ctx.getAutowireCapableBeanFactory().autowireBean(this);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int currentPage;
		
		try {
			if(request.getParameter("search") != null) {
				orderedComputerRequestPager.name(request.getParameter("search"));
			}else if(request.getAttribute("reset") != null) {
				orderedComputerRequestPager.name("");
			}
			RequestDispatcher dispatcher = getServletContext()
	                .getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
			
			if(request.getParameter("page") != null) {
				currentPage = Integer.parseInt(request.getParameter("page").toString());
			}else {
				currentPage = 1;
			}
			request.setAttribute("nbcomputers",orderedComputerRequestPager.getNbComputers());
			request.setAttribute("page",orderedComputerRequestPager.getPage(currentPage-1));
			request.setAttribute("currentPage",currentPage);
			request.setAttribute("nbPages", orderedComputerRequestPager.getNbPages());
			dispatcher.forward(request, response);
		} catch (DatabaseErrorException | InvalidPageNumberException e) {
			throw new ServletException(e);
		}
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] deleteComputers =request.getParameterValues("deletecomputers");
		for(String idString : deleteComputers) {
			long id = Long.parseLong(idString);
			try {
				simpleComputerService.deleteComputerById(id);
			} catch (DatabaseErrorException | ObjectNotFoundException | InvalidParameterException e) {
				throw new ServletException(e);
			}
		}
		doGet(request, response);
	}

}
