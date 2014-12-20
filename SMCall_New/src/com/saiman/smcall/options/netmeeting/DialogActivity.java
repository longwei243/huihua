package com.saiman.smcall.options.netmeeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.Group;
import com.saiman.smcall.util.LogUtil;

public class DialogActivity extends Activity{

	private EditText editName;
	private Button btn_save;
	private Button btn_cancel;
	List<SelectContactBean> dataList;
	String name = "";
	List<Group> groupList = new ArrayList<Group>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		if(i.getSerializableExtra("datalist") != null) {
			dataList = (List<SelectContactBean>) i.getSerializableExtra("datalist");
			LogUtil.i(this.getClass().getSimpleName(), "datalist的size："+dataList.size());

		}
		
		if(MobileApplication.cacheUtils.getAsObject("grouplist") != null) {
			groupList = (ArrayList<Group>)MobileApplication.cacheUtils.getAsObject("grouplist");
		}
		
		setContentView(R.layout.dialog);
		
		editName = (EditText) findViewById(R.id.dialog_edit_name);
		
		btn_save = (Button) findViewById(R.id.dialog_btn_save);
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name = editName.getText().toString().trim();
				if(!"".equals(name)){
					Group g = new Group();
					g.setGroupName(name);
					g.setGroupList(dataList);
					groupList.add(g);
					MobileApplication.cacheUtils.put("grouplist", (Serializable)groupList);
					LogUtil.i(this.getClass().getSimpleName(), "群组保存成功了");
					Toast.makeText(DialogActivity.this, "保存成功", 100).show();
					finish();
				}
			}
		});
		btn_cancel = (Button) findViewById(R.id.dialog_btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
