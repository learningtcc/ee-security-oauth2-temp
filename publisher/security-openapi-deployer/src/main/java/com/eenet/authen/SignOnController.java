package com.eenet.authen;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 登录令牌交互入口
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eenet.authen.SignOnGrant;
import com.eenet.util.EEBeanUtils;

@Controller
public class SignOnController {
	@Autowired
	private AdminUserSignOnBizService adminUserSignOnBizService;
	@Autowired
	private EndUserSignOnBizService endUserSignOnBizService;
	
	@RequestMapping(value = "/getEndUserSignOnGrant", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getEndUserSignOnGrant(String appId, String redirectURI, String loginAccount,String password) {
		SignOnGrant getSignOnGrant = 
				endUserSignOnBizService.getSignOnGrant(appId, redirectURI, loginAccount, password);
		return EEBeanUtils.object2Json(getSignOnGrant);
	}
	
	@RequestMapping(value = "/getAdminSignOnGrant", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getAdminSignOnGrant(String appId, String redirectURI, String loginAccount,String password) {
		SignOnGrant getSignOnGrant = 
				adminUserSignOnBizService.getSignOnGrant(appId, redirectURI, loginAccount, password);
		return EEBeanUtils.object2Json(getSignOnGrant);
	}
	
	@RequestMapping(value = "/getEndUserAccessToken", produces = {"application/json;charset=UTF-8"})//, method = RequestMethod.POST
	@ResponseBody
	public String getEndUserAccessToken(String appId, String appSecretKey, String grantCode) {
		AccessToken accessToken = endUserSignOnBizService.getAccessToken(appId, appSecretKey, grantCode);
		return EEBeanUtils.object2Json(accessToken);
	}
	
	@RequestMapping(value = "/getAdminAccessToken", produces = {"application/json;charset=UTF-8"})//, method = RequestMethod.POST
	@ResponseBody
	public String getAdminAccessToken(String appId, String appSecretKey, String grantCode) {
		AccessToken accessToken = adminUserSignOnBizService.getAccessToken(appId, appSecretKey, grantCode);
		return EEBeanUtils.object2Json(accessToken);
	}
	
	@RequestMapping(value = "/refreshEndUserAccessToken", produces = {"application/json;charset=UTF-8"})//, method = RequestMethod.POST
	@ResponseBody
	public String refreshEndUserAccessToken(String appId, String appSecretKey, String refreshToken, String endUserId) {
		AccessToken accessToken = endUserSignOnBizService.refreshAccessToken(appId, appSecretKey, refreshToken, endUserId);
		return EEBeanUtils.object2Json(accessToken);
	}
	
	@RequestMapping(value = "/refreshAdminAccessToken", produces = {"application/json;charset=UTF-8"})//, method = RequestMethod.POST
	@ResponseBody
	public String refreshAdminAccessToken(String appId, String appSecretKey, String refreshToken, String adminUserId) {
		AccessToken accessToken = adminUserSignOnBizService.refreshAccessToken(appId, appSecretKey, refreshToken, adminUserId);
		return EEBeanUtils.object2Json(accessToken);
	}
	
	@RequestMapping(value = "/getKeySuffix")
	@ResponseBody
	public String getKeySuffix() {
		return String.valueOf(System.currentTimeMillis());
	}
}