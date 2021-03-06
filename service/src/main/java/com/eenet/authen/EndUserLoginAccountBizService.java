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
	 * 根据登录账号获得最终用户登陆账号其他信息
	 * @param loginAccount
	 * @return 返回对象中带有已经加密的账号登录秘钥
	 * 2016年6月8日
	 * @author Orion
	 */
	public EndUserLoginAccount retrieveEndUserLoginAccountInfo(String loginAccount);
	
	/**
	 * 获得登录账号私有密码（明文）
	 * @param loginAccount 解密参数（如果确定不是RSA加密则可空，否则不为空）
	 * @param StorageRSAEncrypt 
	 * @return 如果是RSA加密形式则返回明文，否则返回密文。返回对象只包含加密方式和密码明文（或密文）
	 */
	public EndUserLoginAccount retrieveEndUserAccountPassword(String loginAccount, RSADecrypt StorageRSAEncrypt);
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
