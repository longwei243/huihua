package com.saiman.smcall.app;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MobileApplication extends Application{

	public static RequestQueue requestQueue;
	private static MobileApplication mobileApplication;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mobileApplication = this;
		requestQueue = Volley.newRequestQueue(mobileApplication);
		
		ShareSDK.initSDK(mobileApplication);
		
		JPushInterface.init(mobileApplication);
		JPushInterface.setDebugMode(false);
	}
	
	public static MobileApplication getInstance() {
		return mobileApplication;
	}
}
