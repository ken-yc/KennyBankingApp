package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kenny.model.User;
import com.kenny.service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		UserService service = new UserService();
		
		User currentUser = service.login(username, password);
		HttpSession session = req.getSession();
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		if(currentUser == null) {
			res.setStatus(400);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("message:\"Invalid Login\"}");
		} else {
			res.setStatus(200);
			session.setAttribute("user", currentUser);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("\"message\":\"Success\"}");
		}
		
	}
	
	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
