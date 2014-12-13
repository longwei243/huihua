package com.saiman.smcall.options.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.MainActivity;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class LoginActivity extends Activity implements OnClickListener{


	private EditText mUserName;
	private EditText mPassWord;
	private SharedPreferences sharedPreferences;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_landed);
		sharedPreferences = getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		edit = sharedPreferences.edit();
		if(!"".equals(sharedPreferences.getString("UserName", ""))) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		init();
	}

	private void init() {
		
		mUserName = (EditText) findViewById(R.id.editTextId_UserName_LandingPage);
		mPassWord = (EditText) findViewById(R.id.editTextId_PassWord_LandingPage);
		Button land = (Button) findViewById(R.id.buttonId_Land_LandingPage);
		Button registration = (Button) findViewById(R.id.buttonId_Registration_LandingPage);
		Button find = (Button) findViewById(R.id.buttonId_Find_LandingPage);
		land.setOnClickListener(this);
		registration.setOnClickListener(this);
		find.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		if (networkStatusOK()) {
			switch (arg0.getId()) {
			case R.id.buttonId_Land_LandingPage:
				String userName = mUserName.getText().toString();
				String passWord = mPassWord.getText().toString();
				if (userName.length() == 11 && passWord != null) {
					final StringBuffer path = new StringBuffer(RequestUrl.loginURL
   		            		.replace("[用户手机号码]", userName)
   		            		.replace("[密码]", passWord));  
			MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
					new Response.Listener<String>() {
	
						@Override
						public void onResponse(String response) {
							LogUtil.i(this.getClass().getSimpleName(), "登陆返回数据："+response);
							if (response != null) {
								if ('1' == response.charAt(2)) {

									edit.putString("UserName", mUserName.getText()
											.toString());
									edit.putString("PassWord", mPassWord.getText()
											.toString());
									edit.commit();
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(intent);
									finish();
								} else {
									Toast.makeText(LoginActivity.this, "输入有误,请重新输入!",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					}, new Response.ErrorListener() {
	
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
						}
					}));
				} else {
					Toast.makeText(LoginActivity.this, "输入有误,请重新输入!",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.buttonId_Registration_LandingPage:
				mPassWord.setText("");
				Intent intent = new Intent(this,RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.buttonId_Find_LandingPage:
				if(mUserName.getText().toString().trim() != null && !"".equals(mUserName.getText().toString().trim())){
		            final StringBuffer path = new StringBuffer(RequestUrl.findpwdURL.replace("[用户手机号码]", mUserName.getText().toString().trim()));  
					MobileApplication.requestQueue.add(new StringRequest(Method.GET, path.toString(), 
							new Response.Listener<String>() {
			
								@Override
								public void onResponse(String response) {
									LogUtil.i(this.getClass().getSimpleName(), "找回密码返回数据："+response);
									if (response != null) {
										Show_Toast("请求成功");
									}
								}
							}, new Response.ErrorListener() {
			
								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
								}
							}));
				}else {
					Show_Toast("请输入正确手机号码");
				}
				
				break;

			default:
				break;
			}
		} else {
			Toast.makeText(this, "无网络连接...", Toast.LENGTH_SHORT).show();
		}
	}

	public void Show_Toast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			finish();
			return true;
		} else {
			return false;
		}
	}


}
