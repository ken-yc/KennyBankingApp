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
import com.kenny.model.AccountType;
import com.kenny.model.User;
import com.kenny.service.AccountService;
import com.kenny.service.UserService;

@WebServlet("/accounts/*")
public class AccountsServlet extends HttpServlet {

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

		if (path == null) {
			List<Account> accounts = service.findAll();

			om.writeValue(out, accounts);

		} else {

			try {
				int id = Integer.valueOf(path.substring(1));

				Account thisAccount = service.findById(id);

				om.writeValue(out, thisAccount);
			} catch (NumberFormatException e) {
				om.writeValue(out, null);
			}
		}

		out.flush();
		out.close();
	}

	// SUBMIT ACCOUNT ENDPOINT
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		AccountService service = new AccountService();
		UserService uService = new UserService();

		try {
			int userid = Integer.valueOf(req.getParameter("userid"));
			
			String balString = req.getParameter("balance");
			double balance = 0; 
			if(!balString.isEmpty())
				balance = Double.valueOf(balString);
			
			
			String status = req.getParameter("status");
			AccountStatus statEnum;
			if(status != null && !status.isEmpty()) {
				statEnum = AccountStatus.valueOf(status);
			} else {
				statEnum = AccountStatus.PENDING;
			}
			
			String type = req.getParameter("type");
			AccountType typeEnum;
			if(type !=null && !type.isEmpty()) {
				typeEnum = AccountType.valueOf(type);
			} else {
				typeEnum = AccountType.CHECKING;
			}
			
			Account newAcc = new Account();
			newAcc.setBalance(balance);
			newAcc.setStatus(statEnum);
			newAcc.setType(typeEnum);
			
			User userResult = uService.findById(userid);
			int result = 0;
			if(userResult != null){
				result = service.insert(newAcc, userid);
			}

			PrintWriter out = res.getWriter();

			res.setContentType("application/json");

			if (result == 0) {
				res.setStatus(500);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("message:\"Unable to Submit Account\"}");
			} else {
				res.setStatus(201);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("\"message\":\"Account Creation Successful.\"}");
			}

			out.flush();
			out.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
