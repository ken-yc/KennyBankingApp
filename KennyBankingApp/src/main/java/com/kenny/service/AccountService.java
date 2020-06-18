package com.kenny.service;

import java.util.ArrayList;
import java.util.List;

import com.kenny.dao.AccountDAO;
import com.kenny.model.Account;
import com.kenny.model.AccountStatus;
import com.kenny.model.AccountType;

public class AccountService {

	private AccountDAO dao = new AccountDAO();

	public int insert(Account a) {
		return dao.insert(a);
	}

	public int insert(Account a, int userid) {
		return dao.insert(a, userid);
	}

	public List<Account> findAll() {
		return dao.findAll();
	}

	public Account findById(int id) {
		return dao.findById(id);
	}

	public List<Account> findByStatus(AccountStatus s) {
		return dao.findByStatus(s);
	}

	public List<Account> findByUser(int id) {
		return dao.findByUser(id);
	}

	public int update(Account a) {
		return dao.update(a);
	}

	public int updateList(List<Account> a) {
		return dao.updateList(a);
	}

	public int delete(int id) {
		return dao.delete(id);
	}

//	public List<String> listAccounts() {
//		List<Account> account = findAll();
//		List<String> accountList = new ArrayList<String>();
//		if(account != null) {
//			for (Account a : account) {
//				accountList.add(String.valueOf(a.getAccountId()));
//			}
//			return accountList;
//		} else {
//			return null;
//		}
//	}

	public int withdraw(int id, double amount) {
		Account a = findById(id);
		if (a != null) {
			double balance = a.getBalance();
			a.setBalance(balance - amount);
			return update(a);
		}
		return 0;
	}

	public int deposit(int id, double amount) {
		Account a = findById(id);
		if (a != null) {
			double balance = a.getBalance();
			a.setBalance(balance + amount);
			return update(a);
		}
		return 0;
	}

	public int transfer(int source, int target, double amount) {
		Account sourceAcc = findById(source);
		Account targetAcc = findById(target);
		List<Account> accToUpdate = new ArrayList<Account>();
		if (sourceAcc != null && targetAcc != null) {
			double sourceBalance = sourceAcc.getBalance();
			double targetBalance = targetAcc.getBalance();

			sourceAcc.setBalance(sourceBalance - amount);
			targetAcc.setBalance(targetBalance + amount);

			accToUpdate.add(sourceAcc);
			accToUpdate.add(targetAcc);

			return updateList(accToUpdate);
		}
		return 0;
	}

	public int accrueInterest(int months) {

		List<Account> accounts = findAll();
		List<Account> accToUpdate = new ArrayList<Account>();

		for (Account a : accounts) {
			if (a.getType().equals(AccountType.SAVINGS) && a.getStatus().equals(AccountStatus.OPEN)) {
				for (int i = 0; i < months; i++) {
					double rate = a.getInterestRate();
					double currentBalance = a.getBalance();

					double newBalance = currentBalance + (currentBalance * rate);
					a.setBalance(newBalance);
				}
				accToUpdate.add(a);
			}
		}
		if (!accToUpdate.isEmpty()) {
			return updateList(accToUpdate);
		} else {
			return 0;
		}
	}
}
