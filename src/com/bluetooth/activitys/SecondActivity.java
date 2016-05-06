/**
 * 
 */
package com.bluetooth.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetooth.broadcast.BlueInfoReceiver;

import com.example.bluetoothtest.R;

public class SecondActivity extends Activity implements OnClickListener {

	private TextView textView;
	private Button secondButton;

	private IntentFilter intentFilter;
	private BlueInfoReceiver blueInfoReceiver;
	private String text = " ";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				text = text + msg.obj.toString();
				textView.setText(text);
				// textView.invalidate();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onStart() {
		// 注册蓝牙接收广播O
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.bluetooth.broadcast.BLUEINFO");
		blueInfoReceiver = new BlueInfoReceiver();
		registerReceiver(blueInfoReceiver, intentFilter);
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		textView = (TextView) findViewById(R.id.textView);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		secondButton = (Button) findViewById(R.id.secondButton);
		secondButton.setOnClickListener(this);
	}

	// 解除广播
	@Override
	protected void onDestroy() {
		unregisterReceiver(blueInfoReceiver);
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.secondButton:
			method();
			break;
		default:
			break;
		}

	}

	public void method() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					String s = blueInfoReceiver.getInfo();
					Message message = new Message();
					message.obj = s;
					message.what = 1;
					handler.sendMessage(message);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}
}
