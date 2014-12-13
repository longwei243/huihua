package com.saiman.smcall.options.netmeeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.saiman.smcall.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBoHaoActivity extends Activity{

	
	EditText bohao_edit_num, bohao_edit_name;
	List<SelectContactBean> list = new ArrayList<SelectContactBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bohao);
		
		Button bohao_btn_back = (Button) findViewById(R.id.bohao_btn_back);
		bohao_btn_back.setOnClickListener(clickListener);
		Button bohao_btn_clear = (Button) findViewById(R.id.bohao_btn_clear);
		bohao_btn_clear.setOnClickListener(clickListener);
		Button bohao_btn_continue = (Button) findViewById(R.id.bohao_btn_continue);
		bohao_btn_continue.setOnClickListener(clickListener);
		Button bohao_btn_complete = (Button) findViewById(R.id.bohao_btn_complete);
		bohao_btn_complete.setOnClickListener(clickListener);
		
		bohao_edit_name = (EditText) findViewById(R.id.bohao_edit_name);
		bohao_edit_num = (EditText) findViewById(R.id.bohao_edit_num);
		 
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.bohao_btn_back:
				finish();
				break;
			case R.id.bohao_btn_clear:
				list.clear();
				bohao_edit_name.setText("");
				bohao_edit_num.setText("");
				break;
			case R.id.bohao_btn_continue:
				if(!"".equals(bohao_edit_num.getText().toString().trim())) {
					SelectContactBean scb = new SelectContactBean();
					scb.setPhoneNum(bohao_edit_num.getText().toString().trim());
					scb.setDesplayName(bohao_edit_name.getText().toString().trim());
					add_continue(scb);
				}else {
					Toast.makeText(AddBoHaoActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.bohao_btn_complete:
				if(!"".equals(bohao_edit_num.getText().toString().trim())) {
					SelectContactBean scb = new SelectContactBean();
					scb.setPhoneNum(bohao_edit_num.getText().toString().trim());
					scb.setDesplayName(bohao_edit_name.getText().toString().trim());
					add_complete(scb);
				}else {
					Toast.makeText(AddBoHaoActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};
	
	private void add_continue(SelectContactBean scb) {
		list.add(scb);
		bohao_edit_name.setText("");
		bohao_edit_num.setText("");
	}
	
	private void add_complete(SelectContactBean scb) {
		list.add(scb);
		
		Intent intent = new Intent(AddBoHaoActivity.this, NMDetailActivity.class);
		intent.putExtra("addbohaolist", (Serializable)list);
		startActivity(intent);
		finish();
	}

}
