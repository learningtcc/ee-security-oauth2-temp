package com.eenet.authen;

import com.eenet.base.BaseEntity;
import com.eenet.user.EndUserInfo;

/**
 * 最终用户登录密码
 * @author Orion
 *
 */
public class EndUserCredential extends BaseEntity {
	private static final long serialVersionUID = 1650933617094538884L;
	private EndUserInfo endUser;//最终用户信息
	private String password;//用户登录密码（区别于账号登录密码）
	/**
	 * @return the 最终用户信息
	 */
	public EndUserInfo getEndUser() {
		return endUser;
	}
	/**
	 * @param endUser the 最终用户信息 to set
	 */
	public void setEndUser(EndUserInfo endUser) {
		this.endUser = endUser;
	}
	/**
	 * @return the 用户登录密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the 用户登录密码 to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}