package com.kenny.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kenny.model.Role;
import com.kenny.model.User;
import com.kenny.util.ConnectionManager;

public class UserDAO implements DAOInterface<User> {

	private final String TABLE_NAME = "BANK_USERS";
	
// CREATE METHODS
	// insert data into DB. Generic E should be overridden with User or Account objects 
	@Override
	public int insert(User u) {
		System.out.println("INVOKE INSERT USER METHOD");
		
		// Connect to Database with ConnectionManager Class
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "INSERT INTO " + TABLE_NAME + 
					" (firstname, lastname, username, pass, email, role) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, u.getFirstName());
				statement.setString(2, u.getLastName());
				statement.setString(3, u.getUserName());
				statement.setString(4, u.getPassword());
				statement.setString(5, u.getEmail());
				statement.setString(6, u.getRole().toString());
				int result = statement.executeUpdate();
				conn.commit();
				return result;
			} catch (SQLException pse) {
				if(conn != null) 
					conn.rollback();
				throw pse;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
//READ METHODS
	// run a query to find all rows of Users and store in a List
	@Override
	public List<User> findAll() {
		System.out.println("INVOKE READ USERS METHOD - FIND ALL");
		
		// Connect to Database with ConnectionManager Class
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM " + TABLE_NAME;
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				ResultSet result = statement.executeQuery();
				List<User> userList = new ArrayList<>();
				while(result.next()) {
					userList.add(createUser(result));
				}
				return userList;
			} catch (SQLException pse) {
				throw pse;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// run a query to find a User DB entry that matches a certain id. Returns object of type User.
	@Override
	public User findById(int id) {
		System.out.println("INVOKE READ USERS METHOD - FIND BY ID");
		
		// Connect to Database with ConnectionManager Class
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userid = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, id);
				ResultSet result = statement.executeQuery();
				while(result.next()) {
					User user = createUser(result);
					return user;
				}
			} catch (SQLException pse) {
				throw pse;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public User findByUsername(String username) {
		System.out.println("INVOKE READ USERS METHOD - FIND BY USERNAME");
		
		// Connect to Database with ConnectionManager Class
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, username);
				ResultSet result = statement.executeQuery();
				while(result.next()) {
					User user = createUser(result);
					return user;
				}
			} catch (SQLException pse) {
				throw pse;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//UPDATE METHODS
	@Override
	public int update(User u) {
		System.out.println("INVOKE UPDATE USER METHOD");
		// Connect to Database with ConnectionManager Class
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "UPDATE " + TABLE_NAME + " SET firstname = ?, " +
													"lastname = ?, " + 
													"username = ?, " +
													"pass = ?, " +
													"email = ?, " + 
													"role = ? " +
													"WHERE userid = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, u.getFirstName());
				statement.setString(2, u.getLastName());
				statement.setString(3, u.getUserName());
				statement.setString(4, u.getPassword());
				statement.setString(5, u.getEmail());
				statement.setString(6, u.getRole().toString());
				statement.setInt(7, u.getUserId());
				int result = statement.executeUpdate();
				conn.commit();
				return result;
			} catch (SQLException pse) {
				if(conn != null) 
					conn.rollback();
				throw pse;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}		
		return 0;
	}
	
//DELETE METHODS
	@Override
	public int delete(int id) {
		System.out.println("INVOKE DELETE USER METHOD");
		// Connect to Database with ConnectionManager Class
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE userid = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, id);
				int result = statement.executeUpdate();
				conn.commit();
				return result;
			} catch (SQLException pse) {
				if(conn != null) 
					conn.rollback();
				throw pse;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}		
		return 0;
	}
	
//HELPER METHODS
	
	private User createUser(ResultSet result) throws SQLException {
		try {	
			int userid = result.getInt("userid");
			String fn = result.getString("firstname");
			String ln = result.getString("lastname");
			String em = result.getString("email");
			String un = result.getString("username");
			String pw = result.getString("pass");
			String rl = result.getString("role");
			try {
				Role rle = null;
				if(rl != null) {
					rle = Role.valueOf(rl);
				}
				User thisUser = new User(userid, fn, ln, em, un, pw, rle);
				return thisUser;
			} catch (IllegalArgumentException iae) {
				throw iae;
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return null;
	}
}
