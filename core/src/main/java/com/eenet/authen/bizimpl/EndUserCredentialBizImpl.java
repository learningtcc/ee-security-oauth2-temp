package com.eenet.authen.bizimpl;

import com.eenet.authen.EndUserCredential;
import com.eenet.authen.EndUserCredentialBizService;
import com.eenet.authen.cacheSyn.SynEndUserCredential2Redis;
import com.eenet.base.SimpleResponse;
import com.eenet.base.SimpleResultSet;
import com.eenet.base.StringResponse;
import com.eenet.base.biz.SimpleBizImpl;
import com.eenet.base.query.ConditionItem;
import com.eenet.base.query.QueryCondition;
import com.eenet.base.query.RangeType;
import com.eenet.common.cache.RedisClient;
import com.eenet.user.EndUserInfo;
import com.eenet.user.EndUserInfoBizService;
import com.eenet.util.EEBeanUtils;
import com.eenet.util.cryptography.EncryptException;
import com.eenet.util.cryptography.RSADecrypt;
import com.eenet.util.cryptography.RSAEncrypt;
import com.eenet.util.cryptography.RSAUtil;
/**
 * 最终用户登录秘钥服务实现逻辑
 * @author Orion
 * 2016年6月9日
 */
public class EndUserCredentialBizImpl extends SimpleBizImpl implements EndUserCredentialBizService {
	private RedisClient RedisClient;//Redis客户端
	private RSAEncrypt StorageRSAEncrypt;//数据存储加密公钥
	private RSADecrypt StorageRSADecrypt;//数据存储解密私钥
	private RSADecrypt TransferRSADecrypt;//数据传输解密私钥
	private EndUserInfoBizService endUserInfoBizService;//最终用户信息服务
	
	@Override
	public SimpleResponse initEndUserLoginPassword(EndUserCredential credential) {
		SimpleResponse result = new SimpleResponse();
		/* 参数检查 */
		if (credential == null) {
			result.setSuccessful(false);
			result.addMessage("要初始化的最终用户登录秘钥未知("+this.getClass().getName()+")");
			return result;
		} else if (EEBeanUtils.isNULL(credential.getPassword()) || credential.getEndUser()==null || EEBeanUtils.isNULL(credential.getEndUser().getAtid())) {
			result.setSuccessful(false);
			result.addMessage("要初始化的最终用户登录秘钥参数不全，END USER标识、登录秘钥均不可为空("+this.getClass().getName()+")");
		}
		if (!result.isSuccessful())
			return result;
		
		/* 判断最终用户是否已设置过密码，有则返回错误信息 */
		EndUserCredential existCredential = this.retrieveEndUserCredentialInfo(credential.getEndUser().getAtid());
		if (existCredential.isSuccessful()) {
			result.setSuccessful(false);
			result.addMessage("该最终用户已经设置过统一登录密码");
			return result;
		}
		
		/* 判断指定的最终用户是否存在 */
		EndUserInfo existEndUser = this.getEndUserInfoBizService().get(credential.getEndUser().getAtid());
		if (!existEndUser.isSuccessful() || EEBeanUtils.isNULL(existEndUser.getAtid())) {
			result.setSuccessful(false);
			result.addMessage("未找到指定要设置登录密码对应的最终用户");
			return result;
		}
		
		/* 秘钥加密 */
		try {
			String passwordPlainText = RSAUtil.decryptWithTimeMillis(getTransferRSADecrypt(), credential.getPassword(), 2);
			String passwordCipherText = RSAUtil.encrypt(getStorageRSAEncrypt(), passwordPlainText);
			if (EEBeanUtils.isNULL(passwordCipherText))
				throw new EncryptException("初始化密码前加密失败（空字符）");
			credential.setPassword(passwordCipherText);
		} catch (EncryptException e) {
			result.setSuccessful(false);
			result.addMessage(e.toString());
			return result;
		}
		
		/* 保存到数据库，再根据保存结果写缓存或返回错误信息 */
		EndUserCredential savedResult = super.save(credential);
		result.setSuccessful(savedResult.isSuccessful());
		if (savedResult.isSuccessful())
			SynEndUserCredential2Redis.syn(getRedisClient(), savedResult);
		else
			result.addMessage(savedResult.getStrMessage());
		
		return result;
	}

