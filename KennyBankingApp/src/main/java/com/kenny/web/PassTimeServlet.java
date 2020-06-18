package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kenny.service.AccountService;

/**
 * Servlet implementation class PassTimeServlet
 */

@WebServlet("/passTime")
public class PassTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		AccountService service = new AccountService();
		
		PrintWriter out = res.getWriter();
		
		try {
			int months = Integer.valueOf(req.getParameter("numofmonths"));
			
			int result = service.accrueInterest(months);
			
			res.setContentType("application/json");
			
			if(result != 0) {
				res.setStatus(200);
				out.println("{\"message\":\"" + months + " month(s) of interest has been accrued for all Savings Accounts.\"}");
			} else {
				res.setStatus(500);
				out.println("{\"message\":\"Error has occured while updating savings accounts.\"}");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.setStatus(400);
			out.println("{\"message\":\"Error has occured while updating savings accounts.\"}");
		}
		
		out.flush();
		out.close();
	}
	
	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}

}
