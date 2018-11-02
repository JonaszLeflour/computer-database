package com.excilys.cdb.controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.CachedDTOProvider;
import com.excilys.cdb.dto.DTOProvider;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.DataPresenter;
import com.excilys.cdb.service.SQLDataPresenter;

/**
 * Servlet implementation class EditComputerHttpServlet
 */
@WebServlet("/editcomputer")
public class EditComputerHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataPresenter dp;
	private DTOProvider dto;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			dp = new SQLDataPresenter();
			dto = new CachedDTOProvider();
		} catch (ClassNotFoundException | IOException | DatabaseErrorException e) {
			throw new ServletException(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp");
		Computer c = null;

		if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
			getServletContext().getRequestDispatcher("/404").forward(request, response);
		}

		try {
			int id = Integer.parseInt(request.getParameter("id"));
			c = dp.getComputerById(id);
		} catch (NumberFormatException | ObjectNotFoundException e) {
			getServletContext().getRequestDispatcher("/404").forward(request, response);
		} catch (DatabaseErrorException e) {
			throw new ServletException(e);
		}
		
		request.setAttribute("computer", c);
		try {
			request.setAttribute("companies", dto.getAllCompanies());
		} catch (DatabaseErrorException e) {
			throw new ServletException(e);
		}
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Computer newComputer = new Computer();
		if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
			newComputer.setId(Integer.parseInt(request.getParameter("id")));
		}
		
		if (request.getParameter("computername") != null && !request.getParameter("computername").isEmpty()) {
			newComputer.setName(request.getParameter("computername").toString());
		}
		if (request.getParameter("introduced") != null && !request.getParameter("introduced").isEmpty()) {
			newComputer.setIntroduced(LocalDate.parse(request.getParameter("introduced")));
		}
		if (request.getParameter("discontinued") != null && !request.getParameter("discontinued").isEmpty()) {
			newComputer.setDiscontinued(LocalDate.parse(request.getParameter("discontinued")));
		}
		if (request.getParameter("companyId") != null && !request.getParameter("companyId").isEmpty()
				&& Integer.parseInt(request.getParameter("companyId").toString()) > 0) {
			long companyId = Integer.parseInt(request.getParameter("companyId"));
			newComputer.setCompany(new Company(companyId, ""));
		}
		try {
			dp.updateComputer(newComputer);
		} catch (ObjectNotFoundException |DatabaseErrorException | InvalidParameterException e) {
			throw new ServletException(e);
		}
		response.sendRedirect("dashboard");
	}

}
