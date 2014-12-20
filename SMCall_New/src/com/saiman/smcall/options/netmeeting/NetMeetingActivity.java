package com.saiman.smcall.options.netmeeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.saiman.smcall.R;
import com.saiman.smcall.app.MobileApplication;
import com.saiman.smcall.domain.Group;
import com.saiman.smcall.options.dial.CallPhone;
import com.saiman.smcall.util.LogUtil;

public class NetMeetingActivity extends Activity{
	List<Group> groupList = new ArrayList<Group>();
	ListView list ;
	private NMListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(MobileApplication.cacheUtils.getAsObject("grouplist") != null) {
			groupList = (ArrayList<Group>)MobileApplication.cacheUtils.getAsObject("grouplist");
			LogUtil.i(this.getClass().getSimpleName(), "grouplist的size："+groupList.size());

		}
		setContentView(R.layout.activity_nm);
		
		Button btn_bohao = (Button) findViewById(R.id.nm_btn_bohao);
		btn_bohao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NetMeetingActivity.this, BoHaoActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		Button btn_xuanhao = (Button) findViewById(R.id.nm_btn_xuanhao);
		btn_xuanhao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(NetMeetingActivity.this, XuanHaoActivity.class);
				startActivity(intent1);
				finish();
			}
		});
		
		Button nm_btn_back = (Button) findViewById(R.id.nm_btn_back);
		nm_btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		list = (ListView) findViewById(R.id.nm_list);
		adapter = new NMListAdapter(NetMeetingActivity.this, groupList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				List<SelectContactBean> xuanhaolist = groupList.get(position).getGroupList();
				if(xuanhaolist != null) {
					LogUtil.i(this.getClass().getSimpleName(), "xuanhaolist的size："+xuanhaolist.size());
				}
				Intent intent = new Intent(NetMeetingActivity.this, NMDetailActivity.class);
				intent.putExtra("xuanhaolist", (Serializable)xuanhaolist);
				startActivity(intent);
				finish();
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int position, long arg3) {
				new AlertDialog.Builder(NetMeetingActivity.this)
				.setTitle("删除")
				.setMessage("确认删除？")
				.setPositiveButton("删除",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								groupList.remove(position);
								adapter.notifyDataSetChanged();
								MobileApplication.cacheUtils.put("grouplist", (Serializable)groupList);
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
				return false;
			}
		});
	}
}
