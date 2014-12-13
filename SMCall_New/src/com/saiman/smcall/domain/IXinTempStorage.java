package com.saiman.smcall.domain;

import java.util.List;
import java.util.Map;

import android.view.View;

public class IXinTempStorage {
	private static IXinTempStorage iXinTempStorage = null;
	// 登陆手机号
	private String mLandingPhone = null;
	// 登陆密码
	private String mLoginPassword = null;
	// 全部人的通讯录
	private List<Map<String, String>> mMailList = null;
	// 全部的通话记录
	private List<Map<String, String>> mCallRecords = null;
	//匹配的通讯录
	private List<Map<String, String>> mMatchingMailList = null;
	// 匹配的通话记录
	private List<Map<String, String>> mMatchingRecords = null;

	private List<View> mListBitmaps = null;

	private List<View> mListCallImages = null;

	private View view;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	private IXinTempStorage() {
		super();
	}

	public List<View> getmListCallImages() {
		return mListCallImages;
	}

	public void setmListCallImages(List<View> mListCallImages) {
		this.mListCallImages = mListCallImages;
	}

	public List<View> getmListBitmaps() {
		return mListBitmaps;
	}

	public void setmListBitmaps(List<View> mListBitmaps) {
		this.mListBitmaps = mListBitmaps;
	}

	public static IXinTempStorage getIXinTempStorage() {
		if (iXinTempStorage == null) {
			iXinTempStorage = new IXinTempStorage();
		}
		return iXinTempStorage;
	}

	public String getmLandingPhone() {
		return mLandingPhone;
	}

	public void setmLandingPhone(String mLandingPhone) {
		this.mLandingPhone = mLandingPhone;
	}

	public String getmLoginPassword() {
		return mLoginPassword;
	}

	public void setmLoginPassword(String mLoginPassword) {
		this.mLoginPassword = mLoginPassword;
	}

	public List<Map<String, String>> getmMailList() {
		return mMailList;
	}

	public void setmMailList(List<Map<String, String>> mMailList) {
		this.mMailList = mMailList;
	}

	public List<Map<String, String>> getmCallRecords() {
		return mCallRecords;
	}

	public void setmCallRecords(List<Map<String, String>> mCallRecords) {
		this.mCallRecords = mCallRecords;
	}

	public List<Map<String, String>> getmMatchingMailList() {
		return mMatchingMailList;
	}

	public void setmMatchingMailList(List<Map<String, String>> mMatchingMailList) {
		this.mMatchingMailList = mMatchingMailList;
	}

	public List<Map<String, String>> getmMatchingRecords() {
		return mMatchingRecords;
	}

	public void setmMatchingRecords(List<Map<String, String>> mMatchingRecords) {
		this.mMatchingRecords = mMatchingRecords;
	}
}
