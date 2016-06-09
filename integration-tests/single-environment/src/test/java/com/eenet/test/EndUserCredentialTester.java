package com.eenet.test;

import org.junit.Test;

import com.eenet.authen.EndUserCredential;
import com.eenet.authen.EndUserCredentialBizService;
import com.eenet.base.SimpleResponse;
import com.eenet.base.StringResponse;
import com.eenet.test.env.SpringEnvironment;
import com.eenet.user.EndUserInfo;
import com.eenet.user.EndUserInfoBizService;
import com.eenet.util.cryptography.RSADecrypt;
import com.eenet.util.cryptography.RSAEncrypt;
import com.eenet.util.cryptography.RSAUtil;

public class EndUserCredentialTester extends SpringEnvironment {
	@Test
	public void crud() throws Exception{
		System.out.println("==========================="+this.getClass().getName()+".crud()===========================");
		EndUserCredentialBizService credentialService = (EndUserCredentialBizService)super.getContext().getBean("EndUserCredentialBizImpl");
		EndUserInfoBizService userService = (EndUserInfoBizService)super.getContext().getBean("EndUserInfoBizImpl");
		RSAEncrypt encrypt = (RSAEncrypt)super.getContext().getBean("TransferRSAEncrypt");
		RSADecrypt StorageRSADecrypt = (RSADecrypt)super.getContext().getBean("StorageRSADecrypt");
		
		EndUserInfo user = new EndUserInfo();
		user.setName("Orion");
		user = userService.save(user);
		
		EndUserCredential credential = new EndUserCredential();
		credential.setEndUser(user);
		credential.setPassword(RSAUtil.encryptWithTimeMillis(encrypt, "myPassword"));
		SimpleResponse initResult = credentialService.initEndUserLoginPassword(credential);
		if (!initResult.isSuccessful()) {
			System.out.println(initResult.getStrMessage());
			return;
		}
		
		EndUserCredential curCredential = new EndUserCredential();
		curCredential.setEndUser(user);
		curCredential.setPassword(RSAUtil.encryptWithTimeMillis(encrypt, "myPassword"));
		String newSecretKey = RSAUtil.encrypt(encrypt, "myPassword2");
		SimpleResponse changeResult = credentialService.changeEndUserLoginPassword(curCredential, newSecretKey);
		if (!changeResult.isSuccessful()) {
			System.out.println(changeResult.getStrMessage());
			return;
		}
		
		StringResponse retrievePassword = credentialService.retrieveEndUserSecretKey(user.getAtid(), StorageRSADecrypt);
		if (!retrievePassword.isSuccessful()) {
			System.out.println(retrievePassword.getStrMessage());
			return;
		}
		System.out.println("解密结果："+retrievePassword.getResult());
		
		userService.delete(user.getAtid());
	}
}
