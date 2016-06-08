package com.eenet.authen;

import com.eenet.base.BaseEntity;
import com.eenet.common.BackupDeletedData;
import com.eenet.common.BackupUpdatedData;
import com.eenet.user.EndUserInfo;

/**
 * 最终用户登录账号信息
 * @author Orion
 *
 */
public class EndUserLoginAccount extends BaseEntity implements BackupDeletedData,BackupUpdatedData {
	private static final long serialVersionUID = -6762737260719096975L;
	private EndUserInfo userInfo;//用户基本信息
	private String loginAccount;//登录账号
	private LoginAccountType accountType;//登录账号类型
	private String accountLoginPassword;//账号登录密码
	/**
	 * @return the 用户基本信息
	 */
	public EndUserInfo getUserInfo() {
		return userInfo;
	}
	/**
	 * @param userInfo the 用户基本信息 to set
	 */
	public void setUserInfo(EndUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	/**
	 * @return the 登录账号
	 */
	public String getLoginAccount() {
		return loginAccount;
	}
	/**
	 * @param loginAccount the 登录账号 to set
	 */
	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}
	/**
	 * @return the 登录账号类型
	 */
	public LoginAccountType getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the 登录账号类型 to set
	 */
	public void setAccountType(LoginAccountType accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the 账号登录密码
	 */
	public String getAccountLoginPassword() {
		return accountLoginPassword;
	}
	/**
	 * @param accountLoginPassword the 账号登录密码 to set
	 */
	public void setAccountLoginPassword(String accountLoginPassword) {
		this.accountLoginPassword = accountLoginPassword;
	}
}
