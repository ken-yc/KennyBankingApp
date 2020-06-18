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
import com.kenny.model.Account;
import com.kenny.model.AccountStatus;
import com.kenny.service.AccountService;

@WebServlet("/accounts/status/*")
public class AccountsByStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	// FIND ACCOUNTS ENDPOINT
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String path = req.getPathInfo();
		AccountService service = new AccountService();

		PrintWriter out = res.getWriter();

		ObjectMapper om = new ObjectMapper();

		res.setContentType("application/json");

		if (path != null) {
			try {
				AccountStatus as = AccountStatus.valueOf(path.substring(1));

				List<Account> accounts = service.findByStatus(as);
				
				if(!accounts.isEmpty()) {
					om.writeValue(out, accounts);
				} else {
					om.writeValue(out, null);
				}
			} catch (IllegalArgumentException e) {
				om.writeValue(out, null);
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
