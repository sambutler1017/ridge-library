package com.ridge.test.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Class to create a user profile object
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
public class User {
	private int id;

	private String firstName;

	private String lastName;

	private String email;

	private Object webRole;

	private Boolean appAccess;

	private Boolean emailReportsEnabled;

	private String password;

	private String storeId;

	private String storeName;

	private Object accountStatus;

	private Date hireDate;

	private Date insertDate;

	private Date lastLoginDate;

	@JsonInclude(Include.NON_DEFAULT)
	private long salt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastname) {
		this.lastName = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Object getWebRole() {
		return webRole;
	}

	public void setWebRole(Object webRole) {
		this.webRole = webRole;
	}

	public Boolean isAppAccess() {
		return appAccess;
	}

	public void setAppAccess(Boolean appAccess) {
		this.appAccess = appAccess;
	}

	public Boolean isEmailReportsEnabled() {
		return emailReportsEnabled;
	}

	public void setEmailReportsEnabled(Boolean emailReportsEnabled) {
		this.emailReportsEnabled = emailReportsEnabled;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Object getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Object accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public long getSalt() {
		return salt;
	}

	public void setSalt(long salt) {
		this.salt = salt;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
}
