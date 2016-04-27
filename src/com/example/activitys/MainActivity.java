package com.example.activitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothbroadcast.DeviceReceiver;
import com.example.bluetoothtest.R;
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
	
	//界面元素
	private ListView deviceListView;
	private Button start;
	private Button findBlue;
	private Button communication;
	
	public static final int UPDATE_TEXT = 1;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case UPDATE_TEXT:
				System.out.println(msg.obj.toString());
				break;
			default:
				break;
			}
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
	//解除广播
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
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
		start.setOnClickListener(this);
		findBlue.setOnClickListener(this);
		communication.setOnClickListener(this);
		System.out.println("nihao");
	}

	// 按钮点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			setBluetooth();
			break;
		case R.id.findBlue:
			if(blueAdapter.isDiscovering()){
				blueAdapter.cancelDiscovery();
				findBlue.setText("Repeat Searcher");
			}else{
				findAvalibleDevice();
				blueAdapter.startDiscovery();
				findBlue.setText("Stop Searcher");
			}
			break;
		case R.id.communication:
			//device是要连接的设备，通过获得地址address（00:80:25:4A:1C:79）来构建
			device = blueAdapter.getRemoteDevice(address);   
			connect = new BlueToothConnectThread(device, blueAdapter);
			connect.start();
			blueToothIOStream = new BlueToothIOStream(connect.socket, handler);
			blueToothIOStream.start();
			break;
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
				startActivityForResult(intent, RESULT_FIRST_USER);
				// 使蓝牙设备可见，方便配对
				Intent in = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
				startActivity(in);
				// 直接开启，不经过提示
//				blueAdapter.enable();
			}else{
				Toast.makeText(this, "Bluetooth have started", Toast.LENGTH_SHORT).show();
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
