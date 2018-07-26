package com.toni.sdz.server;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Connection;
import com.toni.sdz.client.AppService;
import com.toni.sdz.shared.User;
import com.mysql.jdbc.Driver;
import java.sql.PreparedStatement;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppServiceImpl extends RemoteServiceServlet implements AppService {

	@Override
	public String loginUser(User u) {
		String userName = u.getUserName();
		String password = u.getPassword();
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			String query = "SELECT username, password, employee_type FROM mydb.users WHERE username='" + u.getUserName()
					+ "'";
			resultSet = statement.executeQuery(query);
			if (!resultSet.next()) {
				return "Korisnik ne postoji!";
			} else {
				if (u.getPassword().equals(resultSet.getString("password"))) {
					String employeeType = resultSet.getString("employee_type");
					if (employeeType.equals("Radnik"))
						return "Radnik";
					else if (employeeType.equals("Sef"))
						return "Sef";
					else if (employeeType.equals("Direktor"))
						return "Direktor";
				} else {
					return "Lozinka netocna!";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	@Override
	public boolean registerUser(User u) {
		String query = null;
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		query = "SELECT username FROM mydb.users WHERE username='" + u.getUserName() + "'";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			if (!resultSet.next()) {
				query = "INSERT INTO mydb.users (username, password, name, surname, employee_type) VALUES(?,?,?,?,?)";
				PreparedStatement pStat = conn.prepareStatement(query);
				pStat.setString(1, u.getUserName());
				pStat.setString(2, u.getPassword());
				pStat.setString(3, u.getName());
				pStat.setString(4, u.getSurname());
				pStat.setString(5, u.getEmployee_type());
				pStat.executeUpdate();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SQLEXCEPTION");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public String getName(String username) {
		String name = null, surname = null;
		String query = null;
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		query = "SELECT name, surname, username FROM mydb.users WHERE username='" + username + "'";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				name = resultSet.getString("name");
				surname = resultSet.getString("surname");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return name + " " + surname;
	}

	@Override
	public ArrayList<String>[] fetchData(String username, int who, String query) {
		ArrayList<String>[] returnArray = new ArrayList[2];
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> list1 = new ArrayList<>();
		ArrayList<String> list2 = new ArrayList<>();
		// ListGridRecord rec = new ListGridRecord();
		/*
		 * "SELECT * FROM nalozi " +
		 * "INNER JOIN nalozi_has_users ON (nalozi.ID=nalozi_has_users.nalozi_ID AND nalozi_has_users.users_username='"
		 * + username + "')";
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			if (who == 0) {
				while (resultSet.next()) {
					if (resultSet.getString("status").equals("empty")) {
						list1.add(resultSet.getString("ID"));
						list1.add(resultSet.getString("vrsta"));
						list1.add(resultSet.getString("opis"));
						list1.add(getName(resultSet.getString("zadao")));
						list1.add(resultSet.getString("datum"));
					} else {
						list2.add(resultSet.getString("opis"));
						list2.add(getName(resultSet.getString("zadao")));
						list2.add(resultSet.getString("datum"));
						list2.add(resultSet.getString("komentar"));
						list2.add(resultSet.getString("status"));
					}
				}

			} else if (who == 1) {
				while (resultSet.next()) {
					if (resultSet.getString("status").equals("empty")) {
						list1.add(resultSet.getString("vrsta"));
						list1.add(resultSet.getString("opis"));
						list1.add(resultSet.getString("datum"));
						String kome = resultSet.getString("kome");
						if (kome == null) {
							String result = "Opci nalog";
							String rezervirao = resultSet.getString("rezervirao");
							if (rezervirao != null) {
								rezervirao = getName(rezervirao);
								result += " <b>rezervirao:</b> " + rezervirao;
							}
							list1.add(result);
						} else {
							list1.add(getName(kome));
						}
					} else {
						list2.add(resultSet.getString("opis"));
						list2.add(resultSet.getString("datum"));
						list2.add(getName(resultSet.getString("rezervirao")));
						list2.add(resultSet.getString("komentar"));
						list2.add(resultSet.getString("status"));
					}
				}
			} else {
				while (resultSet.next()) {
					if (resultSet.getString("status").equals("empty")) {
						list1.add(resultSet.getString("vrsta"));
						list1.add(resultSet.getString("opis"));
						list1.add(getName(resultSet.getString("zadao")));
						list1.add(resultSet.getString("datum"));
						String kome = resultSet.getString("kome");
						if (kome == null) {
							String result = "Opci nalog";
							String rezervirao = resultSet.getString("rezervirao");
							if (rezervirao != null) {
								rezervirao = getName(rezervirao);
								result += " <b>rezervirao:</b> " + rezervirao;
							}
							list1.add(result);
						} else {
							list1.add(getName(kome));
						}
					} else {
						list2.add(resultSet.getString("opis"));
						list2.add(getName(resultSet.getString("zadao")));
						list2.add(resultSet.getString("datum"));
						list2.add(getName(resultSet.getString("rezervirao")));
						list2.add(resultSet.getString("komentar"));
						list2.add(resultSet.getString("status"));
					}
				}
			}
			returnArray[0] = list1;
			returnArray[1] = list2;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnArray;
	}

	@Override
	public boolean reserveOrder(int ID, String username) {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			String query = "SELECT rezervirao FROM mydb.nalozi WHERE ID='" + ID + "'";
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				if (resultSet.getString("rezervirao") == null || resultSet.getString("rezervirao") == "") {
					query = "UPDATE mydb.nalozi SET rezervirao='" + username + "'" + "WHERE ID='" + ID + "'";
					statement.executeUpdate(query);
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void unreserveOrder(int ID) {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			String query = "UPDATE mydb.nalozi SET rezervirao=NULL WHERE ID='" + ID + "'";
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void executeOrder(int ID, String comment) {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy.");
		/*
		 * String time = Integer.toString(date.getHours()) + ":" +
		 * Integer.toString(date.getMinutes()) + ":" +
		 * Integer.toString(date.getSeconds()) + " " +
		 * Integer.toString(date.getDay()) + "." +
		 * Integer.toString(date.getMonth()) + "." +
		 * Integer.toString(date.getYear()) + ".";
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			String query = "UPDATE mydb.nalozi SET status='" + formatter.format(new Date()) + "', komentar='" + comment
					+ "' WHERE ID='" + ID + "'";
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public HashMap<String, String> updateComboBox() {
		HashMap<String, String> users = new HashMap<>();
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			String query = "SELECT username, name, surname FROM mydb.users WHERE employee_type='Radnik'";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				users.put(resultSet.getString("username"),
						resultSet.getString("name") + " " + resultSet.getString("surname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}

	@Override
	public void makeOrder(String description, String username, String employee, int vrsta) {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy.");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root",
					"root");
			statement = conn.createStatement();
			String query = "INSERT INTO mydb.nalozi (opis, zadao, kome, datum, vrsta) VALUES (?,?,?,?,?)";
			PreparedStatement pStat = conn.prepareStatement(query);
			pStat.setString(1, description);
			pStat.setString(2, username);
			pStat.setString(3, employee);
			pStat.setString(4, formatter.format(new Date()));
			pStat.setString(5, Integer.toString(vrsta));
			pStat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}