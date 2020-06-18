package com.kenny.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kenny.model.Account;
import com.kenny.model.AccountStatus;
import com.kenny.model.AccountType;
import com.kenny.util.ConnectionManager;

/**
 * AccountDAO implements the DAOInterface for simple CRUD operations. It
 * specifically interacts with Account objects and the Accounts database. It
 * also interacts with the user-account relation table in order to maintain
 * links between users and their accounts.
 * 
 * @author Kenny Chao
 */
public class AccountDAO implements DAOInterface<Account> {

	private final String TABLE_NAME = "BANK_ACCOUNTS";
	private final String REL_TABLE = "BANK_USERACCOUNTS";

	@Override
	/**
	 * Inserts a single row into the Accounts database with information from the
	 * Account object parameter.
	 * 
	 * @param a The Account Object that holds the data that will be inserted into
	 *          the database.
	 * @return int The method will return the row count of updated rows. This should
	 *         be 1 since we're only inserting a single row. 0 is returned if this
	 *         fails.
	 */
	public int insert(Account a) {
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);

			String sql = "INSERT INTO " + TABLE_NAME + " (balance, accountstatus, accounttype) VALUES (?, ?, ?)";

			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setDouble(1, a.getBalance());
				statement.setString(2, a.getStatus().toString());
				statement.setString(3, a.getType().toString());

				int result = statement.executeUpdate();

				conn.commit();