	@Override
	public SimpleResponse changeEndUserLoginPassword(EndUserCredential curCredential, String newSecretKey) {
		SimpleResponse result = new SimpleResponse();
		/* 参数检查 */
		if (curCredential==null || EEBeanUtils.isNULL(newSecretKey)) {
			result.setSuccessful(false);
			result.addMessage("要修改的最终用户登录密码未知("+this.getClass().getName()+")");
			return result;
		} else if (EEBeanUtils.isNULL(curCredential.getPassword()) || curCredential.getEndUser()==null || EEBeanUtils.isNULL(curCredential.getEndUser().getAtid())) {
			result.setSuccessful(false);
			result.addMessage("要修改的最终用户登录秘钥参数不全，END USER标识、当前登录密码均不可为空("+this.getClass().getName()+")");
		}
		if (!result.isSuccessful())
			return result;
		
		/* 判断最终用户是否已设置过密码，没有则返回错误信息 */
		EndUserCredential existCredential = this.retrieveEndUserCredentialInfo(curCredential.getEndUser().getAtid());
		if (!existCredential.isSuccessful()) {
			result.setSuccessful(false);
			result.addMessage(existCredential.getStrMessage());
			return result;
		}
		
		/* 判断指定的最终用户是否存在 */
		EndUserInfo existEndUser = curCredential.getEndUser();
		if (EEBeanUtils.isNULL(existEndUser.getName()) && existEndUser.getMobile()==null) {//尝试从密码对象中判断人员姓名或手机是否已存在
			existEndUser = this.getEndUserInfoBizService().get(curCredential.getEndUser().getAtid());
			if (!existEndUser.isSuccessful() || EEBeanUtils.isNULL(existEndUser.getAtid())) {
				result.setSuccessful(false);
				result.addMessage("未找到指定要设置登录密码对应的最终用户");
				return result;
			}
		}
		
		/* 判断原有密码是否能匹配，不对则返回错误信息 */
		String passwordPlainText = null;
		String existPasswordPlainText = null;
		try {
			passwordPlainText = RSAUtil.decryptWithTimeMillis(getTransferRSADecrypt(), curCredential.getPassword(), 2);
			existPasswordPlainText = RSAUtil.decrypt(getStorageRSADecrypt(), existCredential.getPassword());
		} catch (EncryptException e) {
			result.setSuccessful(false);
			result.addMessage(e.toString());
			return result;
		}
		if (EEBeanUtils.isNULL(passwordPlainText) || EEBeanUtils.isNULL(existPasswordPlainText)
				|| !passwordPlainText.equals(existPasswordPlainText)) {
			result.setSuccessful(false);
			result.addMessage("原密码不正确！");
		}
		
		/* 新密码加密 */
		try {
			String newPasswordPlainText = RSAUtil.decrypt(getTransferRSADecrypt(), newSecretKey);//用传输私钥解出新密码明文
			String newPasswordCipherText = RSAUtil.encrypt(getStorageRSAEncrypt(), newPasswordPlainText);//用存储公钥加密新密码
			if (EEBeanUtils.isNULL(newPasswordCipherText))
				throw new EncryptException("修改密码前加密失败（空字符）");
			existCredential.setPassword(newPasswordCipherText);
		} catch (EncryptException e) {
			result.setSuccessful(false);
			result.addMessage(e.toString());
			return result;
		}
		
		/* 保存到数据库，再根据保存结果写缓存或返回错误信息 */
		EndUserCredential savedResult = super.save(existCredential);
		result.setSuccessful(savedResult.isSuccessful());
		if (savedResult.isSuccessful())
			SynEndUserCredential2Redis.syn(getRedisClient(), savedResult);
		else
			result.addMessage(savedResult.getStrMessage());
		
		return result;
	}

	@Override
	public SimpleResponse resetEndUserLoginPassword(String endUserId) {
		SimpleResponse result = new SimpleResponse();
		result.setSuccessful(false);
		result.addMessage("该服务暂未开放");
		return result;
	}
	
	@Override
	public EndUserCredential retrieveEndUserCredentialInfo (String endUserId) {
		EndUserCredential result = new EndUserCredential();
		/* 参数检查 */
		if (EEBeanUtils.isNULL(endUserId)) {
			result.setSuccessful(false);
			result.addMessage("最终用户标识未知");
			return result;
		}
		
		/* 从数据库取秘钥对象 */
		QueryCondition query = new QueryCondition();
		query.addCondition(new ConditionItem("endUser.atid",RangeType.EQUAL,endUserId,null));
		SimpleResultSet<EndUserCredential> existCredential = super.query(query, EndUserCredential.class);
		if (!existCredential.isSuccessful()) {
			result.setSuccessful(false);
			result.addMessage(existCredential.getStrMessage());
			return result;
		}
		
		/* 根据取得的数据构建返回结果 */
		if (existCredential.getCount()==1 && existCredential.getResultSet().size()==1) {
			result = existCredential.getResultSet().get(0);
		} else {
			result.setSuccessful(false);
			result.addMessage("匹配到该最终用户设置了个"+existCredential.getResultSet().size()+"统一登录密码");
		}
		return result;
	}

