package com.saiman.smcall.request;

import com.saiman.smcall.app.MobileApplication;

public class RequestUrl {
	//商户id9815
	public static String userid = "2870";
	//官网
	public static String pathGuanwang() {
		String url = "http://182.92.157.215/index.php?g=Wap&m=Index&a=index&token=nmzpjq1414203648";
		if(MobileApplication.cacheUtils.getAsString("officialWebsite") != null) {
			url = MobileApplication.cacheUtils.getAsString("officialWebsite");
		}
		return url;
	} 
	//商店
	public static String pathShop() {
		String url = "http://182.92.157.215/index.php?g=Wap&m=Store&a=cats&token=nmzpjq1414203648";
		if(MobileApplication.cacheUtils.getAsString("mallAddress") != null) {
			url = MobileApplication.cacheUtils.getAsString("mallAddress");
		}
		return url;
	} 
	//广告
	public static String pathMainActivity() {
		String id = userid;
		if(MobileApplication.cacheUtils.getAsString("userId") != null) {
			id = MobileApplication.cacheUtils.getAsString("userId");
		}
		return "http://182.92.157.109:8098/api/adv/GetAdvByUser?userid="+id+"&Page=1&PageSize=5&Tag=android";
	} 
	//版本
	public static String pathVesion(String id) {
		return "http://182.92.157.109/api/agent/clientinfo?userid="+id+"&os=1";
	} 
	//在线充值
	public static String pathzaixianchongzhi = "http://ma.taobao.com/ezj8i";
	//修改密码
	public static String getChangePassword(String phone,String oldPwd,String newPwd ){
		return "http://182.92.157.109/api/mapp/login.aspx?a="+phone+"b="+oldPwd+"c="+newPwd;
	}
	//注册
	public static String registURL = "http://182.92.157.109/api/mapp/zc.aspx?a=[手机号码]&b=[验证码]&c=[推介人]&d=[用户密码]&s=0";
	//登陆
	public static String loginURL ="http://182.92.157.109/api/mapp/login.aspx?a=[用户手机号码]&b=[密码]&s=0";
	//找回密码
	public static String findpwdURL ="http://182.92.157.109/m/GetVoiceCode.aspx?number=[用户手机号码]";
	//打电话
	public static String callURL ="http://182.92.157.109/api/mapp/call.aspx?a=[主叫]&b=[被叫]&c=[透传]&s=0↓0↓1↓0↓end";
//	public static String callURL ="http://182.92.157.109:8098/api/client/MakeCallBack?Caller=[主叫]&Called=[被叫]&isShow=1";
	//查询
	public static String chaxunURL ="http://182.92.157.109/api/mapp/login.aspx?a=[用户手机号码]&b=[密码]&s=0";
	//充值
//	public static String chongzhiURL ="http://182.92.157.109/api/mapp/paybycard.aspx?a=[用户手机号码]&b=[卡号]&c=[卡密]&s=0";
	//创建会议
	public static String createMeetingURL ="http://182.92.157.109:8099/api/mobile/conferencecreate?Phone=[手机号]&password=[密码]&callNumber=[主叫号码]&callName=[主叫姓名]&title=[标题]&isRecording=0&isRecall=0&isShutup=0&hostNumber=[主持人手机号]";
	//加入会议
	public static String joinMeetingURL ="http://182.92.157.109:8099/api/mobile/conferencejoin?meetId=[会议id]&number=[被叫号码]&callNumber=[主叫号码]";
	//用户详细信息
	public static String getUserInfo ="http://182.92.157.109:8098/api/client/Logon?userName=[用户名]&Password=[密码]";
	//商户信息
	public static String getShanghuInfo() {
		String id = userid;
		if(MobileApplication.cacheUtils.getAsString("userId") != null) {
			id = MobileApplication.cacheUtils.getAsString("userId");
		}
		return "http://182.92.157.109:8098/api/Merchants/GetMerchantInfoByCustomers?userId="+id;
	}

	
}
