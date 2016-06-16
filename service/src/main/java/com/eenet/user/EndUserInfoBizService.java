package com.eenet.user;

import com.eenet.base.BooleanResponse;
import com.eenet.base.SimpleResponse;
import com.eenet.base.SimpleResultSet;
import com.eenet.base.query.QueryCondition;

/**
 * 最终用户管理服务
 * @author Orion
 *
 */
public interface EndUserInfoBizService {
	
	/**
	 * 新增最终用户前检查，检查是否存在相同的手机、电子邮箱或身份证号
	 * @param mobile 手机
	 * @param email 邮箱
	 * @param idCard 身份证
	 * @return
	 * 2016年6月16日
	 * @author Orion
	 */
	public BooleanResponse existMobileEmailId(String mobile, String email, String idCard);
	/**
	 * 保存用户信息
	 * @param m
	 * @return
	 */
	public EndUserInfo save(EndUserInfo m);
	
	/**
	 * 获得用户信息
	 * @param pk
	 * @return
	 */
	public EndUserInfo get(String pk);
	
	/**
	 * 删除用户信息
	 * @param pk
	 * @return
	 */
	public SimpleResponse delete(String... pk);
	
	/**
	 * 查询用户信息
	 * @param condition
	 * @return
	 */
	
	public SimpleResultSet<EndUserInfo> query (QueryCondition condition);
}
