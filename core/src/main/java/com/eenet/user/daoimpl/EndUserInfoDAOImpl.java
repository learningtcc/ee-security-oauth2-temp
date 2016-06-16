package com.eenet.user.daoimpl;

import com.eenet.user.dao.EndUserInfoDAOService;
import com.eenet.base.dao.BaseDAOImpl;
import com.eenet.common.exception.DBOPException;
import com.eenet.user.EndUserInfo;

public class EndUserInfoDAOImpl extends BaseDAOImpl implements EndUserInfoDAOService {

	@Override
	public boolean existMobileEmailId(EndUserInfo m) throws DBOPException {
		try {
			Integer totalRecord = super.getBatisSession().selectOne("com.eenet.user.EndUserInfo.existMobileEmailId",m);
			return totalRecord.intValue()!=0;
		} catch (Exception e) {
			throw new DBOPException(e);
		} finally {
			super.getBatisSession().clearCache();
		}
	}

}
