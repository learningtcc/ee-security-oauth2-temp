package com.eenet.authen;

import com.eenet.base.SimpleResponse;
import com.eenet.base.StringResponse;
import com.eenet.util.cryptography.RSADecrypt;

/**
 * 最终用户登录秘钥服务
 * 2016年4月7日
 * @author Orion
 */
public interface EndUserCredentialBizService {
	/**
	 * 初始化用户登录密码
	 * @param mainAccount
	 * @param credential 
	 * @return
	 * 2016年3月31日
	 * @author Orion
	 */
	public SimpleResponse initEndUserLoginPassword(EndUserCredential credential);
	
	/**
	 * 修改用户主登录密码
	 * @param mainAccount
	 * @param curCredential
	 * @param newSecretKey
	 * @return
	 * 2016年3月31日
	 * @author Orion
	 */
	public SimpleResponse changeEndUserLoginPassword(EndUserCredential curCredential, String newSecretKey);
	
	/**
	 * 重置用户登录密码（适合忘记密码）
	 * @param endUserId 用户标识
	 * @return
	 * 2016年3月31日
	 * @author Orion
	 */
	public SimpleResponse resetEndUserLoginPassword(String endUserId);
	
	/**
	 * 获得用户登录密码（密文）
	 * @param endUserId 用户标识
	 * @return
	 * 2016年4月7日
	 * @author Orion
	 */
	public StringResponse retrieveEndUserSecretKey(String endUserId);
	
	/**
	 * 获得用户登录密码（明文）
	 * @param endUserId 用户标识
	 * @param redisRSADecrypt 解密参数
	 * @return
	 * 2016年4月7日
	 * @author Orion
	 */
	public StringResponse retrieveEndUserSecretKey(String endUserId, RSADecrypt StorageRSAEncrypt);
}
