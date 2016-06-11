package com.eenet.authen;

import com.eenet.authen.request.AppAuthenRequest;
import com.eenet.authen.request.UserAccessTokenAuthenRequest;
import com.eenet.authen.response.UserAccessTokenAuthenResponse;
import com.eenet.base.BooleanResponse;

/**
 * 身份认证服务，含：服务消费者、最终用户
 * @author Orion
 *
 */
public interface IdentityAuthenticationBizService {
	
	/**
	 * 业务应用认证
	 * @param request 应用接入秘钥属性，以带时间戳形式加密
	 * @return
	 */
	public BooleanResponse appAuthen(AppAuthenRequest request);
	
	/**
	 * 最终用户认证
	 * 同时认证最终用户身份和业务应用系统身份
	 * @param request 应用接入秘钥属性，以带时间戳形式加密
	 * @return
	 */
	public UserAccessTokenAuthenResponse endUserAuthen(UserAccessTokenAuthenRequest request);
	
	/**
	 * 服务人员认证
	 * 同时认证服务人员身份和业务应用系统身份
	 * @param request 应用接入秘钥属性，以带时间戳形式加密
	 * @return
	 * 2016年6月9日
	 * @author Orion
	 */
	public UserAccessTokenAuthenResponse adminUserAuthen(UserAccessTokenAuthenRequest request);
	/**
	 * 检查服务提供者状态
	 * @return
	 * 2016年3月29日
	 * @author Orion
	 */
	public boolean authenServiceProviderPing();
}
