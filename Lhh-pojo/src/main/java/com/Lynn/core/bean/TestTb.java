package com.Lynn.core.bean;

import java.io.Serializable;
import java.util.Date;

public class TestTb implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private Date birthday;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "TestTb{" +
				"id=" + id +
				", username='" + username + '\'' +
				", birthday=" + birthday +
				'}';
	}
}
