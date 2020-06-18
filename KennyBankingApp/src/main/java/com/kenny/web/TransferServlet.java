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

@WebServlet("/accounts/transfer")
public class TransferServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		System.out.println(this.getServletName() + " IS INSTANTIATED.");
		super.init();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		String resMsg = "{\"message\":\"\"}";
		String msgString;
		
		try {
			int sourceId = Integer.valueOf(req.getParameter("sourceid"));
			int targetId = Integer.valueOf(req.getParameter("targetid"));
			double amount = Double.valueOf(req.getParameter("amount"));
		
			int result = 0;
			
			AccountService service = new AccountService();
	
			Account sourceAccount = service.findById(sourceId);
			Account targetAccount = service.findById(targetId);
			if (sourceAccount != null && targetAccount != null) {
				result = service.transfer(sourceId, targetId, amount);
			}
	
			if (result == 0) {
				// failed
				msgString = "Transfer Failed.";
			} else {
				// success
				msgString = "$" + amount + " has been transfered from Account #" + sourceId + " to Account #" + targetId;
			}
			

		} catch (NumberFormatException e) {
			e.printStackTrace();
			msgString = "Transfer Failed. Problem with user input.";
		}
		
		res.setContentType("application/json");
		resMsg = "{\"message\":\"" + msgString + "\"}";
		out.println(resMsg);
		out.flush();
		out.close();

	}

	public void destroy() {
		System.out.println(this.getServletName() + " DESTROY METHOD CALLED.");
		super.destroy();
	}
}
