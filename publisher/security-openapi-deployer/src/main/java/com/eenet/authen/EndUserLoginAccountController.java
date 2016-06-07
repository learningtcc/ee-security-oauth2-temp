package com.eenet.authen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eenet.user.EndUserInfo;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.RSADecrypt;

@Controller
public class EndUserLoginAccountController {
//	@Autowired
	private RSADecrypt transferRSADecrypt;
//	@Autowired
	private EndUserLoginAccountBizService EndUserLoginAccountBizService;
//	@Autowired
	private EndUserCredentialBizService EndUserCredentialBizService;
	
	@RequestMapping(value = "/registeEndUserLoginAccount", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String registeEndUserLoginAccount(APIRequestIdentity identity, EndUserLoginAccount loginAccount) {
		return null;
	}
	
	@RequestMapping(value = "/retrieveEndUserInfo", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String retrieveEndUserInfo(APIRequestIdentity identity, String loginAccount) {
		EndUserInfo user = new EndUserInfo();
		
		return EEBeanUtils.object2Json(user);
	}
	
	@RequestMapping(value = "/initEndUserLoginPassword", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String initEndUserLoginPassword(APIRequestIdentity identity, EndUserCredential curCredential) {
		return null;
	}
	
	@RequestMapping(value = "/changeEndUserLoginPassword", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String changeEndUserLoginPassword(APIRequestIdentity identity, EndUserCredential curCredential, String newSecretKey) {
		return null;
	}
}
