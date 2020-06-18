package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		
		if(session == null) {
			//res.sendRedirect("login.html");
			res.setStatus(440);
		} else {
			ObjectMapper om = new ObjectMapper();
			om.writeValue(out, session.getAttribute("user"));
		}
		
		out.close();
	}
	
	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
