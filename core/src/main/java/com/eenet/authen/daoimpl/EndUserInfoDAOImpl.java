package com.eenet.authen.daoimpl;

import com.eenet.authen.dao.EndUserInfoDAOService;
import com.eenet.base.dao.BaseDAOImpl;
import com.eenet.common.exception.DBOPException;
import com.eenet.user.EndUserInfo;

public class EndUserInfoDAOImpl extends BaseDAOImpl implements EndUserInfoDAOService {

	@Override
	public boolean existMobileEmailId(EndUserInfo m) throws DBOPException {
		return false;
	}

}
