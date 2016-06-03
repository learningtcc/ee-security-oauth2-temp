package com.eenet.test;

import org.junit.Test;

import com.eenet.test.env.SpringEnvironment;
import com.eenet.user.EndUserInfo;
import com.eenet.user.EndUserInfoBizService;

public class EndUserInfoTester extends SpringEnvironment {
	
	@Test
	public void crud(){
		EndUserInfo endUser = new EndUserInfo();
		EndUserInfoBizService service = (EndUserInfoBizService)super.getContext().getBean("EndUserInfoBizImpl");
		endUser.setName("Orion");
		endUser = service.save(endUser);
		
		if (!endUser.isSuccessful()) {
			System.out.println(endUser.getStrMessage());
			return;
		}
		
		endUser.setMobile(13999999999l);
		endUser = service.save(endUser);
		if (!endUser.isSuccessful()) {
			System.out.println(endUser.getStrMessage());
			return;
		}
		
		EndUserInfo getEndUser = service.get(endUser.getAtid());
		if (!getEndUser.isSuccessful()) {
			System.out.println(getEndUser.getStrMessage());
			return;
		}
		System.out.println("============"+getEndUser.getMobile());
	}
}
