package com.eenet.test;

import java.util.Random;

import org.junit.Test;

import com.eenet.authen.AccessToken;
import com.eenet.authen.AdminUserCredential;
import com.eenet.authen.AdminUserCredentialBizService;
import com.eenet.authen.AdminUserLoginAccount;
import com.eenet.authen.AdminUserLoginAccountBizService;
import com.eenet.authen.AdminUserSignOnBizService;
import com.eenet.authen.BusinessApp;
import com.eenet.authen.BusinessAppBizService;
import com.eenet.authen.BusinessAppType;
import com.eenet.authen.LoginAccountType;
import com.eenet.authen.SignOnGrant;
import com.eenet.base.SimpleResponse;
import com.eenet.test.env.SpringEnvironment;
import com.eenet.user.AdminUserInfo;
import com.eenet.user.AdminUserInfoBizService;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.RSAEncrypt;
import com.eenet.util.cryptography.RSAUtil;

public class AdminUserSignOnTester extends SpringEnvironment {
	private BusinessAppBizService appService = (BusinessAppBizService)super.getContext().getBean("BusinessAppBizImpl");
	private AdminUserInfoBizService userService = (AdminUserInfoBizService)super.getContext().getBean("AdminUserInfoBizImpl");
	private AdminUserLoginAccountBizService accountService = (AdminUserLoginAccountBizService)super.getContext().getBean("AdminUserLoginAccountBizImpl");
	private AdminUserCredentialBizService credentialService = (AdminUserCredentialBizService)super.getContext().getBean("AdminUserCredentialBizImpl");
	private AdminUserSignOnBizService signService = (AdminUserSignOnBizService)super.getContext().getBean("AdminUserSignOnBizImpl");
	private RSAEncrypt encrypt = (RSAEncrypt)super.getContext().getBean("TransferRSAEncrypt");
	
	private BusinessApp app;
	private AdminUserInfo user;
	private AdminUserLoginAccount account;
	private AdminUserCredential credential;
	private String userPassword = "myPassword";
	private String appPassword = "999Aa$";
	
	@Test
	public void normalFlow() throws Exception{
		System.out.println("==========================="+this.getClass().getName()+".normalFlow()===========================");
		try {
			if (!this.preNormalFlow())
				return;
			
			System.out.println("appid : "+app.getAppId());
			System.out.println("RedirectURIPrefix : "+app.getRedirectURIPrefix());
			SignOnGrant getSignOnGrant = 
					signService.getSignOnGrant(app.getAppId(), app.getRedirectURIPrefix(), account.getLoginAccount(), RSAUtil.encryptWithTimeMillis(encrypt, userPassword));
			if (!getSignOnGrant.isSuccessful()){
				System.out.println("getSignOnGrant : \n"+getSignOnGrant.getStrMessage());
				return;
			}
			System.out.println("getSignOnGrant: "+getSignOnGrant.getGrantCode());
			
			AccessToken getAccessToken = 
					signService.getAccessToken(app.getAppId(), RSAUtil.encryptWithTimeMillis(encrypt, appPassword), getSignOnGrant.getGrantCode());
			if (!getAccessToken.isSuccessful()){
				System.out.println("getAccessToken : \n"+getAccessToken.getStrMessage());
				return;
			}
			System.out.println("getAccessToken AccessToken: "+getAccessToken.getAccessToken());
			System.out.println("getAccessToken RefreshToken: "+getAccessToken.getRefreshToken());
			System.out.println("getAccessToken UserInfo.Name: "+getAccessToken.getUserInfo().getName());
			
			AccessToken refreshAccessToken = 
					signService.refreshAccessToken(app.getAppId(), RSAUtil.encryptWithTimeMillis(encrypt, appPassword), getAccessToken.getRefreshToken(), getAccessToken.getUserInfo().getAtid());
			if (!refreshAccessToken.isSuccessful()){
				System.out.println("refreshAccessToken : \n"+refreshAccessToken.getStrMessage());
				return;
			}
			System.out.println("refreshAccessToken AccessToken: "+refreshAccessToken.getAccessToken());
			System.out.println("refreshAccessToken RefreshToken: "+refreshAccessToken.getRefreshToken());
			
		} finally {
			this.afterNormalFlow();
		}
	}
	
	public boolean preNormalFlow() throws Exception{
		/* 注册业务系统 */
		BusinessApp app = new BusinessApp();
		app.setAppName("临时系统"+EEBeanUtils.getUUID());
		app.setAppType(BusinessAppType.WEBAPP);
		app.setRedirectURIPrefix("http://www.eenet.com");
		app.setSecretKey(this.appPassword);
		app = appService.registeApp(app);
		if (!app.isSuccessful()) {
			System.out.println(app.getStrMessage());
			return false;
		}
		System.out.println("appid: "+app.getAppId());
		this.app = app;
		
		/* 注册服务人员个人信息 */
		AdminUserInfo user = new AdminUserInfo();
		user.setName("Orion");
		user = userService.save(user);
		if (!user.isSuccessful()) {
			System.out.println(user.getStrMessage());
			return false;
		}
		System.out.println("user atid: "+user.getAtid());
		this.user = user;
		
		/* 注册服务人员登录账号 */
		AdminUserLoginAccount account = new AdminUserLoginAccount();
		account.setUserInfo(user);
		account.setAccountType(LoginAccountType.MOBILE);
		account.setLoginAccount(String.valueOf(13900000000l + (new Random().nextInt(100))));
		account = accountService.registeAdminUserLoginAccount(account);
		if (!account.isSuccessful()) {
			System.out.println(account.getStrMessage());
			return false;
		}
		System.out.println("account atid: "+account.getAtid());
		this.account = account;
		
		/* 初始化服务人员登录密码 */
		AdminUserCredential credential = new AdminUserCredential();
		credential.setAdminUser(user);
		credential.setPassword(RSAUtil.encryptWithTimeMillis(encrypt, this.userPassword));
		SimpleResponse initResult = credentialService.initAdminUserLoginPassword(credential);
		if (!initResult.isSuccessful()) {
			System.out.println(initResult.getStrMessage());
			return false;
		}
		this.credential = credential;
		
		return true;
	}
	
	public void afterNormalFlow() {
		/* 注销服务人员个人信息 */
		if (user!=null)
			userService.delete(user.getAtid());
		/* 注销服务人员登录账号 */
		if (account!=null)
			accountService.removeAdminUserLoginAccount(account.getLoginAccount());
		/* 注销业务系统 */
		if (app!=null)
			appService.removeApp(app.getAppId());
	}
}
