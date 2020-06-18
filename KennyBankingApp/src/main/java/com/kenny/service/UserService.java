package com.kenny.service;

import java.util.ArrayList;
import java.util.List;

import com.kenny.dao.UserDAO;
import com.kenny.model.User;

public class UserService {

	private UserDAO dao = new UserDAO();
	
	private int insert(User u) {
		return dao.insert(u);
	}
	
	public List<User> findAll() {
		return dao.findAll();
	}
	
	public User findById(int id) {
		return dao.findById(id);
	}
	
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
	public int update(User u) {
		return dao.update(u);
	}
	
	public int delete(int id) {
		return dao.delete(id);
	}
	
	// Handle the user's login and returns the user's information in an object
	public User login(String username, String password) {	
		User userFromDB = findByUsername(username);
		
		// Username was incorrect
		if(userFromDB == null) {
			return null;
		}
		
		// Username was correct and so was password
		if(userFromDB.getPassword().equals(password)) {
			return userFromDB;
		}
		
		// Username was correct, but password was not
		return null;
	}
	
	// Handles User Registration and checks for existing user names.
	public int registerUser(User u) {
		User existingUser = findByUsername(u.getUserName());
		
		// Username was incorrect
		if(existingUser != null) {
			return 0;
		} else {
			int result = insert(u);
			return result;
		}
	}
	
	// Method to return all user names (necessary?)
	public List<String> listUsers() {
		List<User> users = findAll();
		List<String> userList = new ArrayList<String>();
		for (User u : users) {
			userList.add(u.getUserName());
		}
		return userList;
	}

}
