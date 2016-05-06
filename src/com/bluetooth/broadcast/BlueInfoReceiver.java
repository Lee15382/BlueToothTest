package com.bluetooth.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BlueInfoReceiver extends BroadcastReceiver {

	public String s;

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
		s = intent.getStringExtra("info");
		System.out.println("Àî×³" + s);
	}

	@SuppressWarnings("unused")
	public String getInfo() {
		return s;
	}
}