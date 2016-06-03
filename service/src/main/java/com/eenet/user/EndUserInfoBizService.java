package com.eenet.user;

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
