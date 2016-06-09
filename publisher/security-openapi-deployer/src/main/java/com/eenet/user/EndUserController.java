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
		//参数完整性检测
		if (EEBeanUtils.isNULL(identity.getAppId()) || EEBeanUtils.isNULL(identity.getAppSecretKey()) || EEBeanUtils.isNULL(getEndUserId)) {
			response.setSuccessful(false);
			response.addMessage("未提供应用标识、应用秘钥或要取得用户的ID");
			return EEBeanUtils.object2Json(response);
		}
		System.out.println("密文： "+identity.getAppSecretKey());
		System.out.println("明文： "+SignOnController.getSecretKey(transferRSADecrypt, identity.getAppSecretKey()));
		//app接入身份检测
		if (APIRequestIdentity.APP_ID.equals(identity.getAppId())) {
			if (!APIRequestIdentity.APP_PASSWORD.equals(SignOnController.getSecretKey(transferRSADecrypt, identity.getAppSecretKey()))) {
				response.setSuccessful(false);
				response.addMessage("应用标识或应用秘钥有误");
				return EEBeanUtils.object2Json(response);
			}
		} else if (APIRequestIdentity.APP_ID2.equals(identity.getAppId())) {
			if (!APIRequestIdentity.APP_PASSWORD2.equals(SignOnController.getSecretKey(transferRSADecrypt, identity.getAppSecretKey()))) {
				response.setSuccessful(false);
				response.addMessage("应用标识或应用秘钥有误");
				return EEBeanUtils.object2Json(response);
			}
		} else {
			response.setSuccessful(false);
			response.addMessage("应用标识或应用秘钥有误");
			return EEBeanUtils.object2Json(response);
		}
		
		//用户身份检查
		if (EEBeanUtils.isNULL(identity.getCurrentUserId()) && !"sysUser".equals(identity.getUserType())) {
			response.setSuccessful(false);
			response.addMessage("在当前用户未知时，只有系统用户才能读取用户资料");
			return EEBeanUtils.object2Json(response);
		} else if (!EEBeanUtils.isNULL(identity.getCurrentUserId()) && !APIRequestIdentity.ACCESS_TOKEN.equals(identity.getAccessToken())) {
			response.setSuccessful(false);
			response.addMessage("访问令牌有误");
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
		if (!APIRequestIdentity.APP_ID.equals(identity.getAppId())) {
			response.setSuccessful(false);
			response.addMessage("非法应用标识");
		} else if (!"anonymous".equals(identity.getUserType())) {
			if (!APIRequestIdentity.ACCESS_TOKEN.equals(identity.getAccessToken()) || EEBeanUtils.isNULL(identity.getCurrentUserId()) ) {
				response.setSuccessful(false);
				response.addMessage("当前用户不可识别或令牌有误");
			}
		} else if (!APIRequestIdentity.APP_PASSWORD.equals(SignOnController.getSecretKey(transferRSADecrypt, identity.getAppSecretKey()))) {
			response.setSuccessful(false);
			response.addMessage("应用秘钥有误");
		}
		
		if (!response.isSuccessful()){
			return EEBeanUtils.object2Json(response);
		}
		
		EndUserInfo result = this.EndUserInfoBizService.save(endUser);
		return EEBeanUtils.object2Json(result);
	}
}