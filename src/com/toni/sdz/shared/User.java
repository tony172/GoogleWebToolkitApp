package com.toni.sdz.shared;

import java.io.Serializable;

public class User implements Serializable{
	private String userName;
	private String password;
	private String name;
	private String surname;
	private String employee_type;
	
	public User(){};
	
	public User(String userName, String password){
		this.userName=userName;
		this.password=Integer.toString(password.hashCode());
	}
	
	public User(String username, String password, String name, String surname, String employee_type){
		this.userName = username;
		this.password = Integer.toString(password.hashCode());
		this.name = name;
		this.surname = surname;
		this.employee_type = employee_type;
	
	
}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmployee_type() {
		return employee_type;
	}

	public void setEmployee_type(String employee_type) {
		this.employee_type = employee_type;
	}
}
