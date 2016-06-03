package com.eenet.authen;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 登录令牌交互入口
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eenet.authen.SignOnGrant;
import com.eenet.user.AdminUserInfo;
import com.eenet.user.EndUserInfo;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.EncryptException;
import com.eenet.util.cryptography.RSADecrypt;
import com.eenet.util.cryptography.RSAUtil;

@Controller
public class SignOnController {
	@Autowired
	private RSADecrypt transferRSADecrypt;
	
	@RequestMapping(value = "/getEndUserSignOnGrant", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getEndUserSignOnGrant(String appId, String redirectURI, String loginAccount,String password) {
		SignOnGrant grant = new SignOnGrant();
		grant.setSuccessful(true);
		
		if (!APIRequestIdentity.REDIRECT_URL.equals(redirectURI) || !APIRequestIdentity.APP_ID.equals(appId)) {
			grant.setSuccessful(false);
			grant.addMessage("该应用不可接入");
		} else if (EEBeanUtils.isNULL(loginAccount) || EEBeanUtils.isNULL(password)) {
			grant.setSuccessful(false);
			grant.addMessage("用户名、密码不可为空");
		}
		
		if (!grant.isSuccessful())
			return EEBeanUtils.object2Json(grant);
		
		grant.setGrantCode(APIRequestIdentity.GRANT_CODE);
		return EEBeanUtils.object2Json(grant);
	}
	
	@RequestMapping(value = "/getAdminSignOnGrant", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getAdminSignOnGrant(String appId, String redirectURI, String loginAccount,String password) {
		return this.getEndUserSignOnGrant(appId, redirectURI, loginAccount, password);
	}
	
	@RequestMapping(value = "/getEndUserAccessToken", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getEndUserAccessToken(String appId, String appSecretKey, String grantCode) {
		AccessToken accessToken = new AccessToken();
		accessToken.setSuccessful(true);
		
		if (EEBeanUtils.isNULL(appId) || EEBeanUtils.isNULL(appSecretKey)) {
			accessToken.setSuccessful(false);
			accessToken.addMessage("应用标识和秘钥不可为空");
		} else if (!APIRequestIdentity.GRANT_CODE.equals(grantCode)) {
			accessToken.setSuccessful(false);
			accessToken.addMessage("无效的授权码");
		}
		
		if (!APIRequestIdentity.APP_PASSWORD.equals(SignOnController.getSecretKey(transferRSADecrypt, appSecretKey))) {
			accessToken.setSuccessful(false);
			accessToken.addMessage("无效的应用秘钥");
		}
		
		accessToken.setAccessToken(APIRequestIdentity.ACCESS_TOKEN);
		accessToken.setRefreshToken(APIRequestIdentity.FRESS_TOKEN);
		/* 写入个人信息 */
		EndUserInfo user = new EndUserInfo();
		user.setName("name");user.setCity("city");
		accessToken.setUserInfo(user);
		
		return EEBeanUtils.object2Json(accessToken);
	}
	
	@RequestMapping(value = "/getAdminAccessToken", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getAdminAccessToken(String appId, String appSecretKey, String grantCode) {
		AccessToken accessToken = new AccessToken();
		accessToken.setSuccessful(true);
		
		if (EEBeanUtils.isNULL(appId) || EEBeanUtils.isNULL(appSecretKey)) {
			accessToken.setSuccessful(false);
			accessToken.addMessage("应用标识和秘钥不可为空");
		} else if (!APIRequestIdentity.GRANT_CODE.equals(grantCode)) {
			accessToken.setSuccessful(false);
			accessToken.addMessage("无效的授权码");
		}
		
		if (!APIRequestIdentity.APP_PASSWORD.equals(SignOnController.getSecretKey(transferRSADecrypt, appSecretKey))) {
			accessToken.setSuccessful(false);
			accessToken.addMessage("无效的应用秘钥");
		}
		
		accessToken.setAccessToken(APIRequestIdentity.ACCESS_TOKEN);
		accessToken.setRefreshToken(APIRequestIdentity.FRESS_TOKEN);
		/* 写入个人信息 */
		AdminUserInfo user = new AdminUserInfo();
		user.setMobile(13322277785l);user.setAddress("address,address,address");
		accessToken.setUserInfo(user);
		
		return EEBeanUtils.object2Json(accessToken);
	}
	
	@RequestMapping(value = "/getKeySuffix")
	@ResponseBody
	public String getKeySuffix() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	public static String getSecretKey(RSADecrypt decrypt, String chiperText) {
		String result = null;
		try {
			String plainText = RSAUtil.decrypt(decrypt, chiperText);
			result = plainText.substring(0, plainText.lastIndexOf("##"));
		} catch (EncryptException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}