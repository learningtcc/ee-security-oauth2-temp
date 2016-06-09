package com.eenet.authen;

//备用UUID
//2F4766CE823D46528C8DF40B3F09266D - 给职工教育网做appid（谭云），秘钥：aSder%82
//CACE8BB8E1DA49E193D4C0A59C744A67
//DB923A0CC7C740BA8AC7CF90D4D36494
//A1A5979594A343F5BCECF34CAEE259D3
//943CC17AE4484510B7B9C26D820B25F9
//8220B5DD54054520A4B46E1B0E82E310
//72038587CE07483C87BFA8FA0DD42279
//802BA72FCAAD4CC0B9070949BE21392D
//90AFC82862624BF9BEBABBD49198BC3F
//7E437989860F4C08AD06210D0E0F194A
public class APIRequestIdentity implements java.io.Serializable {
	//临时参数
	public static final String GRANT_CODE = "C1A3285B43154C06BB1B422967F81B3F";
	public static final String ACCESS_TOKEN = "8E2305C0AB8B400B8A280828D1A94C6C";
	public static final String FRESS_TOKEN = "4D457BF80BEF41298AE5A8AEC0A130BD";
	public static final String REDIRECT_URL = "http://hz.zhigongjiaoyu.com/";
	
	public static final String APP_ID = "zhigongjiaoyu";
	public static final String APP_PASSWORD = "pASS12#";
	public static final String APP_ID2 = "2F4766CE823D46528C8DF40B3F09266D";
	public static final String APP_PASSWORD2 = "aSder%82";
	
	private static final long serialVersionUID = 9186253949644956731L;
	private String appId;//应用标识
	private String appSecretKey;//应用秘钥（密文）
	private String currentUserId;//当前用户标识
	private String accessToken;//访问令牌
	private String userType;//用户类型(endUser,adminUser,anonymous，sysUser)
	/**
	 * @return the 应用标识
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * @param appId the 应用标识 to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @return the 应用秘钥（密文）
	 */
	public String getAppSecretKey() {
		return appSecretKey;
	}
	/**
	 * @param appSecretKey the 应用秘钥（密文） to set
	 */
	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}
	/**
	 * @return the 当前用户标识
	 */
	public String getCurrentUserId() {
		return currentUserId;
	}
	/**
	 * @param currentUserId the 当前用户标识 to set
	 */
	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}
	/**
	 * @return the 访问令牌
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * @param accessToken the 访问令牌 to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * @return the 用户类型(endUser,adminUser,anonymous，sysUser)
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the 用户类型(endUser,adminUser,anonymous，sysUser) to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
