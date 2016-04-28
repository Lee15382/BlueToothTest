package com.example.activitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.bluetoothbroadcast.DeviceReceiver;
import com.example.bluetoothtest.R;
import com.example.service.BluetoothService;

public class MainActivity extends Activity implements OnClickListener {

	private BluetoothAdapter blueAdapter= BluetoothAdapter.getDefaultAdapter();
	private ArrayAdapter<String> adapter;
	private List<String> deviceList = new ArrayList<String>();
	private DeviceReceiver mydevice;
	private boolean hasregister = false;
	private BluetoothDevice device = null;
//	private String address = "00:80:25:4A:1C:79";
	private String address = "00:80:25:48:CA:20";
	// 界面元素
	private ListView deviceListView;
	private Button test;
	private Button find2;
	private Button connectbutton;

	public static final int UPDATE_TEXT = 1;
	public static final String OPEN = "open buletooth";
	public static final String CLOSE = "close buletooth";

	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TEXT:
				System.out.println(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};
	private BluetoothService.Bluetooth bluetooth;

	private ServiceConnection conn1 = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bluetooth = (BluetoothService.Bluetooth) service;
			bluetooth.setBluetooth();
		}
	};
	
	private ServiceConnection conn2 = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bluetooth = (BluetoothService.Bluetooth) service;
		    ArrayList<String> deviceList2 =(ArrayList<String>) bluetooth.findAvalibleDevice();
		   
		for(Iterator it = deviceList2.iterator(); it.hasNext();){
			String s = (String) it.next();
			deviceList.add(s);
		}
			adapter.notifyDataSetChanged();
		}
	};
	private ServiceConnection conn3 = new ServiceConnection(){
		
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bluetooth = (BluetoothService.Bluetooth) service;
			device = blueAdapter.getRemoteDevice(address);
			bluetooth.blueIOSMethod(device, blueAdapter, handler);
		}

		};

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
	}

	private void initView() {
		// 绑定listview
		deviceListView = (ListView) findViewById(R.id.devicelist);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, deviceList);
		deviceListView.setAdapter(adapter);
		// 绑定按钮元素
		test = (Button) findViewById(R.id.communicationInfo);
		find2 = (Button) findViewById(R.id.find2);
		connectbutton = (Button) findViewById(R.id.connect);
		
		test.setOnClickListener(this);
		find2.setOnClickListener(this);
		connectbutton.setOnClickListener(this);
	}

	// 按钮点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.communicationInfo:
			if (test.getText().toString().equals(OPEN)) {
				Intent bindIntent = new Intent(this, BluetoothService.class);
				bindService(bindIntent, conn1, BIND_AUTO_CREATE);
				test.setText(CLOSE);
			} else {
				unbindService(conn1);
				Intent stopIntent = new Intent(this,BluetoothService.class);
				stopService(stopIntent);
				test.setText(OPEN);
			}
			break;
		case R.id.find2:
			Intent bindIntent = new Intent(this, BluetoothService.class);
			bindService(bindIntent, conn2, BIND_AUTO_CREATE);
			break;
		case R.id.connect:
			Intent bindIntent1 = new Intent(this, BluetoothService.class);
			bindService(bindIntent1, conn3, BIND_AUTO_CREATE);
			break;
		default:
			break;
		}
	}

}
