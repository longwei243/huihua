package com.saiman.smcall.app;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.CacheUtils;

public class MobileApplication extends Application{

	public static RequestQueue requestQueue;
	private static MobileApplication mobileApplication;
	public static CacheUtils cacheUtils;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mobileApplication = this;
		requestQueue = Volley.newRequestQueue(mobileApplication);
		cacheUtils = CacheUtils.get(mobileApplication);
		ShareSDK.initSDK(mobileApplication);
		
		JPushInterface.init(mobileApplication);
		JPushInterface.setDebugMode(false);
		JPushInterface.setAlias(mobileApplication, RequestUrl.userid, null);
	}
	
	public static MobileApplication getInstance() {
		return mobileApplication;
	}
}
