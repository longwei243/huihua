package com.saiman.smcall.options.im;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.MainActivity;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.LandedDate;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class ChongZhi extends Activity implements OnClickListener {
	private EditText mCH;
	private EditText mMM;
	private EditText mSJ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chong_zhi);
		init();

	}

	private void init() {
		mCH = (EditText) findViewById(R.id.editTextId_ch_CZ);
		mMM = (EditText) findViewById(R.id.editTextId_mm_CZ);
		mSJ = (EditText) findViewById(R.id.editTextId_sj_CZ);
		Button cz = (Button) findViewById(R.id.buttonId_cz_CZ);
		Button fh = (Button) findViewById(R.id.buttonId_fh_CZ);
		Button gk = (Button) findViewById(R.id.buttonId_zx_GK);
		cz.setOnClickListener(this);
		fh.setOnClickListener(this);
		gk.setOnClickListener(this);
		SharedPreferences sharedPreferences = getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		String namees = sharedPreferences.getString("UserName", null);
		mSJ.setText(namees);
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.buttonId_cz_CZ:
			if (networkStatusOK()) {
				final String sjt = mSJ.getText().toString();
				final String cht = mCH.getText().toString();
				final String mmt = mMM.getText().toString();
				if (sjt != null) {
					
					final StringBuffer path = new StringBuffer("http://182.92.157.109:8098/api/client/UsePrepaidCard?userName="+sjt+"&cardNumber="+cht+"&cardPassword="+mmt);		
					LogUtil.i(this.getClass().getSimpleName(), "充值请求地址："+path.toString());
					MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
							new Response.Listener<String>() {
			
								@Override
								public void onResponse(String response) {
									LogUtil.i(this.getClass().getSimpleName(), "充值返回数据："+response);
									if (response != null) {
										try {
											JSONObject obj = new JSONObject(response);
											int retCode = obj.getInt("retCode");
											if(retCode == 0){
												getUserInfo();
												getShangHuInfo();
												Toast.makeText(ChongZhi.this, "充值成功", 100).show();
												finish();
											}else if(retCode == -1006) {
												Toast.makeText(ChongZhi.this, "充值卡已使用", 100).show();
											}else {
												Toast.makeText(ChongZhi.this, "充值失败", 100).show();
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								}
							}, new Response.ErrorListener() {
			
								@Override
								public void onErrorResponse(VolleyError error) {
								}
							}));
				}
			} else {
				Toast.makeText(this, "无网络连接，请检查网络状态", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.buttonId_fh_CZ:
			finish();
			break;
		case R.id.buttonId_zx_GK:
			 Uri uri = Uri.parse("http://ma.taobao.com/ezj8i");  
		      	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
		      	startActivity(it);
			break;
		}
	}
	
	private int userId;
	
	private void getUserInfo() {
		LandedDate ld = new LandedDate(ChongZhi.this);
		
		String path = RequestUrl.getUserInfo.replace("[用户名]", ld.getUserName()).replace("[密码]", ld.getPassWord());
		MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						LogUtil.i(this.getClass().getSimpleName(), "查询用户信息返回数据："+response);
						if (response != null) {
							try {
								JSONObject obj = new JSONObject(response);
								int retCode = obj.getInt("retCode");
								if(retCode == 0) {
									JSONObject accountInfo = obj.getJSONObject("accountInfo");
									userId = accountInfo.getInt("userId");
									LogUtil.i(this.getClass().getSimpleName(), "userid是："+userId);
									MobileApplication.cacheUtils.put("userId", userId+"");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}));
	}
	
	private void getShangHuInfo() {
		String path = RequestUrl.getShanghuInfo();
		MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						LogUtil.i(this.getClass().getSimpleName(), "查询商户信息返回数据："+response);
						if (response != null) {
							try {
								JSONObject obj = new JSONObject(response);
								int retCode = obj.getInt("retCode");
								if(retCode == 0) {
									String mallAddress = obj.getString("mallAddress");
									String officialWebsite = obj.getString("officialWebsite");
									if(!"".equals(mallAddress) && !"".equals(officialWebsite) && mallAddress != null && officialWebsite != null) {
										MobileApplication.cacheUtils.put("mallAddress", mallAddress);
										MobileApplication.cacheUtils.put("officialWebsite", officialWebsite);
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}));
	}
	
	
	private boolean networkStatusOK() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}