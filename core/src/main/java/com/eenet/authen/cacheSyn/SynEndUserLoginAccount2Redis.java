package com.eenet.authen.cacheSyn;

import java.util.HashMap;
import java.util.Map;

import com.eenet.authen.EndUserLoginAccount;
import com.eenet.common.cache.RedisClient;
import com.eenet.common.exception.RedisOPException;
import com.eenet.common.util.RemoveMapItemFromRedisThread;
import com.eenet.util.EEBeanUtils;

/**
 * 最终用户登录账号在Redis中的操作thread safe
 * @author Orion
 * 2016年6月7日
 */
public final class SynEndUserLoginAccount2Redis {
	
	/**
	 * 将最终用户登录账号同步到Redis
	 * @param client
	 * @param accounts
	 * 2016年6月7日
	 * @author Orion
	 */
	public static void syn(final RedisClient client, final EndUserLoginAccount... accounts) {
		if (accounts == null || accounts.length == 0 || client == null)
			return;
		try {
			ToRedis syn = new SynEndUserLoginAccount2Redis().new ToRedis(client, accounts);
			Thread thread = new Thread(syn);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();// 同步到Redis失败
		}
	}
	
	/**
	 * 根据登录账号获得最终用户登录账号对象
	 * @param client
	 * @param loginAccount
	 * @return
	 * 2016年6月7日
	 * @author Orion
	 */
	public static EndUserLoginAccount get(final RedisClient client, final String loginAccount) {
		if (EEBeanUtils.isNULL(loginAccount) || client == null)
			return null;
		
		EndUserLoginAccount account = null;
		try {
			account = EndUserLoginAccount.class.cast(client.getMapValue(AuthenCacheKey.ENDUSER_LOGIN_ACCOUNT, loginAccount));
		} catch (Exception e) {
			e.printStackTrace();//此处应该有log
		}
		return account;
	}
	
	/**
	 * 将最终用户登录账号从Redis移除
	 * @param client
	 * @param accounts
	 * 2016年6月7日
	 * @author Orion
	 */
	public static void remove(final RedisClient client, final String[] accounts) {
		RemoveMapItemFromRedisThread.execute(client, accounts, AuthenCacheKey.ENDUSER_LOGIN_ACCOUNT);
	}
	
	/**
	 * 将最终用户登录账号同步到Redis
	 * @author Orion
	 * 2016年6月7日
	 */
	private class ToRedis implements Runnable {
		private final RedisClient redisClient;
		private final EndUserLoginAccount[] accounts;
		
		public ToRedis(RedisClient redisClient, EndUserLoginAccount[] accounts) throws Exception {
			this.accounts = new EndUserLoginAccount[accounts.length];
			for (int i=0;i<this.accounts.length;i++) {
				EndUserLoginAccount dest = new EndUserLoginAccount();
				EEBeanUtils.coverProperties(dest, accounts[i]);
				this.accounts[i] = dest;
			}
			this.redisClient = redisClient;
		}
		@Override
		public void run() {
			try {
				Map<String, EndUserLoginAccount> map = new HashMap<String, EndUserLoginAccount>();
				for (EndUserLoginAccount account : this.accounts) {
					map.put(account.getLoginAccount(), account);
					this.redisClient.addMapItem(AuthenCacheKey.ENDUSER_LOGIN_ACCOUNT, map, -1);
				}
			} catch (RedisOPException e) {
				e.printStackTrace();// 缓存写入失败，do nothing
			} catch (Exception e) {
				e.printStackTrace();// 其他错误，do nothing
			}
		}
	}
}
