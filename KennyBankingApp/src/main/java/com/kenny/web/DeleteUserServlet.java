package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kenny.model.User;
import com.kenny.service.UserService;

@WebServlet("/users/delete")
public class DeleteUserServlet extends HttpServlet {

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
			int userid = Integer.valueOf(req.getParameter("userid"));

			int result = 0;

			User userToDelete = service.findById(userid);
			if (userToDelete != null)
				result = service.delete(userid);

			res.setContentType("application/json");
			
			if (result == 0) {
				res.setStatus(500);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("\"message\":\"Unable to Delete User. Make sure user's accounts are removed.\"}");
			} else {
				res.setStatus(200);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("\"message\":\"Deletion Successful.\"}");
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			res.setStatus(400);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("\"message\":\"Unable to Delete User. Could not find that user.\"}");
		}
		out.flush();
		out.close();
	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
