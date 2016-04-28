package com.example.activitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.bluetoothbroadcast.DeviceReceiver;
import com.example.bluetoothtest.R;
import com.example.service.BluetoothService;
import com.example.service.BluetoothService.Bluetooth;
import com.example.tools.BlueToothConnectThread;
import com.example.tools.BlueToothIOStream;

public class MainActivity extends Activity implements OnClickListener {

	private BluetoothAdapter blueAdapter;
	private ArrayAdapter<String> adapter;
	private List<String> deviceList = new ArrayList<String>();
	private DeviceReceiver mydevice;
	private boolean hasregister = false;
	private BlueToothConnectThread connect;
	private BluetoothDevice device = null;
	private String address = "00:80:25:4A:1C:79";
	private BlueToothIOStream blueToothIOStream;

	// 界面元素
	private ListView deviceListView;
	private Button start;
	private Button findBlue;
	private Button communication;
	private Button test;
	private Button find2;

	public static final int UPDATE_TEXT = 1;
	public static final String OPEN = "open buletooth";
	public static final String CLOSE = "close buletooth";

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
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bluetooth = (BluetoothService.Bluetooth) service;
			bluetooth.setBluetooth();
			

		}
	};
	
	private ServiceConnection conn2 = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		
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
//		unbindService(conn);
//		Intent stopIntent = new Intent(this,BluetoothService.class);
//		stopService(stopIntent);
		
		super.onDestroy();
	}

	private void initView() {
		// 绑定listview
		deviceListView = (ListView) findViewById(R.id.devicelist);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, deviceList);
		deviceListView.setAdapter(adapter);
		// 绑定按钮元素
		start = (Button) findViewById(R.id.start);
		findBlue = (Button) findViewById(R.id.findBlue);
		communication = (Button) findViewById(R.id.communication);
		test = (Button) findViewById(R.id.communicationInfo);
		start.setOnClickListener(this);
		findBlue.setOnClickListener(this);
		communication.setOnClickListener(this);
		test.setOnClickListener(this);
		find2 = (Button) findViewById(R.id.find2);
		find2.setOnClickListener(this);
	}

	// 按钮点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			setBluetooth();
			break;
		case R.id.findBlue:
			if (blueAdapter.isDiscovering()) {
				blueAdapter.cancelDiscovery();
				findBlue.setText("Repeat Searcher");
			} else {
				findAvalibleDevice();
				blueAdapter.startDiscovery();
				findBlue.setText("Stop Searcher");
			}
			break;
		case R.id.communication:
			// device是要连接的设备，通过获得地址address（00:80:25:4A:1C:79）来构建
			device = blueAdapter.getRemoteDevice(address);
			connect = new BlueToothConnectThread(device, blueAdapter);
			connect.start();
			blueToothIOStream = new BlueToothIOStream(connect.socket, handler);
			blueToothIOStream.start();
			break;

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
		default:
			break;
		}
	}

	private void setBluetooth() {
		// 获取蓝牙适配器
		blueAdapter = BluetoothAdapter.getDefaultAdapter();
		// 检查是否获得
		if (blueAdapter != null) {
			// 获得蓝牙，蓝牙没有开启，则请求开启
			if (!blueAdapter.isEnabled()) {
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(intent);
				// 使蓝牙设备可见，方便配对
				Intent in = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
				startActivity(in);
				// 直接开启，不经过提示
				// blueAdapter.enable();
			} else {
				Toast.makeText(this, "Bluetooth have started",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			// 设备不支持蓝牙,显示弹出框提醒用户
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("No bluetooth devices");
			dialog.setMessage("Your equipment does not support bluetooth, please change device");
			dialog.setNegativeButton("cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			dialog.show();
		}
	}

	private void findAvalibleDevice() {
		// 法获得已经绑定的蓝牙设备列表
		Set<BluetoothDevice> device = blueAdapter.getBondedDevices();

		if (blueAdapter != null && blueAdapter.isDiscovering()) {
			deviceList.clear();
			adapter.notifyDataSetChanged();
		}
		if (device.size() > 0) { // 存在已经配对过的蓝牙设备
			for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext();) {
				BluetoothDevice btd = it.next();
				deviceList.add(btd.getName() + '\n' + btd.getAddress());
				adapter.notifyDataSetChanged();
			}
		} else { // 不存在已经配对过的蓝牙设备
			deviceList.add("No can be matched to use bluetooth");
			adapter.notifyDataSetChanged();
		}
	}

}
