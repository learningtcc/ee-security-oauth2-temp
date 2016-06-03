package com.eenet.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eenet.authen.APIRequestIdentity;
import com.eenet.authen.SignOnController;
import com.eenet.base.SimpleResponse;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.RSADecrypt;

@Controller
public class EndUserController {
	@Autowired
	private EndUserInfoBizService EndUserInfoBizService;
	@Autowired
	private RSADecrypt transferRSADecrypt;
	
	@RequestMapping(value = "/getEndUser", produces = {"application/json;charset=UTF-8"})// , method = RequestMethod.GET
	@ResponseBody
	public String getEndUser(APIRequestIdentity identity, String getEndUserId) {
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
		} else if (EEBeanUtils.isNULL(getEndUserId) || EEBeanUtils.isNULL(identity.getCurrentUserId())) {
			response.setSuccessful(false);
			response.addMessage("当前用户或要获取的用户未指定");
		}
		if (!response.isSuccessful()){
			return EEBeanUtils.object2Json(response);
		}
		
		EndUserInfo endUser = this.EndUserInfoBizService.get(getEndUserId);
		return EEBeanUtils.object2Json(endUser);
	}
	
	@RequestMapping(value = "/saveEndUser", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String saveEndUser(APIRequestIdentity identity, EndUserInfo endUser) {
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
		
		EndUserInfo result = this.EndUserInfoBizService.save(endUser);
		return EEBeanUtils.object2Json(result);
	}
}