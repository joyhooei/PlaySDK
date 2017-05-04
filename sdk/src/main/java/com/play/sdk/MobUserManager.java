package com.play.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.play.common.Config;
import com.play.util.ConfigUtil;
import com.play.util.DeviceInfo;
import com.play.util.Encryption;
import com.play.util.SPUtil;

public class MobUserManager {
	// private static final String GAME_KEY = "^&*(YUI";
	private static final String ACCOUNT_LIST = "account_list";
	private static final String ACCOUNT_ORDER_LIST = "account_order_list";

	public static final String CONFIG_KEY_RSA = "rsa";
	public static final String CONFIG_KEY_FB = "FBid";
	public static final String CONFIG_KEY_TWITTER_ID = "twitter-key";
	public static final String CONFIG_KEY_TWITTER_KEY = "twitter-secret";

	private static MobUserManager m_instance;
	private DeviceInfo deviceInfo;
	private MobUser currentUser;

	// private boolean isShowLog = false;
	// private boolean isDevMode = true;

	private SharedPreferences accountSP;
	private SharedPreferences cacheAccountSP;
	private SharedPreferences accountOrderSP;

	private String gwUrl = "";
	private String serviceUrl = "";
	private String giftUrl = "";
	private String fbUrl = "";
	private String serviceHost = "";
	private String fb_copywriter = "";

	private int statType = 0;

	private JSONObject noticeContent;
	private JSONObject configJson;
	private boolean isHadShowNotice = false;

	private int rankType = 0;

	private boolean isRankDataUpload = false;

	private boolean isLoginIng = false;

	public void setStatType(int statType) {
		this.statType = statType;
	}

	public int getStatType() {
		return statType;
	}

	public void setIsLoginIng(boolean is) {
		isLoginIng = is;
	}

	public boolean isLoginIng() {
		return isLoginIng;
	}

	public void setRankDataUpload(boolean isRankDataUpload) {
		this.isRankDataUpload = isRankDataUpload;
	}

	public boolean isRankDataUpload() {
		return isRankDataUpload;
	}

	public int getRankType() {
		return rankType;
	}

	public void setRankType(int rankType) {
		this.rankType = rankType;
	}

	public String getFb_copywriter() {
		return fb_copywriter;
	}

	public void setFb_copywriter(String fb_copywriter) {
		this.fb_copywriter = fb_copywriter;
	}


	public boolean isHadShowNotice() {
		return isHadShowNotice;
	}

	public void setHadShowNotice(boolean isHadShowNotice) {
		this.isHadShowNotice = isHadShowNotice;
	}

	public MobUser getCurrentUser() {
		return currentUser;
	}

	public String getCurrentUserId() {
		return accountOrderSP.getString("currentUser", null);
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public JSONObject getConfigJson(Context context) {
		if (configJson == null) {
			String cacheStr = SPUtil.getConfigJsonString(context);
			if (cacheStr != null && cacheStr.length() > 0) {
				try {
					configJson = new JSONObject(Encryption.decryptDES(cacheStr));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return configJson;
	}

	public JSONObject getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(JSONObject notice) {
		this.noticeContent = notice;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
		ConfigUtil.setConfig(ConfigUtil.MENU_SERVICES, serviceHost != null && serviceHost.length() > 0);
	}



	public void setCacheUser(MobUser user) {
		cacheAccountSP.edit().putString("cacheUser", user.toCacheJson()).commit();
	}

	public MobUser getCacheUser() {
		return new MobUser().toCacheUser(cacheAccountSP.getString("cacheUser", ""));
	}

	public String getGwUrl() {
		return gwUrl;
	}

	public void setGwUrl(String gwUrl) {
		this.gwUrl = gwUrl;
		ConfigUtil.setConfig(ConfigUtil.MENU_HOMEPAGE, gwUrl != null && gwUrl.length() > 0);
	}


	public synchronized static MobUserManager initUserManager(Context ctx, boolean isShowLog) {
		if (m_instance == null)
			m_instance = new MobUserManager(ctx, isShowLog);
		return m_instance;
	}

	public synchronized static MobUserManager getInstance() {
		return m_instance;
	}

	private MobUserManager(Context ctx, boolean isShowLog) {
		this.deviceInfo = DeviceInfo.getInstance(ctx);
		Config.isShowLog = isShowLog;

		cacheAccountSP = ctx.getSharedPreferences("cache_account", Context.MODE_PRIVATE);
		accountSP = ctx.getSharedPreferences(ACCOUNT_LIST, Context.MODE_PRIVATE);
		accountOrderSP = ctx.getSharedPreferences(ACCOUNT_ORDER_LIST, Context.MODE_PRIVATE);


	}


	/**
	 * 保存一个登录账号
	 * 
	 * @param userid
	 * @param jsonStr
	 */
	public void saveAccount(String userid, String jsonStr) {
		accountSP.edit().putString(userid, jsonStr).commit();
		accountOrderSP.edit().putString("currentUser", userid).commit();
	}

	/**
	 * 移除一个登录账号
	 * 
	 * @param userid
	 */
	public void removeAccount(String userid) {
		Map<String, ?> list = accountSP.getAll();
		for (String key : list.keySet()) {
			if (key.equals(userid)) {
				accountSP.edit().remove(key).commit();
				accountList();
				break;
			}
		}
		if (userid.equals(getCurrentUserId())) {
			accountOrderSP.edit().remove("currentUser").commit();
		}
	}

	/**
	 * 获得登录过的账号
	 * 
	 * @return
	 */
	public List<MobUser> accountList() {
		Map<String, ?> list = accountSP.getAll();
		List<MobUser> userList = new ArrayList<MobUser>();
		String currentId = getCurrentUserId();
		if (currentId != null) {
			userList.add(userByUserId(currentId));
		}
		for (String key : list.keySet()) {
			MobUser user = new MobUser((String) list.get(key));
			if (!userList.contains(user))
				userList.add(user);
		}

		return userList;
	}

	/**
	 * 获得一个登录过的账号
	 * 
	 * @param userid
	 * @return
	 */
	public MobUser userByUserId(String userid) {
		Map<String, ?> list = accountSP.getAll();
		for (String key : list.keySet()) {
			MobUser user = new MobUser((String) list.get(key));
			if (user.getUserid().equalsIgnoreCase(userid)) {
				return user;
			}
		}
		return null;
	}

	public void destroy() {
		m_instance = null;
	}

}
