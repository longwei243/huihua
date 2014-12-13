package com.saiman.smcall.options.receive;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

public class PhoneUtils {
	static String TAG = "PhoneUtils";

	/**
	 * 从TelephonyManager中实例化ITelephony,并返�?
	 */
	static public ITelephony getITelephony(TelephonyManager telMgr)
			throws Exception {
		Method getITelephonyMethod = telMgr.getClass().getDeclaredMethod(
				"getITelephony");
		getITelephonyMethod.setAccessible(true);// 私有化函数也能使�?
		return (ITelephony) getITelephonyMethod.invoke(telMgr);
	}

	//自动接听
	public static synchronized void autoAnswerPhone(Context c,
			TelephonyManager tm) {
		try {
			ITelephony itelephony = getITelephony(tm);
			itelephony.answerRingingCall();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
				KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_HEADSETHOOK);
				intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
				c.sendOrderedBroadcast(intent,
						"android.permission.CALL_PRIVILEGED");
				intent = new Intent("android.intent.action.MEDIA_BUTTON");
				keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_HEADSETHOOK);
				intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
				c.sendOrderedBroadcast(intent,
						"android.permission.CALL_PRIVILEGED");
			} catch (Exception e3) {
				e3.printStackTrace();
				try {
					Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
					localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					localIntent1.putExtra("state", 1);
					localIntent1.putExtra("microphone", 1);
					localIntent1.putExtra("name", "Headset");
					c.sendOrderedBroadcast(localIntent1,
							"android.permission.CALL_PRIVILEGED");
				} catch (Exception e4) {
					e4.printStackTrace();
					try {
						Intent localIntent2 = new Intent(
								Intent.ACTION_MEDIA_BUTTON);
						KeyEvent localKeyEvent1 = new KeyEvent(
								KeyEvent.ACTION_DOWN,
								KeyEvent.KEYCODE_HEADSETHOOK);
						localIntent2.putExtra("android.intent.extra.KEY_EVENT",
								localKeyEvent1);
						c.sendOrderedBroadcast(localIntent2,
								"android.permission.CALL_PRIVILEGED");
					} catch (Exception e5) {
						e5.printStackTrace();
						try {
							Intent localIntent3 = new Intent(
									Intent.ACTION_MEDIA_BUTTON);
							KeyEvent localKeyEvent2 = new KeyEvent(
									KeyEvent.ACTION_UP,
									KeyEvent.KEYCODE_HEADSETHOOK);
							localIntent3.putExtra(
									"android.intent.extra.KEY_EVENT",
									localKeyEvent2);
							c.sendOrderedBroadcast(localIntent3,
									"android.permission.CALL_PRIVILEGED");
						} catch (Exception e6) {
							e6.printStackTrace();
							try {
								Intent localIntent4 = new Intent(
										Intent.ACTION_HEADSET_PLUG);
								localIntent4
										.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
								localIntent4.putExtra("state", 0);
								localIntent4.putExtra("microphone", 1);
								localIntent4.putExtra("name", "Headset");
								c.sendOrderedBroadcast(localIntent4,
										"android.permission.CALL_PRIVILEGED");
							} catch (Exception e7) {
								e7.printStackTrace();
								try {
									Intent meidaButtonIntent = new Intent(
											Intent.ACTION_MEDIA_BUTTON);
									KeyEvent keyEvent = new KeyEvent(
											KeyEvent.ACTION_UP,
											KeyEvent.KEYCODE_HEADSETHOOK);
									meidaButtonIntent.putExtra(
											Intent.EXTRA_KEY_EVENT, keyEvent);
									c.sendOrderedBroadcast(meidaButtonIntent,
											null);
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
							// }
						}
					}
				}
				// try {
				// Intent intent = new Intent(
				// "android.intent.action.MEDIA_BUTTON");
				// KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
				// KeyEvent.KEYCODE_HEADSETHOOK);
				// intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
				// c.sendOrderedBroadcast(intent,
				// "android.permission.CALL_PRIVILEGED");
				// intent = new Intent("android.intent.action.MEDIA_BUTTON");
				// keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
				// KeyEvent.KEYCODE_HEADSETHOOK);
				// intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
				// c.sendOrderedBroadcast(intent,
				// "android.permission.CALL_PRIVILEGED");
				//
				// Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
				// localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				// localIntent1.putExtra("state", 1);
				// localIntent1.putExtra("microphone", 1);
				// localIntent1.putExtra("name", "Headset");
				// c.sendOrderedBroadcast(localIntent1,
				// "android.permission.CALL_PRIVILEGED");
				//
				// Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
				// KeyEvent localKeyEvent1 = new KeyEvent(
				// KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
				// localIntent2.putExtra("android.intent.extra.KEY_EVENT",
				// localKeyEvent1);
				// c.sendOrderedBroadcast(localIntent2,
				// "android.permission.CALL_PRIVILEGED");
				//
				// Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
				// KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
				// KeyEvent.KEYCODE_HEADSETHOOK);
				// localIntent3.putExtra("android.intent.extra.KEY_EVENT",
				// localKeyEvent2);
				// c.sendOrderedBroadcast(localIntent3,
				// "android.permission.CALL_PRIVILEGED");
				//
				// Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
				// localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				// localIntent4.putExtra("state", 0);
				// localIntent4.putExtra("microphone", 1);
				// localIntent4.putExtra("name", "Headset");
				// c.sendOrderedBroadcast(localIntent4,
				// "android.permission.CALL_PRIVILEGED");
				// } catch (Exception e3) {
				// e2.printStackTrace();
				// }

			}
		}
	}

	// 自动挂断
	// public static synchronized void endPhone(Context c, TelephonyManager tm)
	// {
	// try {
	// Log.i(TAG, "endPhone");
	// ITelephony iTelephony;
	// Method getITelephonyMethod = TelephonyManager.class
	// .getDeclaredMethod("getITelephony", (Class[]) null);
	// getITelephonyMethod.setAccessible(true);
	// iTelephony = (ITelephony) getITelephonyMethod.invoke(tm,
	// (Object[]) null);
	// // 挂断电话
	// iTelephony.endCall();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
