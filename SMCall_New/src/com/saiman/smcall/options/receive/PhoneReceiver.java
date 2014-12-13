package com.saiman.smcall.options.receive;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

public class PhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		switch (tm.getCallState()) {
		case TelephonyManager.CALL_STATE_OFFHOOK:
			// 电话打进来接通状态；电话打出时首先监听到的状态。
			// Log.i("onCallStateChanged", "CALL_STATE_OFFHOOK");

			break;
		case TelephonyManager.CALL_STATE_RINGING:// 电话打进来状态״̬
			PhoneUtils.autoAnswerPhone(context, tm);

			break;
		case TelephonyManager.CALL_STATE_IDLE:
			// 不管是电话打出去还是电话打进来都会监听到的状态。
			Cursor cursor = context.getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null, null, null, null);
			ContentResolver resolver = context.getContentResolver();

			if (cursor != null && resolver != null) {

				while (cursor.moveToNext()) {
					String number = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.NUMBER));
					int id = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls._ID));
					if ("-1".equals(number) || "-2".equals(number)
							|| "".equals(number) || number == null) {
						resolver.delete(CallLog.Calls.CONTENT_URI,
								CallLog.Calls._ID + "=?", new String[] { id
										+ "" });
					}
				}
			}
			
		}
	}
}
