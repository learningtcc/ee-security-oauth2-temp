package com.eenet.authen.cacheSyn;

/**
 * 认证模块在缓存中的标识
 * @author Orion
 * 2016年6月6日
 */
public final class AuthenCacheKey {
	
	/**
	 * 业务应用系统
	 * redisKey:BIZ_APP, mapKey:appId，value:序列化的BusinessApp对象
	 */
	public final static String BIZ_APP = "BIZ_APP";
	/**
	 * 服务人员登录账号
	 * redisKey:ADMINUSER_LOGIN_ACCOUNT, mapKey:登录账号，value:服务人员对象(@see com.eenet.authen.AdminUserLoginAccount)
	 */
	public final static String ADMINUSER_LOGIN_ACCOUNT = "ADMINUSER_LOGIN_ACCOUNT";
	/**
	 * 服务人员登录密码
	 * redisKey:ADMINUSER_CREDENTIAL, mapKey:对应服务人员ID，value:服务人员登录密码(String)
	 */
	public final static String ADMINUSER_CREDENTIAL = "ADMINUSER_CREDENTIAL";
	/**
	 * 最终用户登录账号
	 * redisKey:ENDUSER_LOGIN_ACCOUNT, mapKey:登录账号，value:最终用户对象(@see com.eenet.authen.EndUserLoginAccount)
	 */
	public final static String ENDUSER_LOGIN_ACCOUNT = "ENDUSER_LOGIN_ACCOUNT";
	/**
	 * 最终用户登录密码
	 * redisKey:ENDUSER_CREDENTIAL, mapKey:对应最终用户ID，value:最终用户登录密码(String)
	 */
	public final static String ENDUSER_CREDENTIAL = "ENDUSER_CREDENTIAL";
	
	
	private AuthenCacheKey() {}
}
