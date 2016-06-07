package com.eenet.authen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eenet.user.AdminUserInfo;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.RSADecrypt;

@Controller
public class AdminUserLoginAccountController {
//	@Autowired
	private RSADecrypt transferRSADecrypt;
//	@Autowired
	private AdminUserLoginAccountBizService AdminUserLoginAccountBizService;
//	@Autowired
	private AdminUserCredentialBizService AdminUserCredentialBizService;
	
	@RequestMapping(value = "/registeAdminUserLoginAccount", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String registeAdminUserLoginAccount(APIRequestIdentity identity, AdminUserLoginAccount loginAccount) {
		return null;
	}
	
	@RequestMapping(value = "/retrieveAdminUserInfo", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String retrieveAdminUserInfo(APIRequestIdentity identity, String loginAccount) {
		AdminUserInfo user = new AdminUserInfo();
		
		return EEBeanUtils.object2Json(user);
	}
	
	@RequestMapping(value = "/initAdminUserLoginPassword", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String initAdminUserLoginPassword(APIRequestIdentity identity, AdminUserCredential curCredential) {
		return null;
	}
	
	@RequestMapping(value = "/changeAdminUserLoginPassword", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String changeAdminUserLoginPassword(APIRequestIdentity identity, AdminUserCredential curCredential, String newSecretKey) {
		return null;
	}
}
