package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import application.obj.InviteCode;
import application.util.LogUtil;
import databasePart1.DatabaseHelper;

/**
 * Acts as a manager class for dealing with InviteCode objects. This use of data
 * hiding and encapsulation helps ensure parity between data stored in the local
 * cache and data stored on the MySQL database.
 */
public class InviteCodeManager {

	private final DatabaseHelper database;
	private Set<InviteCode> cache = new HashSet<>();

	public InviteCodeManager(DatabaseHelper database) {
		this.database = database;
		LogUtil.debug("Initialized InviteCodeManager");
	}

	/**
	 * Populates the local cache.
	 */
	public void fetchInviteCodes() {
		this.cache.clear(); // Clear the cache before adding content from query
		String query = "SELECT * FROM InvitationCodes";
		try (ResultSet rs = this.database.getStatement().executeQuery(query)) {
			while (rs.next()) {
				String code = rs.getString("code");
				boolean isUsed = rs.getBoolean("isUsed");
				InviteCode invCode = new InviteCode(code, isUsed);
				this.cache.add(invCode);
			}
		} catch (SQLException e) {
			LogUtil.error(
					"Caught SQLException when trying to fetch data from InvitationCodes table. Printing stacktrace.");
			e.printStackTrace();
		}
	}

	/**
	 * Gets an unmodifiable {@link Set} containing {@link InviteCode} objects.
	 * 
	 * @return unmodifiable Set
	 */
	public Set<InviteCode> getInviteCodes() {
		return Collections.unmodifiableSet(this.cache);
	}

	public InviteCode createInviteCode() {
		String code = UUID.randomUUID().toString().substring(0, 4);
		boolean isUsed = false;

		String query = "INSERT INTO InvitationCodes (code, isUsed) VALUES (?, ?)";
		try (PreparedStatement pstmt = this.database.getConnection().prepareStatement(query)) {
			pstmt.setString(1, code);
			pstmt.setBoolean(2, isUsed);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LogUtil.error("Caught SQLException when trying to create a new invitation code. Printing stacktrace.");
			e.printStackTrace();
		}

		InviteCode invCode = new InviteCode(code, isUsed);
		this.cache.add(invCode);
		return invCode;
	}

	public boolean validateCode(InviteCode invCode) {
		String code = invCode.getCode();
		String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
		try (PreparedStatement pstmt = this.database.getConnection().prepareStatement(query)) {
			pstmt.setString(1, code);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			LogUtil.error("Caught SQLException when trying to validate an invitation code. Printing stacktrace.");
			e.printStackTrace();
		}
		return false;
	}

	public void markAsUsed(InviteCode invCode) {
		String code = invCode.getCode();
		String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
		try (PreparedStatement pstmt = this.database.getConnection().prepareStatement(query)) {
			pstmt.setString(1, code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LogUtil.error("Caught SQLException when trying to use an invitation code. Printing stacktrace.");
			e.printStackTrace();
		}

	}

	public void useCode(InviteCode invCode) {
		if (!this.validateCode(invCode))
			return; // Ignore invalid invite codes
		this.markAsUsed(invCode);
	}
}
