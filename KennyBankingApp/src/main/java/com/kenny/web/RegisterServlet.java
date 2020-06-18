package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kenny.model.Role;
import com.kenny.model.User;
import com.kenny.service.UserService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		System.out.println("Connected!");

		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		User newUser = new User(0, firstname, lastname, email, username, password, Role.STANDARD);
		UserService service = new UserService();

		PrintWriter out = res.getWriter();

		int result = service.registerUser(newUser);

		res.setContentType("application/json");
		if (result == 0) {
			res.setStatus(400);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("message:\"Unable to Register User\"}");
		} else {
			res.setStatus(200);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("\"message\":\"Registration Successful.\"}");
		}
		out.flush();
		out.close();

	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
