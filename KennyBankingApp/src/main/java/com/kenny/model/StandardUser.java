package com.kenny.model;

public class StandardUser extends User {
	
	public StandardUser() {
		super();
	}

	public StandardUser(int userId, String firstName, String lastName, String email, String userName, String password, Role role) {
		super(userId, firstName, lastName, email, userName, password, role);
	}
	
	
}
