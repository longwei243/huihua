package com.saiman.smcall.options.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.saiman.smcall.R;
import com.saiman.smcall.domain.ContactBean;
import com.saiman.smcall.domain.LandedDate;
import com.saiman.smcall.options.contact.adapter.ContactAdapter;
import com.saiman.smcall.options.dial.CallPhone;
import com.saiman.smcall.options.login.LoginActivity;
import com.saiman.smcall.view.QuickAlphabeticBar;

public class ContactFragment extends Fragment{
	private ContactAdapter adapter;
	private ListView contactList;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQueryHandler;
	private QuickAlphabeticBar alphabeticBar;
	private Map<Integer, ContactBean> contactIdMap = null;
	private View mView;
	private String contentContact = "";
	private EditText  editText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_contact, null);
		init(mView);
		return mView;
	}
	
	private void init(View view) {
		contactList = (ListView) view.findViewById(R.id.contact_list);
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				TextView name = (TextView) view.findViewById(R.id.name);
				TextView number = (TextView) view.findViewById(R.id.number);
				final String phoneName = name.getText().toString();
				String string = number.getText().toString();
				if (phoneName.equals(number.getText().toString())) {
					string = "";
				}
				final String phoneNum = string;
				if (networkStatusOK()) {
					if (getLandName()) {
						new AlertDialog.Builder(getActivity())
								.setTitle("拨号")
								.setMessage(phoneName + "   " + phoneNum)
								.setPositiveButton("呼叫",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {

												if (networkStatusOK()) {
													Intent intent = new Intent(
															getActivity(),
															CallPhone.class);
													intent.putExtra("CALLNAME",
															phoneName);
													intent.putExtra(
															"CALLNUMBER",
															phoneNum);
													getActivity()
															.startActivity(
																	intent);
												} else {
													Toast.makeText(
															getActivity(),
															"无网络,无法拨号...",
															Toast.LENGTH_SHORT)
															.show();
												}
												dialog.dismiss();
											}
										})
								.setNegativeButton("返回",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
					} else {
						Intent intent = new Intent(getActivity(),
								LoginActivity.class);
						getActivity().startActivity(intent);
						Toast.makeText(getActivity(), "登陆",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(getActivity(), "无可用网络,请检查您的网络,在进行拨号",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		alphabeticBar = (QuickAlphabeticBar) view
				.findViewById(R.id.fast_scroller);
		asyncQueryHandler = new MyAsyncQueryHandler(getActivity()
				.getContentResolver());
		init();
	}

	private boolean getLandName() {
		LandedDate landedDate = new LandedDate(getActivity());
		String passWord = landedDate.getPassWord();
		String userName = landedDate.getUserName();
		if (passWord != null || userName != null) {
			return true;
		}
		return false;
	}

	private boolean networkStatusOK() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) mView
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
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

	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
		editText = (EditText) mView
				.findViewById(R.id.editTextId_ContactList);

//		imageButton1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				contentContact = editText.getText().toString();
//				asyncQueryHandler = new MyAsyncQueryHandler(getActivity()
//						.getContentResolver());
//				init2(contentContact);
//			}
//		});
		
		contentContact = editText.getText().toString();
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				contentContact = editText.getText().toString();
				asyncQueryHandler = new MyAsyncQueryHandler(getActivity()
						.getContentResolver());
				init2(contentContact);
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
//				
//				String ss = editText.getText().toString();
//				if (ss.length() != 0) {
//					List<ContactBean> list1 = new ArrayList<ContactBean>();
//					for (int i = 0; i < list.size(); i++) {
//						ContactBean contactBean = list.get(i);
//						String name = contactBean.getDesplayName();
//						String num = contactBean.getPhoneNum();
//						String key = contactBean.getSortKey();
//						Long photoId = contactBean.getPhotoId();
//						String lookUpKey = contactBean.getLookUpKey();
//						if (name.contains(ss) || num.contains(ss)
//						if (PingYinUtil.getFirstSpell(name).contains(ss) || num.contains(ss)
//								|| key.contains((ss.toUpperCase()))) {
//							ContactBean contactBean2 = new ContactBean();
//							contactBean2.setDesplayName(name);
//							contactBean2.setPhoneNum(num);
//							contactBean2.setSortKey(key);
//							contactBean2.setPhotoId(photoId);
//							contactBean2.setLookUpKey(lookUpKey);
//							list1.add(contactBean2);
//						}
//						contactList.setAdapter(new ContactAdapter(mView
//								.getContext(), list1, alphabeticBar));
//					}
//				} else {
//					contactList.setAdapter(new ContactAdapter(mView
//							.getContext(), list, alphabeticBar));
//				}
			}
		});
	}
//	public void do_search(View v){
//		contentContact = editText.getText().toString();
//		asyncQueryHandler = new MyAsyncQueryHandler(getActivity()
//				.getContentResolver());
//		init2(contentContact);
//	}

