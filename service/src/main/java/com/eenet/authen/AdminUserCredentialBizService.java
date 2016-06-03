package com.eenet.authen;

import com.eenet.base.SimpleResponse;
import com.eenet.base.StringResponse;
import com.eenet.util.cryptography.RSADecrypt;

/**
 * 服务人员登录秘钥服务
 * 2016年4月7日
 * @author Orion
 */
public interface AdminUserCredentialBizService {
	/**
	 * 初始化服务人员登录密码
	 * @param mainAccount
	 * @param credential 
	 * @return
	 * 2016年3月31日
	 * @author Orion
	 */
	public SimpleResponse initAdminUserLoginPassword(AdminUserCredential credential);
	
	/**
	 * 修改服务人员登录密码
	 * @param mainAccount
	 * @param curCredential
	 * @param newSecretKey
	 * @return
	 * 2016年3月31日
	 * @author Orion
	 */
	public SimpleResponse changeAdminUserLoginPassword(AdminUserCredential curCredential, String newSecretKey);
	
	/**
	 * 重置服务人员登录密码（适合忘记密码）
	 * @param endUserId 用户标识
	 * @return
	 * 2016年3月31日
	 * @author Orion
	 */
	public SimpleResponse resetAdminUserLoginPassword(String adminUserId);
	
	/**
	 * 获得服务人员登录密码（密文）
	 * @param endUserId 用户标识
	 * @return
	 * 2016年4月7日
	 * @author Orion
	 */
	public StringResponse retrieveAdminUserSecretKey(String adminUserId);
	
	/**
	 * 获得服务人员登录密码（明文）
	 * @param endUserId 用户标识
	 * @param redisRSADecrypt 解密参数
	 * @return
	 * 2016年4月7日
	 * @author Orion
	 */
	public StringResponse retrieveAdminUserSecretKey(String adminUserId, RSADecrypt StorageRSAEncrypt);
}
