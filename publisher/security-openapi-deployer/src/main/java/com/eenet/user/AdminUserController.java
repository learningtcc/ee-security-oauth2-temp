package com.eenet.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eenet.authen.APIRequestIdentity;
import com.eenet.authen.SignOnController;
import com.eenet.base.SimpleResponse;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.RSADecrypt;

@Controller
public class AdminUserController {
	@Autowired
	private AdminUserInfoBizService AdminUserInfoBizService;
	@Autowired
	private RSADecrypt transferRSADecrypt;
	
	@RequestMapping(value = "/getAdminUser", produces = {"application/json;charset=UTF-8"}) //, method = RequestMethod.GET
	@ResponseBody
	public String getAdminUser(APIRequestIdentity identity, String getAdminUserId) {
		SimpleResponse response = new SimpleResponse();
		response.setSuccessful(true);
		/*
		 * 临时检查身份
		 */
		if (!APIRequestIdentity.APP_ID.equals(identity.getAppId()) || !APIRequestIdentity.ACCESS_TOKEN.equals(identity.getAccessToken())) {
			response.setSuccessful(false);
			response.addMessage("应用标识或令牌有误");
		} else if (!APIRequestIdentity.APP_PASSWORD.equals(SignOnController.getSecretKey(transferRSADecrypt, identity.getAppSecretKey()))) {
			response.setSuccessful(false);
			response.addMessage("应用秘钥有误");
		} else if (EEBeanUtils.isNULL(getAdminUserId) || EEBeanUtils.isNULL(identity.getCurrentUserId())) {
			response.setSuccessful(false);
			response.addMessage("当前用户或要获取的用户未指定");
		}
		if (!response.isSuccessful()){
			return EEBeanUtils.object2Json(response);
		}
		
		AdminUserInfo admin = this.AdminUserInfoBizService.get(getAdminUserId);
		return EEBeanUtils.object2Json(admin);
	}
	
	@RequestMapping(value = "/saveAdminUser", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String saveAdminUser(APIRequestIdentity identity, AdminUserInfo admin) {
		SimpleResponse response = new SimpleResponse();
		response.setSuccessful(true);
		/*
		 * 临时检查身份
		 */
		if (!APIRequestIdentity.APP_ID.equals(identity.getAppId()) || !APIRequestIdentity.ACCESS_TOKEN.equals(identity.getAccessToken())) {
			response.setSuccessful(false);
			response.addMessage("应用标识或令牌有误");
		} else if (!APIRequestIdentity.APP_PASSWORD.equals(SignOnController.getSecretKey(transferRSADecrypt, identity.getAppSecretKey()))) {
			response.setSuccessful(false);
			response.addMessage("应用秘钥有误");
		} else if (EEBeanUtils.isNULL(identity.getCurrentUserId())) {
			response.setSuccessful(false);
			response.addMessage("当前用户未指定");
		}
		if (!response.isSuccessful()){
			return EEBeanUtils.object2Json(response);
		}
		
		
		AdminUserInfo result = this.AdminUserInfoBizService.save(admin);
		return EEBeanUtils.object2Json(result);
	}
	
	@RequestMapping(value = "/tryIdentity", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String tryIdentity(APIRequestIdentity identity, AdminUserInfo admin) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(identity.getAppId());
		buffer.append(identity.getAppSecretKey());
		buffer.append(identity.getCurrentUserId());
		buffer.append(identity.getUserType());
		buffer.append(admin.getAtid());
		buffer.append(admin.getMobile());
		buffer.append(admin.getName());
		
		
		
		return buffer.toString();
	}
}