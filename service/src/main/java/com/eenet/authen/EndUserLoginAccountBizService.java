package com.eenet.authen;

import com.eenet.base.SimpleResponse;
import com.eenet.base.StringResponse;
import com.eenet.user.EndUserInfo;
import com.eenet.util.cryptography.RSADecrypt;

/**
 * 最终用户登录账号服务
 * 2016年4月7日
 * @author Orion
 */
public interface EndUserLoginAccountBizService {
	/**
	 * 最终用户登录账号注册
	 * @param user
	 * @return
	 */
	public EndUserLoginAccount registeEndUserLoginAccount(EndUserLoginAccount account);
	/**
	 * 最终用户登录账号废弃
	 * @param code
	 * @return
	 * 2016年3月30日
	 * @author Orion
	 */
	public SimpleResponse removeEndUserLoginAccount(String... loginAccounts);
	/**
	 * 根据登录账号获得人员基本信息（返回信息不包含登录账号的私有密码）
	 * @param loginAccount 登录账号
	 * @return 主账号
	 * 2016年4月7日
	 * @author Orion
	 */
	public EndUserInfo retrieveEndUserInfo(String loginAccount);
	

	/**
	 * 获得登录账号私有密码（密文）
	 * @param loginAccount
	 * @return
	 */
	public StringResponse retrieveUserSecretKey(String loginAccount);
	
	/**
	 * 获得登录账号私有密码（明文）
	 * @param loginAccount
	 * @param StorageRSAEncrypt
	 * @return
	 */
	public StringResponse retrieveUserSecretKey(String loginAccount, RSADecrypt StorageRSAEncrypt);
	/**
	 * 不应该再考虑维护账号的私有密码
	 */
//	/**
//	 * 初始化登录账号私有密码
//	 * @param loginAccount
//	 * @param secretKey
//	 * @return
//	 */
//	public SimpleResponse initAccountLoginPassword(String loginAccount, String secretKey);
//	
//	/**
//	 * 修改登录账号私有密码
//	 * @param loginAccount
//	 * @param curSecretKey
//	 * @param newSecretKey
//	 * @return
//	 */
//	public SimpleResponse changeAccountLoginPassword(String loginAccount, String curSecretKey, String newSecretKey);
//	
//	/**
//	 * 重置登录账号私有密码（适合忘记密码）
//	 * @param loginAccount
//	 * @return
//	 */
//	public SimpleResponse resetUserLoginPassword(String loginAccount);
}
