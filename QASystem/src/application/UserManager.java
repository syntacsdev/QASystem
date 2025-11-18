package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import application.util.LogUtil;
import databasePart1.DatabaseHelper;

public class UserManager {

	private final DatabaseHelper database;
	private Set<User> userSet = new HashSet<>();

	public UserManager(DatabaseHelper helper) {
		this.database = helper;
		LogUtil.debug("Initialized QuestionManager");
	}

	public Set<User> getUserSet() {
		return Collections.unmodifiableSet(this.userSet);
	}

	public void fetchUsers() {
		this.userSet.clear();
		String query = "SELECT * FROM cse360users";
		try (ResultSet rs = this.database.getStatement().executeQuery(query)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String userName = rs.getString("userName");
				String password = rs.getString("password");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String email = rs.getString("email");
				UserRole role = UserRole.valueOf(rs.getString("role").toUpperCase());
				User u = new User(id, userName, password, firstName, lastName, email, role);
				this.userSet.add(u);
			}
		} catch (SQLException e) {
			LogUtil.error("Caught SQLException when trying to fetch users. Printing stacktrace.");
			e.printStackTrace();
		}
	}
}
