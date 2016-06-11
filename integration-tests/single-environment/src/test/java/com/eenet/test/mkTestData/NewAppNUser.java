package com.eenet.test.mkTestData;

import java.util.Random;

import org.junit.Test;

import com.eenet.authen.AdminUserCredential;
import com.eenet.authen.AdminUserCredentialBizService;
import com.eenet.authen.AdminUserLoginAccount;
import com.eenet.authen.AdminUserLoginAccountBizService;
import com.eenet.authen.BusinessApp;
import com.eenet.authen.BusinessAppBizService;
import com.eenet.authen.BusinessAppType;
import com.eenet.authen.EndUserCredential;
import com.eenet.authen.EndUserCredentialBizService;
import com.eenet.authen.EndUserLoginAccount;
import com.eenet.authen.EndUserLoginAccountBizService;
import com.eenet.authen.LoginAccountType;
import com.eenet.base.SimpleResponse;
import com.eenet.test.env.SpringEnvironment;
import com.eenet.user.AdminUserInfo;
import com.eenet.user.AdminUserInfoBizService;
import com.eenet.user.EndUserInfo;
import com.eenet.user.EndUserInfoBizService;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.EncryptException;
import com.eenet.util.cryptography.RSAEncrypt;
import com.eenet.util.cryptography.RSAUtil;

public class NewAppNUser extends SpringEnvironment{
	private AdminUserInfoBizService adminService = (AdminUserInfoBizService)super.getContext().getBean("AdminUserInfoBizImpl");
	private EndUserInfoBizService endUserService = (EndUserInfoBizService)super.getContext().getBean("EndUserInfoBizImpl");
	private BusinessAppBizService appService = (BusinessAppBizService)super.getContext().getBean("BusinessAppBizImpl");
	private AdminUserLoginAccountBizService adminAccountService = (AdminUserLoginAccountBizService)super.getContext().getBean("AdminUserLoginAccountBizImpl");
	private AdminUserCredentialBizService adminCredentialService = (AdminUserCredentialBizService)super.getContext().getBean("AdminUserCredentialBizImpl");
	private EndUserLoginAccountBizService endUserAccountService = (EndUserLoginAccountBizService)super.getContext().getBean("EndUserLoginAccountBizImpl");
	private EndUserCredentialBizService endUserCredentialService = (EndUserCredentialBizService)super.getContext().getBean("EndUserCredentialBizImpl");
	private RSAEncrypt encrypt = (RSAEncrypt)super.getContext().getBean("TransferRSAEncrypt");
	
//	@Test
	public void createAdmin() {
		AdminUserInfo admin = new AdminUserInfo();
		admin.setName("职工教育网管理员（广州）");
		admin.setDataDescription("勿删！！！重要测试数据");
		admin = adminService.save(admin);
		System.out.println(admin.getAtid() + "," + admin.getName());
	}
	
	@Test
	public void createEndUserNAccountNCredential() throws Exception {
		for (int i=0;i<5;i++) {
			EndUserInfo endUser = new EndUserInfo();
			endUser.setName("职工#"+i);
			endUser.setMobile(13900000000l + new Random().nextInt(100));
			endUser.setMobileChecked(true);
			endUser.setDataDescription("勿删！！！重要测试数据");
			endUser = endUserService.save(endUser);
			
			EndUserLoginAccount account = new EndUserLoginAccount();
			account.setUserInfo(endUser);
			account.setAccountType(LoginAccountType.MOBILE);
			account.setLoginAccount(String.valueOf(endUser.getMobile()));
			account.setDataDescription("勿删！！！重要测试数据");
			account = endUserAccountService.registeEndUserLoginAccount(account);
			
			String password = "abc"+new Random().nextInt(100);
			EndUserCredential credential = new EndUserCredential();
			credential.setEndUser(endUser);
			credential.setPassword(RSAUtil.encryptWithTimeMillis(encrypt, password));
			credential.setDataDescription("勿删！！！重要测试数据");
			SimpleResponse initResult = endUserCredentialService.initEndUserLoginPassword(credential);
			
			if (endUser.isSuccessful() && account.isSuccessful() && initResult.isSuccessful()) {
				StringBuffer info = new StringBuffer("ID: " + endUser.getAtid());
				info.append(",姓名：" + endUser.getName());
				info.append(",手机：" + endUser.getMobile());
				info.append(",登录账号："+ account.getLoginAccount());
				info.append(",登录密码："+password);
				System.out.println(info.toString());
			} else
				System.out.println("出错了");
		}
	}
	
//	@Test
	public void createAPP() {
		BusinessApp app = new BusinessApp();
		app.setAppName("职工教育网（广州）");
		app.setAppType(BusinessAppType.WEBAPP);
		app.setRedirectURIPrefix("http://www.zhigongjiaoyu.com");
		app.setSecretKey("pASS12#");
		app = appService.registeApp(app);
		app.setDataDescription("勿删！！！重要测试数据");
		System.out.println("APPID: " + app.getAtid() + ",系统中文名：" + app.getAppName() + ",接入密码:pASS12#,合法地址： "+app.getRedirectURIPrefix());
	}
	
//	@Test
	public void createAdminLoginAccountNCredential() throws Exception {
		String loginAccount = "gz.zhigongjiaoyu";
		String password = "gzzgjyAdmin";
		String adminUserId = "742481BBADDB4CCA8394BFDBE36EAE27";
		AdminUserLoginAccount account = new AdminUserLoginAccount();
		AdminUserInfo admin = new AdminUserInfo();admin.setAtid(adminUserId);
		account.setUserInfo(admin);
		account.setAccountType(LoginAccountType.USERNAME);
		account.setLoginAccount(loginAccount);
		account = adminAccountService.registeAdminUserLoginAccount(account);
		account.setDataDescription("勿删！！！重要测试数据");
		
		AdminUserCredential credential = new AdminUserCredential();
		credential.setAdminUser(admin);
		credential.setPassword(RSAUtil.encryptWithTimeMillis(encrypt, password));
		credential.setDataDescription("勿删！！！重要测试数据");
		SimpleResponse initResult = adminCredentialService.initAdminUserLoginPassword(credential);
		
		if (account.isSuccessful() && initResult.isSuccessful())
			System.out.println("管理员登录账号： "+loginAccount+",管理员登录密码："+password);
		else
			System.out.print("出错了");
	}
}
