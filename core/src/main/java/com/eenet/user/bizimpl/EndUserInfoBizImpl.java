package com.eenet.user.bizimpl;

import com.eenet.user.dao.EndUserInfoDAOService;
import com.eenet.base.BooleanResponse;
import com.eenet.base.SimpleResultSet;
import com.eenet.base.biz.SimpleBizImpl;
import com.eenet.base.query.QueryCondition;
import com.eenet.common.exception.DBOPException;
import com.eenet.user.EndUserInfo;
import com.eenet.user.EndUserInfoBizService;
import com.eenet.util.EEBeanUtils;

public class EndUserInfoBizImpl extends SimpleBizImpl implements EndUserInfoBizService {
	private EndUserInfoDAOService endUserDAOService;
	
	@Override
	public BooleanResponse existMobileEmailId(String mobile, String email, String idCard) {
		BooleanResponse result = new BooleanResponse();
		result.setSuccessful(false);
		if (EEBeanUtils.isNULL(mobile) && EEBeanUtils.isNULL(email) && EEBeanUtils.isNULL(idCard)) {
			result.addMessage("至少指定手机、邮箱或身份证的其中一项");
			return result;
		}
		
		try {
			EndUserInfo queryCondition = new EndUserInfo();
			queryCondition.setMobile(Long.parseLong(mobile));
			queryCondition.setEmail(email);
			queryCondition.setIdCard(idCard);
			boolean canCreate = getEndUserDAOService().existMobileEmailId(queryCondition);
			result.setSuccessful(true);
			result.setResult(canCreate);
			return result;
		} catch (NumberFormatException e) {
			result.addMessage("手机格式错误");
			return result;
		} catch (DBOPException e) {
			result.addMessage(e.toString());
			return result;
		}
	}
	
	@Override
	public EndUserInfo save(EndUserInfo m) {
		if (m == null) {
			m = new EndUserInfo();
			m.setSuccessful(false);
			m.addMessage("无法处理空对象");
			return m;
		}
		try {
			// atid为空（即新增用户），同时手机、邮箱、身份证至少一个不为空，判断是否存在重复的手机、邮箱、身份证
			if (EEBeanUtils.isNULL(m.getAtid()) && (m.getMobile() != null || !EEBeanUtils.isNULL(m.getEmail())
					|| !EEBeanUtils.isNULL(m.getIdCard()))) {
				if (this.getEndUserDAOService().existMobileEmailId(m)) {
					m.setSuccessful(false);
					m.addMessage("存在重复的手机、邮箱或身份证");
					return m;
				}
			}
		} catch (DBOPException e) {
			m.setSuccessful(false);
			m.addMessage("系统错误：无法判断是否存在重复的手机、邮箱或身份证");
			m.addMessage(e.getMessage());
			return m;
		}
		return super.save(m);
	}

	@Override
	public EndUserInfo get(String pk) {
		// TODO Auto-generated method stub
		return super.get(pk);
	}

	@Override
	public SimpleResultSet<EndUserInfo> query(QueryCondition condition) {
		// TODO Auto-generated method stub
		return super.query(condition);
	}

	@Override
	public Class<?> getPojoCLS() {
		// TODO Auto-generated method stub
		return EndUserInfo.class;
	}

	/**
	 * @return the endUserDAOService
	 */
	public EndUserInfoDAOService getEndUserDAOService() {
		return endUserDAOService;
	}

	/**
	 * @param endUserDAOService the endUserDAOService to set
	 */
	public void setEndUserDAOService(EndUserInfoDAOService endUserDAOService) {
		this.endUserDAOService = endUserDAOService;
	}
	
}
