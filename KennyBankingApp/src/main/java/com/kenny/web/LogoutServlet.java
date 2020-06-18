package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		String msg;
		
		HttpSession session = req.getSession(false);
		if(session != null)
		{
			session.invalidate();
			msg = "Logout successful";
		} else {
			res.setStatus(440);
			msg = "Session has expired";
		}
		
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		res.setStatus(200);
		out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
		out.println("\"message\":\"" + msg + "\"}");
	}
	
	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