	@Override
	public StringResponse retrieveEndUserSecretKey(String endUserId) {
		StringResponse result = new StringResponse();
		/* 参数检查 */
		if (EEBeanUtils.isNULL(endUserId)) {
			result.setSuccessful(false);
			result.addMessage("未指定最终用户标识");
		}
		
		/* 从缓存取数据 */
		String ciphertext = SynEndUserCredential2Redis.get(getRedisClient(), endUserId);
		if (!EEBeanUtils.isNULL(ciphertext))
			result.setResult(ciphertext);
		
		/* 从数据库取数据 */
		if (EEBeanUtils.isNULL(result.getResult())) {
			result.setResult(this.retrieveEndUserCredentialInfo(endUserId).getPassword());
		}
		
		/* 从数据库也取不到数据 */
		if (EEBeanUtils.isNULL(result.getResult())) {
			result.setSuccessful(false);
			result.addMessage("未找到指定最终用户的秘钥（密文）");
		}
		
		return result;
	}

	@Override
	public StringResponse retrieveEndUserSecretKey(String endUserId, RSADecrypt decrypt) {
		StringResponse result = new StringResponse();
		/* 参数检查 */
		if (EEBeanUtils.isNULL(endUserId) || decrypt==null) {
			result.setSuccessful(false);
			result.addMessage("未指定最终用户标识或解密私钥未知");
		}
		
		/* 取秘钥密文 */
		StringResponse ciphertextResponse = this.retrieveEndUserSecretKey(endUserId);
		if (!ciphertextResponse.isSuccessful())
			return ciphertextResponse;
		
		/* 密文解密 */
		try {
			String plaintext = RSAUtil.decrypt(decrypt, ciphertextResponse.getResult());
			if (EEBeanUtils.isNULL(plaintext))
				throw new EncryptException("解密密码失败（空字符）");
			result.setResult(plaintext);
		} catch (EncryptException e) {
			result.setSuccessful(false);
			result.addMessage(e.toString());
		}
		return result;
	}
	
	/****************************************************************************
	**                                                                         **
	**                           Getter & Setter                               **
	**                                                                         **
	****************************************************************************/
	@Override
	public Class<?> getPojoCLS() {
		// TODO Auto-generated method stub
		return EndUserCredential.class;
	}

	/**
	 * @return the Redis客户端
	 */
	public RedisClient getRedisClient() {
		return RedisClient;
	}

	/**
	 * @param redisClient the Redis客户端 to set
	 */
	public void setRedisClient(RedisClient redisClient) {
		RedisClient = redisClient;
	}

	/**
	 * @return the 数据存储加密公钥
	 */
	public RSAEncrypt getStorageRSAEncrypt() {
		return StorageRSAEncrypt;
	}

	/**
	 * @param storageRSAEncrypt the 数据存储加密公钥 to set
	 */
	public void setStorageRSAEncrypt(RSAEncrypt storageRSAEncrypt) {
		StorageRSAEncrypt = storageRSAEncrypt;
	}

	/**
	 * @return the 数据存储解密私钥
	 */
	public RSADecrypt getStorageRSADecrypt() {
		return StorageRSADecrypt;
	}

	/**
	 * @param storageRSADecrypt the 数据存储解密私钥 to set
	 */
	public void setStorageRSADecrypt(RSADecrypt storageRSADecrypt) {
		StorageRSADecrypt = storageRSADecrypt;
	}

	/**
	 * @return the 数据传输解密私钥
	 */
	public RSADecrypt getTransferRSADecrypt() {
		return TransferRSADecrypt;
	}

	/**
	 * @param transferRSADecrypt the 数据传输解密私钥 to set
	 */
	public void setTransferRSADecrypt(RSADecrypt transferRSADecrypt) {
		TransferRSADecrypt = transferRSADecrypt;
	}

	/**
	 * @return the 最终用户信息服务
	 */
	public EndUserInfoBizService getEndUserInfoBizService() {
		return endUserInfoBizService;
	}

	/**
	 * @param endUserInfoBizService the 最终用户信息服务 to set
	 */
	public void setEndUserInfoBizService(EndUserInfoBizService endUserInfoBizService) {
		this.endUserInfoBizService = endUserInfoBizService;
	}
}