				return result;
			} catch (SQLException pse) {
				if (conn != null)
					conn.rollback();

				throw pse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Inserts a row into the Accounts database with information from the Account
	 * object parameter. This also inserts a row into the user-account relationship
	 * table that will map users to their accounts. Both updates should happen or
	 * changes should be rolledback if an error occurs.
	 * 
	 * @param a      The Account Object that holds the data that will be inserted
	 *               into the database.
	 * @param userid This is the id of the user that is related to the account
	 *               (owner)
	 * @return int The method will return the number of rows that have been updated
	 *         by the SQL statement. 0 is returned if this fails.
	 */
	public int insert(Account a, int userid) {
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);

			String sql = "INSERT INTO " + TABLE_NAME + " (balance, accountstatus, accounttype) VALUES (?, ?, ?)";

			try (PreparedStatement statement = conn.prepareStatement(sql, new String[] { "accountid" })) {
				statement.setDouble(1, a.getBalance());
				statement.setString(2, a.getStatus().toString());
				statement.setString(3, a.getType().toString());

				int result = statement.executeUpdate();

				if (result > 0) {
					ResultSet generatedKeys = statement.getGeneratedKeys();

					if (generatedKeys.next()) {
						a.setAccountId(generatedKeys.getInt(1));

						sql = "INSERT INTO " + REL_TABLE + " (userid, accountid) VALUES (?, ?)";

						PreparedStatement rel_statement = conn.prepareStatement(sql);

						rel_statement.setInt(1, userid);
						rel_statement.setInt(2, a.getAccountId());

						result = rel_statement.executeUpdate();

						if (result > 0) {
							conn.commit();
						}
					}
				}

				return result;
			} catch (SQLException pse) {
				if (conn != null)
					conn.rollback();

				throw pse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	/**
	 * Sends a query to the Accounts database to return all Accounts
	 * 
	 * @return List<Account> A list of all the accounts from the database stored as
	 *         Account objects. Returns null if no results or the operation has
	 *         failed.
	 */
	public List<Account> findAll() {
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM " + TABLE_NAME;

			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				ResultSet result = statement.executeQuery();

				List<Account> accountList = new ArrayList<>();

				while (result.next()) {
					accountList.add(createAccount(result));
				}

				return accountList;
			} catch (SQLException pse) {
				throw pse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	/**
	 * Sends a query to find the account in the database that matches the requested
	 * account id.
	 * 
	 * @param id The Account ID we're searching for.
	 * @return Account The account from the database stored as an Account object.
	 *         Returns null if no results or the operation has failed.
	 */
	public Account findById(int id) {
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE accountid = ?";

			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, id);

				ResultSet result = statement.executeQuery();

				while (result.next()) {
					Account acc = createAccount(result);

					return acc;
				}
			} catch (SQLException pse) {
				throw pse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Sends a query to find all accounts in the database that matches the requested
	 * AccountStatus.
	 * 
	 * @param s The account status we're searching.
	 * @return List<Account> A list of accounts that match the requested status.
	 *         Returns null if no results or the operation has failed.
	 */
	public List<Account> findByStatus(AccountStatus s) {
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE accountstatus = ?";

			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, s.toString());

				ResultSet result = statement.executeQuery();

				List<Account> accountList = new ArrayList<>();

				while (result.next()) {
					accountList.add(createAccount(result));
				}

				return accountList;
			} catch (SQLException pse) {
				throw pse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Sends a query to find all accounts in the database that matches the requested
	 * User ID.
	 * 
	 * @param userid The user id that matches the "owner" of the accounts.
	 * @return List<Account> A list of accounts that match the requested user.
	 *         Returns null if no results or the operation has failed.
	 */
	public List<Account> findByUser(int userid) {
		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT accountid FROM " + REL_TABLE + " WHERE userid = ?";

			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, userid);

				ResultSet result = statement.executeQuery();

				String newSql = "SELECT * FROM " + TABLE_NAME + " WHERE accountid = ?";

				List<Account> accountList = new ArrayList<Account>();

				while (result.next()) {
					PreparedStatement thisStatement = conn.prepareStatement(newSql);
					thisStatement.setInt(1, result.getInt("accountid"));

					ResultSet thisResult = thisStatement.executeQuery();

					while (thisResult.next()) {
						accountList.add(createAccount(thisResult));
					}
				}

				return accountList;
			} catch (SQLException pse) {
				throw pse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int update(Account a) {
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);

			int result = updateHelper(conn, a);

			if (result != 0 && conn != null) {
				conn.commit();
				return result;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int updateList(List<Account> alist) {
		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);

			int result = 0;

			for (Account a : alist) {
				result = updateHelper(conn, a);
				if (result == 0) {
					throw new SQLException("Unable to update account: " + a.getAccountId() + " Rolling back changes.");
				}
			}

			if (result != 0 && conn != null) {
				conn.commit();
				return result;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int delete(int id) {
		int result = 0;

		try (Connection conn = ConnectionManager.getConnection()) {
			conn.setAutoCommit(false);

			// 1. Delete all entries from the user-account relationship table first.
			String sql = "DELETE FROM " + REL_TABLE + " WHERE accountid = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, id);
				result += statement.executeUpdate();
			} catch (SQLException pse) {
				if (conn != null)
					conn.rollback();

				throw pse;
			}

			// 2. Now we can delete from the Accounts Table
			sql = "DELETE FROM " + TABLE_NAME + " WHERE accountid = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, id);
				result += statement.executeUpdate();
			} catch (SQLException pse) {
				if (conn != null)
					conn.rollback();
				throw pse;
			}

			// 3. If everything went through, commit the changes.
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	// HELPER METHODS

	/**
	 * Creates an Account object from the given ResultSet and returns it.
	 * 
	 * @param result The ResultSet object from a query.
	 * @return Account The object created by this method to be returned. Returns
	 *         null if no results or the operation has failed.
	 */
	private Account createAccount(ResultSet result) throws SQLException {
		try {
			int accountId = result.getInt("accountid");
			double bl = result.getDouble("balance");
			String as = result.getString("accountstatus");
			String at = result.getString("accounttype");

			try {
				AccountStatus ase = null;
				AccountType ate = null;

				if (as != null) {
					ase = AccountStatus.valueOf(as);
				}

				if (at != null) {
					ate = AccountType.valueOf(at);
				}

				Account thisAccount = new Account(accountId, bl, ase, ate);
				return thisAccount;
			} catch (IllegalArgumentException iae) {
				throw iae;
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}

		return null;
	}

	/**
	 * A method to prepare and send an UPDATE statement to the database.
	 * 
	 * @param conn The existing Connection object.
	 * @param a    The Account to be updated in the database.
	 * @return int The number of rows updated. Returns null if the operation has
	 *         failed.
	 */
	private int updateHelper(Connection conn, Account a) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET balance = ?, " + "accountstatus = ?, " + "accounttype = ? "
				+ "WHERE accountid = ?";

		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setDouble(1, a.getBalance());
			statement.setString(2, a.getStatus().toString());
			statement.setString(3, a.getType().toString());
			statement.setInt(4, a.getAccountId());

			int result = statement.executeUpdate();
			return result;
		} catch (SQLException pse) {
			throw pse;
		}
	}
}
