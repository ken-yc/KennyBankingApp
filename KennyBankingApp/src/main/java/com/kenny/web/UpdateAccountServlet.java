package com.kenny.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kenny.model.Account;
import com.kenny.model.AccountStatus;
import com.kenny.model.AccountType;
import com.kenny.service.AccountService;

@WebServlet("/accounts/update")
public class UpdateAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	// UPDATE ACCOUNT ENDPOINT
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		AccountService service = new AccountService();
		PrintWriter out = res.getWriter();

		try {
			int accountid = Integer.valueOf(req.getParameter("accountid"));
			
			String balanceString = req.getParameter("balance");
			
			double balance = 0;
			if(!balanceString.isEmpty())
				balance = Double.valueOf(balanceString);
			
			String type = req.getParameter("type");
			AccountType typeEnum = null;
			if(!type.isEmpty())
				typeEnum = AccountType.valueOf(type);

			String status = req.getParameter("status");
			AccountStatus statEnum = null;
			if(!status.isEmpty())
				statEnum = AccountStatus.valueOf(status);
			
			int result = 0;
			
			Account accountToUpdate = service.findById(accountid);
			if(accountToUpdate != null){
				if(!balanceString.isEmpty())
					accountToUpdate.setBalance(balance);
				if(typeEnum != null)
					accountToUpdate.setStatus(statEnum);
				if(statEnum != null)
					accountToUpdate.setType(typeEnum);
				result = service.update(accountToUpdate);
			}

			res.setContentType("application/json");

			if (result == 0) {
				res.setStatus(500);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("message:\"Unable to Update Account\"}");
			} else {
				res.setStatus(200);
				out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
				out.println("\"message\":\"Account Update Successful.\"}");
			}

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			res.setStatus(400);
			out.print("{\"statuscode\":\"" + res.getStatus() + "\",");
			out.println("message:\"Unable to Update Account\"}");
		}
		out.flush();
		out.close();

	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
