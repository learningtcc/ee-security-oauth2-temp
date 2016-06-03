package com.eenet.authen.daoimpl;

import com.eenet.authen.dao.AdminUserInfoDAOService;
import com.eenet.base.dao.BaseDAOImpl;
import com.eenet.common.exception.DBOPException;
import com.eenet.user.AdminUserInfo;

/**
 * 服务人员信息数据库查询逻辑
 * @author Orion
 *
 */
public class AdminUserInfoDAOImpl extends BaseDAOImpl implements AdminUserInfoDAOService {

	@Override
	public boolean existMobileEmailId(AdminUserInfo m) throws DBOPException {
		return false;
	}

}
