/**
 * 
 */
package com.bluetooth.activitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bluetooth.broadcast.DeviceReceiver;
import com.bluetooth.service.BluetoothService;
import com.example.bluetoothtest.R;

public class SecondActivity extends BaseActivity implements OnClickListener {

	private BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
	private ArrayAdapter<String> adapter;
	private List<String> deviceList = new ArrayList<String>();
	private DeviceReceiver mydevice;
	private Intent bindIntent;
	private BluetoothDevice device = null;

	// private String address = "00:80:25:4A:1C:79";
	private String address = "00:80:25:48:CA:20";
	// 界面元素
	private ListView deviceListView;
	private Button test;
	private Button find2;
	private Button connectbutton;
	private Button start_secondActivity;
	private TextView textView;

	public static final String OPEN = "open buletooth";// 打开蓝牙按钮中显示的字符
	public static final String CLOSE = "close buletooth";
	private int BUTTONID = 0;
	private boolean hasregister = false;


	@Override
	protected void onStart() {
		// 注册蓝牙接收广播
		if (!hasregister) {
			hasregister = true;
			mydevice = new DeviceReceiver(deviceList);
			IntentFilter filterStart = new IntentFilter(
					BluetoothDevice.ACTION_FOUND);
			IntentFilter filterEnd = new IntentFilter(
					BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			registerReceiver(mydevice, filterStart);
			registerReceiver(mydevice, filterEnd);
		}
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();

		bindIntent = new Intent(this, BluetoothService.class);
		bindService(bindIntent, conn1, BIND_AUTO_CREATE);
	}

	// 解除广播
	@Override
	protected void onDestroy() {
		if (blueAdapter != null && blueAdapter.isDiscovering()) {
			blueAdapter.cancelDiscovery();
		}
		if (hasregister) {
			hasregister = false;
			unregisterReceiver(mydevice);
		}
		super.onDestroy();
		unbindService(conn1);
		stopService(bindIntent);
	}

	private void initView() {
		// 绑定listview
		deviceListView = (ListView) findViewById(R.id.devicelist);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, deviceList);
		deviceListView.setAdapter(adapter);
		textView = (TextView) findViewById(R.id.text);
		textView.setText("SecondActivity");
		// 绑定按钮元素
		test = (Button) findViewById(R.id.communicationInfo);
		find2 = (Button) findViewById(R.id.find2);
		connectbutton = (Button) findViewById(R.id.connect);

		start_secondActivity = (Button) findViewById(R.id.start_second);
		start_secondActivity.setVisibility(View.INVISIBLE);

		test.setOnClickListener(this);
		find2.setOnClickListener(this);
		connectbutton.setOnClickListener(this);
	}

	// 按钮点击事件
	@Override
	public void onClick(View v) {
		Intent bindIntent = new Intent(this, BluetoothService.class);
		switch (v.getId()) {
		case R.id.communicationInfo:
			if (test.getText().toString().equals(OPEN)) {
				bluetooth.setBluetooth();
				test.setText(CLOSE);
			} else {
				test.setText(OPEN);
				blueAdapter.disable();
			}
			break;
		case R.id.find2:
			BUTTONID = R.id.find2;
			ArrayList<String> deviceList2 = (ArrayList<String>) bluetooth
					.findAvalibleDevice(blueAdapter);
			for (Iterator it = deviceList2.iterator(); it.hasNext();) {
				String s = (String) it.next();
				deviceList.add(s);
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.connect:
			BUTTONID = R.id.connect;
			device = blueAdapter.getRemoteDevice(address);
			bluetooth.blueIOSMethod(device, blueAdapter, handler);
			break;
		default:
			break;
		}
	}

}
