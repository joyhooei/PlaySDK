package com.play.util;

import android.content.Context;
import android.content.res.Resources;

import com.play.sdk.PlaySDK;

public class ConfigUtil {
	public static final String LOGIN_FACEBOOK = "facebook_login";
	public static final String LOGIN_GOOGLE = "google_login";
	public static final String LOGIN_TWITTER = "twitter_login";
	public static final String LOGIN_INSTAGRAM = "instagram_login";
	public static final String LOGIN_RC = "rc_login";
	public static final String LOGIN_WX = "wx_login";

	public static final String MENU_ACCOUNT = "menu_account";
	public static final String MENU_HOMEPAGE = "menu_homepage";
	public static final String MENU_SERVICES = "menu_service";
	public static final String MENU_FACEBOOK = "menu_facebook";

	public static final String LOGIN_CLOSE = "login_close";

	public static void setConfig(String configName, boolean enable) {
		try {
			SPUtil.setConfigSettings(PlaySDK.getInstance().getActivity(), configName, enable ? 1 : -1);
		} catch (Exception e) {
		}
	}

	public static boolean getConfigEnable(Context ctx, String configName) {
		int enable = SPUtil.getConfigSettings(ctx, configName);
		if (enable != 0) {
			return enable == 1 ? true : false;
		}
		Resources res = ctx.getResources();
		try {
			boolean b = res.getBoolean(res.getIdentifier("enable_" + configName, "bool", ctx.getPackageName()));
			return b;
		} catch (Exception e) {
			LogUtil.printHTTP(configName);
			e.printStackTrace();
			return false;
		}
	}

	public static boolean getConfigEnable(String configName) {
		Context c = PlaySDK.getInstance().getActivity();
		return getConfigEnable(c, configName);
	}


	private static int thirdLoginCount = -1;

	public static int getThirdLoginCount() {
		if (thirdLoginCount > 0)
			return thirdLoginCount;
		thirdLoginCount = 0;
		if (ConfigUtil.getConfigEnable(LOGIN_FACEBOOK))
			thirdLoginCount++;
		if (ConfigUtil.getConfigEnable(LOGIN_GOOGLE))
			thirdLoginCount++;
		if (ConfigUtil.getConfigEnable(LOGIN_INSTAGRAM))
			thirdLoginCount++;
		if (ConfigUtil.getConfigEnable(LOGIN_TWITTER))
			thirdLoginCount++;
		if (ConfigUtil.getConfigEnable(LOGIN_RC))
			thirdLoginCount++;
		return thirdLoginCount;
	}

	public static int[] getMenuOrigin() {
		Context c = PlaySDK.getInstance().getActivity();
		Resources res = c.getResources();
		try {
			return res.getIntArray(res.getIdentifier("sdk_menu_origin", "array", c.getPackageName()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int[] getScrollAdOrigin() {
		Context c = PlaySDK.getInstance().getActivity();
		Resources res = c.getResources();
		try {
			return res.getIntArray(res.getIdentifier("sdk_scroll_ad_origin", "array", c.getPackageName()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setFacebookId(String id) {
		if (id != null && id.length() > 0) {
			setConfig(LOGIN_FACEBOOK, true);
//			FacebookSdk.setApplicationId(id);
		} else {
			setConfig(LOGIN_FACEBOOK, false);
		}
	}

}
