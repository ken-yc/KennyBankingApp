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

@WebServlet("/users/update")
public class UpdateUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	// UPDATE USER ENDPOINT
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		UserService service = new UserService();
		PrintWriter out = res.getWriter();

		try {
			int userid = Integer.valueOf(req.getParameter("id"));
			String firstname = req.getParameter("firstname");
			String lastname = req.getParameter("lastname");
			String email = req.getParameter("email");
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String role = req.getParameter("role");
			Role roleEnum = null;
			if(!role.isEmpty()) {
				roleEnum = Role.valueOf(role);
			}

			int result = 0;

			User userToUpdate = service.findById(userid);
			if (userToUpdate != null) {
				if (!firstname.isEmpty())
					userToUpdate.setFirstName(firstname);
				if (!lastname.isEmpty())
					userToUpdate.setLastName(lastname);
				if (!email.isEmpty())
					userToUpdate.setEmail(email);
				if (!username.isEmpty())
					userToUpdate.setUserName(username);
				if (!password.isEmpty())
					userToUpdate.setPassword(password);
				if (roleEnum != null)
					userToUpdate.setRole(roleEnum);
				result = service.update(userToUpdate);
			}

			res.setContentType("application/json");
			if (result == 0) {
				res.setStatus(500);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("message:\"Unable to Update User\"}");
			} else {
				res.setStatus(200);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("\"message\":\"Update Successful.\"}");
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			res.setStatus(400);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("message:\"Unable to Update User\"}");
		}
		out.flush();
		out.close();
	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
