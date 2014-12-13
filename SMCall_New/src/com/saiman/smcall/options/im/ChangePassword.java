package com.saiman.smcall.options.im;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.request.RequestUrl;
import com.saiman.smcall.util.LogUtil;

public class ChangePassword extends Activity {
	private EditText change_password_againpw_et, change_password_newpw_et,
			change_password_oldPw_et, change_password_phone_et;
	private Button change_password_cancel_bt, change_password_ok_bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);
		findId();
	}

	private void findId() {
		change_password_againpw_et = (EditText) findViewById(R.id.change_password_againpw_et);
		change_password_newpw_et = (EditText) findViewById(R.id.change_password_newpw_et);
		change_password_oldPw_et = (EditText) findViewById(R.id.change_password_oldPw_et);
		change_password_phone_et = (EditText) findViewById(R.id.change_password_phone_et);
		change_password_cancel_bt = (Button) findViewById(R.id.change_password_cancel_bt);
		change_password_ok_bt = (Button) findViewById(R.id.change_password_ok_bt);
		change_password_cancel_bt.setOnClickListener(onClickListener);
		change_password_ok_bt.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_password_cancel_bt:
				finish();
				break;
			case R.id.change_password_ok_bt:
				if (checkEditext(change_password_phone_et.getText().toString()
						.trim(), change_password_oldPw_et.getText().toString()
						.trim(), change_password_newpw_et.getText().toString()
						.trim(), change_password_againpw_et.getText()
						.toString().trim())) {

					MobileApplication.requestQueue.add(new StringRequest(Method.GET, RequestUrl.getChangePassword(
								change_password_phone_et.getText()
									.toString().trim(),
								change_password_oldPw_et.getText()
									.toString().trim(),
								change_password_newpw_et.getText()
									.toString().trim()), 
							new Response.Listener<String>() {
			
								@Override
								public void onResponse(String response) {
									LogUtil.i(this.getClass().getSimpleName(), "修改密码返回数据："+response);
									if (response != null) {
										Toast.makeText(ChangePassword.this, "修改成功", 100).show();
										finish();
									}
								}
							}, new Response.ErrorListener() {
			
								@Override
								public void onErrorResponse(VolleyError error) {
								
								}
							}));
				}
				break;

			default:
				break;
			}
		}
	};
	

	private Boolean checkEditext(String phone, String oldpwd, String newpwd,
			String againpwd) {
		if ("".equals(phone)) {
			Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(oldpwd) || "".equals(newpwd)
				|| "".equals(againpwd)) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (newpwd.equals(againpwd)) {
			Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}

	}
}
