package respositry;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Event;
import model.User;
import service.TestLogger;

import com.google.common.base.Throwables;


public class LocalDataBase {

	private String url = "jdbc:sqlite:db/test.db";

	private static class LocalDataBaseHolder {
		private static final LocalDataBase my = new LocalDataBase();
	}
	
	public static LocalDataBase getInstance() {
		return LocalDataBaseHolder.my;
	}
	
	private LocalDataBase() {
		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				TestLogger.trace(String.format("A new Database has been created, the driver name is: %s", meta.getDriverName()));
			}
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS events (id text not null, eventname text not null, status text not null)";
		String sql2 = "CREATE TABLE IF NOT EXISTS user (id text PRIMARY KEY, username text not null, regurl text)";
		String sql3 = "CREATE TABLE IF NOT EXISTS user_assessment (username text not null, actname text not null, submissiontime text not null)";
		String sql4 = "CREATE TABLE IF NOT EXISTS team_students (teamname text not null, usernames text, status text)";
		try (Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			stmt.execute(sql2);
			stmt.execute(sql3);
			stmt.execute(sql4);
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public void addEvent(String eventid, String eventName, String isOriginal) {
		String sql = "INSERT INTO events(id, eventname, status, isoriginal) VALUES(?, ?, 1, ?)";
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, eventid);
			stmt.setString(2, eventName);
			stmt.setString(3, isOriginal);
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public void updateEventStatus(String eventid) {
		String sql = "UPDATE events SET status = 0 WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, eventid);
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public Event getUserEvent(String eventid) {
		String sql = "SELECT id, eventname, isoriginal FROM events WHERE id = ? and status = 1";
		ResultSet rs = null;
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, eventid);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return new Event(rs.getString("id"), rs.getString("eventname"), rs.getString("isoriginal"));
			}
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			if (rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
				}
			}
		}
		return null;
	}
	
	public Event getCurrentEvent() {
		String sql = "SELECT id, eventname, isoriginal FROM events WHERE status = 1";
		ResultSet rs = null;
		
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return new Event(rs.getString("id"), rs.getString("eventname"), rs.getString("isoriginal"));
			}
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			if (rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
				}
			}
		}
		return null;
	}
	
	public User getCurrnetUser(int userSystemid) {
		String sql = String.format("SELECT username, regurl FROM user where id = %s", userSystemid);
		User user = null;
		ResultSet rs = null;
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			rs = stmt.executeQuery();
			while(rs.next()) {
				user = new User(rs.getString("username"), rs.getString("regurl"));
			}
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			if (rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
				}
			}
		}
		return user;
	}
	
	public void addUser(int id, String username, String regUrl) {
		String sql = "REPLACE INTO user(id, username, regurl) VALUES(?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.setString(2, username);
			stmt.setString(3, regUrl);
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public int getUserSubmissionNumber(String userName, String activityName) {
		String sql = "SELECT count(1) FROM user_assessment WHERE username = ? and actname = ?";
		ResultSet rs = null;
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			stmt.setString(2, activityName);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			if (rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
				}
			}
		}
		return -1;
	}
	
	public void addUserSubmission(String userName, String activityName, String submissionTime) {
		String sql = "INSERT INTO user_assessment(username, actname, submissiontime) VALUES(?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			stmt.setString(2, activityName);
			stmt.setString(3, submissionTime);
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public String getActiveTeam() {
		String sql = "SELECT teamname FROM team_students WHERE status = 1";
		ResultSet rs = null;
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return rs.getString(1) == null? "" : rs.getString(1);
			}
			return "";
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			if (rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
				}
			}
		}
		return null;
	}
	
	public void createATeam(String teamName) {
		String sql = "INSERT INTO team_students(teamname, usernames, status) VALUES(?, ?, 1)";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, teamName);
			stmt.setString(2, "");
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public String getTeamStudents(String teamName) {
		String sql = "SELECT usernames FROM team_students WHERE teamname = ? and status = 1";
		ResultSet rs = null;
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, teamName);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return rs.getString(1) == null? "" : rs.getString(1);
			}
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			if (rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
				}
			}
		}
		return null;
	}
	
	public void addAStudentIntoATeam(String teamName, String studentName) {
		String t = String.format("%s;%s", getTeamStudents(teamName), studentName);
		
		String sql = "UPDATE team_students SET usernames = ? WHERE teamname = ? and status = 1";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, t);
			stmt.setString(2, teamName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
	public void invalidateATeam(String teamName) {
		
		String sql = "UPDATE team_students SET status = 0 WHERE teamname = ?";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, teamName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}
	
}