//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 24367:
//				List<ContactBean> beans = (List<ContactBean>) msg.obj;
//				// if (beans.size() != 0) {
//				contactList.setAdapter(new ContactAdapter(getActivity(),
//						beans, alphabeticBar));
//				// } else {
//				// contactList.setAdapter(new ContactAdapter(mView
//				// .getContext(), list, alphabeticBar));
//				// }
//				break;
//			}
//		}
//	};

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//			if (cursor != null && cursor.getCount() > 0) {
//				contactIdMap = new HashMap<Integer, ContactBean>();
//				list = new ArrayList<ContactBean>();
//				cursor.moveToFirst();
//				for (int i = 0; i < cursor.getCount(); i++) {
//					cursor.moveToPosition(i);
//					String name = cursor.getString(1);
//					String number = cursor.getString(2);
//					String sortKey = cursor.getString(3);
//					int contactId = cursor.getInt(4);
//					Long photoId = cursor.getLong(5);
//					String lookUpKey = cursor.getString(6);
//
////					if (contactIdMap.containsKey(contactId)) {
////
////					} else {
//
//						ContactBean contact = new ContactBean();
//						contact.setDesplayName(name);
//						contact.setPhoneNum(number);
//						contact.setSortKey(sortKey);
//						contact.setPhotoId(photoId);
//						contact.setLookUpKey(lookUpKey);
//						list.add(contact);
//
//						contactIdMap.put(contactId, contact);
////					}
//				}
//				if (list.size() > 0) {
//					setAdapter(list);
//				}
//			}
			if (cursor != null && cursor.getCount() > 0) {

				contactIdMap = new HashMap<Integer, ContactBean>();

				list = new ArrayList<ContactBean>();

				Pattern p = Pattern.compile("[a-zA-Z]");
				Matcher m = p.matcher(contentContact);
				if (m.find()) {
					
					cursor.moveToFirst();
					for (int i = 0; i < cursor.getCount(); i++) {

						cursor.moveToPosition(i);
						String name = cursor.getString(1);
						String number = cursor.getString(2);
						String sortKey = cursor.getString(3);
						int contactId = cursor.getInt(4);
						Long photoId = cursor.getLong(5);
						String lookUpKey = cursor.getString(6);
						
						FirstLetterUtil.getFirstLetter(name);
                        System.out.println("---contentContact是拼音字母---" + number);
//						if (contactIdMap.containsKey(contactId)) {
//
//						} else {
							if (PingYinUtil.getFirstSpell(name).contains(contentContact.toLowerCase())) {
								ContactBean cb = new ContactBean();

								cb.setDesplayName(name);
								if (number.contains(" ")) {
									number = number.replace(" ", "");
								}
							
								if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
									cb.setPhoneNum(number.substring(3));
								} else {
									cb.setPhoneNum(number);
								}
								cb.setSortKey(sortKey);
								cb.setContactId(contactId);
								cb.setPhotoId(photoId);
								cb.setLookUpKey(lookUpKey);
								list.add(cb);

								contactIdMap.put(contactId, cb);

							}
//						}
					}
				} else {
					cursor.moveToFirst();
					for (int i = 0; i < cursor.getCount(); i++) {

						cursor.moveToPosition(i);
						String name = cursor.getString(1);
						String number = cursor.getString(2);
						String sortKey = cursor.getString(3);
						int contactId = cursor.getInt(4);
						Long photoId = cursor.getLong(5);
						String lookUpKey = cursor.getString(6);
						System.out.println("---contentContact不是拼音---" + number);
//						if (contactIdMap.containsKey(contactId)) {
//
//						} else {

							ContactBean cb = new ContactBean();
							cb.setDesplayName(name);
							if (number.contains(" ")) {
								number = number.replace(" ", "");
							}
							if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
								cb.setPhoneNum(number.substring(3));
							} else {
								cb.setPhoneNum(number);
							}
							cb.setSortKey(sortKey);
							cb.setContactId(contactId);
							cb.setPhotoId(photoId);
							cb.setLookUpKey(lookUpKey);
							list.add(cb);

							contactIdMap.put(contactId, cb);

//						}
					}
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
				cursor.close();
			}
		}

	}

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactAdapter(getActivity(), list, alphabeticBar);
		contactList.setAdapter(adapter);
		alphabeticBar.init(mView);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}
	private void init2(String inptxt) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY }; // 查询的列
		Pattern p = Pattern.compile("[a-zA-Z]");
		Matcher m = p.matcher(inptxt);
		if (m.find()) {
			// System.out.println("进入了字母查询");
			asyncQueryHandler.startQuery(0, null, uri, projection, null, null, "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
		} else {
			// System.out.println("没有进入字母查询");
			asyncQueryHandler.startQuery(0, null, uri, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like '%" + inptxt + "%' or " + ContactsContract.CommonDataKinds.Phone.NUMBER
					+ " like '%" + inptxt + "%'", null, "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询

		}
	}
}
