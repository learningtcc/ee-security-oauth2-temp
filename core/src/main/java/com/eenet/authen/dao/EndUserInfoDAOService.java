package com.eenet.authen.dao;

import com.eenet.base.dao.BaseDAOService;
import com.eenet.common.exception.DBOPException;
import com.eenet.user.EndUserInfo;

/**
 * 最终用户信息数据库查询服务
 * @author Orion
 *
 */
public interface EndUserInfoDAOService extends BaseDAOService {
	
	/**
	 * 判断手机、邮箱和身份证号是否已存在
	 * @param m 最终用户信息对象
	 * @return
	 * @throws DBOPException
	 */
	public boolean existMobileEmailId(EndUserInfo m) throws DBOPException;
}
