package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.model.User;
import com.kenny.service.UserService;

@WebServlet("/users/*")
public class UsersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}
	
	// FIND USERS ENDPOINT
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		String path = req.getPathInfo();
		
		UserService service = new UserService();
		
		PrintWriter out = res.getWriter();
		
		ObjectMapper om = new ObjectMapper();
		
		res.setContentType("application/json");
		
		if(path == null) {

			List<User> users = service.findAll();
			om.writeValue(out, users);

		} else {
			try{
				int id = Integer.valueOf(path.substring(1));
				
				User thisUser = service.findById(id);
				
				if(thisUser != null) {
					om.writeValue(out, thisUser);
				} 
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}			
		}
		
		out.flush();
		out.close();

	}
	
	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
