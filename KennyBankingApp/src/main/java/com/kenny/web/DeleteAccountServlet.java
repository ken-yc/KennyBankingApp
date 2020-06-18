package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kenny.model.Account;
import com.kenny.service.AccountService;

@WebServlet("/accounts/delete")
public class DeleteAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	// DELETE ACCOUNT ENDPOINT
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		AccountService service = new AccountService();
		PrintWriter out = res.getWriter();

		try {
			int accountid = Integer.valueOf(req.getParameter("accountid"));

			int result = 0;
			
			Account accountToDelete = service.findById(accountid);
			if(accountToDelete != null){
				result = service.delete(accountid);
			}

			res.setContentType("application/json");

			if (result == 0) {
				res.setStatus(500);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("message:\"Unable to Delete Account\"}");
			} else {
				res.setStatus(200);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("\"message\":\"Account Deletion Successful.\"}");
			}

			out.flush();
			out.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			res.setStatus(400);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("message:\"Unable to Delete Account\"}");
		}

	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
