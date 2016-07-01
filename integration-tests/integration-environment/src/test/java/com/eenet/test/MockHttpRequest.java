package com.eenet.test;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONObject;

public class MockHttpRequest {
	public static String baseURL = "http://172.16.165.223:8080/security-api";
	
	public static void main(String[] args) throws Exception {
		MockHttpRequest.adminLoginAndGetEndUserInfo();
//		MockHttpRequest.endUserLoginWithMD5Password();
	}
	
	/**
	 * 对只设置了私有账号并且是MD5加密的密码进行认证
	 * @throws Exception
	 * 2016年6月16日
	 * @author Orion
	 */
	public static void endUserLoginWithMD5Password() throws Exception{
		/* 公共参数 */
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		PostMethod method;
		String returnMessage;
		JSONObject jsonObject;
		String appId = "432B31FB2F7C4BB19ED06374FB0C1850";
		String appSecretKey = "pASS12#";
		String appDomain = "http://www.zhigongjiaoyu.com";
		String loginAccount = "gjm2015";
		String password = "gjm2015Password";
		
		/* 定义调用地址和调用参数 */
		String getEndUserSignOnGrantURL = baseURL+"/getEndUserSignOnGrant";
		String getEndUserAccessTokenURL = baseURL+"/getEndUserAccessToken";
		
		/* 获得登录授权码 */
		method = new PostMethod(getEndUserSignOnGrantURL);
		method.addParameter("appId", appId);
		method.addParameter("redirectURI", appDomain);
		method.addParameter("loginAccount", loginAccount);
		method.addParameter("password", encrypt(password+"##"+System.currentTimeMillis()));
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
		jsonObject = new JSONObject(returnMessage);
		String grantCode = jsonObject.get("grantCode").toString();
		System.out.println("grantCode : " + grantCode);
		
		/* 获得访问令牌 */
		method = new PostMethod(getEndUserAccessTokenURL);
		method.addParameter("appId", appId);
		method.addParameter("grantCode", grantCode);
		method.addParameter("appSecretKey", encrypt(appSecretKey+"##"+System.currentTimeMillis()));
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
//		jsonObject = new JSONObject(returnMessage);
//		String accessToken = jsonObject.get("accessToken").toString();
//		String refreshToken = jsonObject.get("refreshToken").toString();
//		System.out.println("accessToken : " + accessToken + ", refreshToken : " + refreshToken);
	}
	
	public static void adminLoginAndGetEndUserInfo() throws Exception{
		/* 公共参数 */
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		PostMethod method;
		String returnMessage;
		JSONObject jsonObject;
		String appId = "432B31FB2F7C4BB19ED06374FB0C1850";
		String appSecretKey = "pASS12#";
		String adminId = "38424AE288CF4C1CB22BF324C17AAB74";
		String loginAccount = "md5Account";
		String adminPassword = "md5Password";
		String getEndUserId = "EE31F64139804467B42866053F1FA6C4";
		String appDomain = "http://www.zhigongjiaoyu.com";
		
		/* 定义调用地址和调用参数 */
		String getAdminSignOnGrantURL = baseURL+"/getAdminSignOnGrant";
		String getAdminAccessTokenURL = baseURL+"/getAdminAccessToken";
		String getEndUserURL = baseURL+"/getEndUser";
		String refreshAdminAccessTokenURL = baseURL+"/refreshAdminAccessToken";
		
		/* 获得登录授权码 */
		method = new PostMethod(getAdminSignOnGrantURL);
		method.addParameter("appId", appId);
		method.addParameter("redirectURI", appDomain);
		method.addParameter("loginAccount", loginAccount);
		method.addParameter("password", encrypt(adminPassword+"##"+System.currentTimeMillis()));
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
		jsonObject = new JSONObject(returnMessage);
		String grantCode = jsonObject.get("grantCode").toString();
		System.out.println("grantCode : " + grantCode);
		
		/* 获得访问令牌 */
		method = new PostMethod(getAdminAccessTokenURL);
		method.addParameter("appId", appId);
		method.addParameter("grantCode", grantCode);
		method.addParameter("appSecretKey", encrypt(appSecretKey+"##"+System.currentTimeMillis()));
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
		jsonObject = new JSONObject(returnMessage);
		String accessToken = jsonObject.get("accessToken").toString();
		String refreshToken = jsonObject.get("refreshToken").toString();
		System.out.println("accessToken : " + accessToken + ", refreshToken : " + refreshToken);
		
		/* 使用访问令牌查询数据 */
		method = new PostMethod(getEndUserURL);
		method.addParameter("getEndUserId", getEndUserId);
		method.addParameter("userType", "adminUser");
		method.addParameter("userId", adminId);
		method.addParameter("userAccessToken", accessToken);
		method.addParameter("appId", appId);
		method.addParameter("appSecretKey", encrypt(appSecretKey+"##"+System.currentTimeMillis()) );
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
		
		/* 使用刷新令牌获得新的访问令牌 */
		method = new PostMethod(refreshAdminAccessTokenURL);
		method.addParameter("appId", appId);
		method.addParameter("appSecretKey", encrypt(appSecretKey+"##"+System.currentTimeMillis()) );
		method.addParameter("refreshToken", refreshToken);
		method.addParameter("adminUserId", adminId);
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
		jsonObject = new JSONObject(returnMessage);
		String newAccessToken = jsonObject.get("accessToken").toString();
		String newRefreshToken = jsonObject.get("refreshToken").toString();
		System.out.println("newAccessToken : " + newAccessToken + ", newRefreshToken : " + newRefreshToken);
		
		/* 使用新的访问令牌查询数据 */
		method = new PostMethod(getEndUserURL);
		method.addParameter("getEndUserId", getEndUserId);
		method.addParameter("userType", "adminUser");
		method.addParameter("userId", adminId);
		method.addParameter("userAccessToken", newAccessToken);
		method.addParameter("appId", appId);
		method.addParameter("appSecretKey", encrypt(appSecretKey+"##"+System.currentTimeMillis()));
		client.executeMethod(method);
		
		returnMessage = EncodingUtil.getString(method.getResponseBody(), "UTF-8");
		System.out.println("returnMessage : " + returnMessage);
	}
	
	
	public static String encrypt(String plaintext) throws Exception {
		String sslPublicKey = 
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3ofG3TuzCBaolNYFuTVkOv8yN" + "\r" +
				"B+u3KvSwqqMYsqAKK/q518kyVnl5Mq2h4kqE6YKaV1hJgsd0n4McjCg06xXQP1nh" + "\r" +
				"w3kjX/cL0W6jKTTERDnNDK6ifIdczsFOsaFMSxuA9T3Laji3WmTz4sDpkBN7Ymql" + "\r" +
				"yzqa7HG12GH4zODWtwIDAQAB" + "\r";
		
		byte[] buffer= Base64.decodeBase64(sslPublicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
		RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		
		Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());//RSA是加密方法，ECB是加密模式，PKCS1Padding是填充方式
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] output= cipher.doFinal(plaintext.getBytes("UTF-8"));
		
		return Base64.encodeBase64String(output);
	}
}
