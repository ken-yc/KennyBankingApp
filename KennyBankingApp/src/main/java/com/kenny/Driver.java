package com.kenny;

import java.util.List;

import com.kenny.dao.AccountDAO;
import com.kenny.dao.UserDAO;
import com.kenny.model.Account;
import com.kenny.model.AccountStatus;
import com.kenny.model.AccountType;
import com.kenny.model.Role;
import com.kenny.model.StandardUser;
import com.kenny.model.User;

public class Driver {

	public static void main(String[] args) {
		
		//StandardUser currentUser = new StandardUser("Kimmy", "Choo", "lemo@gmail.com", "yourpass");
		//User currentUser = new User(0, "Kenny", "Chao", "kenny@email.com", "myUserName", "myPassword", Role.STANDARD);
		//UserDAO dao = new UserDAO();
		
//		int results = dao.insert(currentUser);
//		System.out.println(results + " rows inserted!");
//		
//		//TEST - FIND ALL
//		List<User> userList = dao.findAll();
//		System.out.println("User List: ");
//		for(User u : userList) {
//			System.out.println(u.toString());
//		}
//		
//		//TEST - FIND BY ID
//		int userid = 1011;
//		User thisUser = dao.findById(userid);
//		if(thisUser != null) {
//			System.out.println(thisUser.toString());
//		
//		//TEST - UPDATE USER
//			thisUser.setEmail("updatedEmail");
//			int updateResult = dao.update(thisUser);
//			System.out.println(updateResult + " rows updated!");
//		}
//		
//		//TEST - DELETE USER 
//		int deleteId = 1005;
//		int deleteResult = dao.delete(deleteId);
//		System.out.println(deleteResult + " rows deleted!");
		
		AccountDAO adao = new AccountDAO();
//		Account na = new Account(0, 200, AccountStatus.OPEN, AccountType.CHECKING);
//		int result = adao.insert(na, 1013);
//		System.out.println("Result: " + result);
//		//int result = adao.update(a);
//		List<Account> alist = adao.findAll();
//		//alist.add(na);
//		for (Account a : alist) {
//			a.setBalance(35000);
//		}
//		result = adao.updateList(alist);
//
//		System.out.println("Result: " + result);
		
		//System.out.println(adao.delete(1000014));
		
		List<Account> alist = adao.findByUser(1003);
		for (Account a : alist) {
			System.out.println(a.toString());
		}
		
		List<Account> aStatusList = adao.findByStatus(AccountStatus.OPEN);
		for (Account a : aStatusList) {
			System.out.println(a.toString());
		}
	}

}
