package com.example.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.example.bluetoothbroadcast.DeviceReceiver;

public class BluetoothService extends Service {
	public BluetoothAdapter blueAdapter;
	private  Bluetooth mBinder = new Bluetooth();
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		return startId;}
	
//	public void onStartCommand() {
//		// 注册蓝牙接收广播
//		if (!hasregister) {
//			hasregister = true;
//			mydevice = new DeviceReceiver(deviceList);
//			IntentFilter filterStart = new IntentFilter(
//					BluetoothDevice.ACTION_FOUND);
//			IntentFilter filterEnd = new IntentFilter(
//					BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//			registerReceiver(mydevice, filterStart);
//			registerReceiver(mydevice, filterEnd);
//		}
//		super.onStart();
//	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		blueAdapter.disable();
	}
	@Override
	public void onCreate(){
		super.onCreate();
		System.out.println("ss");
	}

	//用于开启蓝牙，搜索设备，显示信息
	public class Bluetooth extends Binder{
		
		
		public Bluetooth(){
		}
		
		public void setBluetooth() {
			// 获取蓝牙适配器
			blueAdapter = BluetoothAdapter.getDefaultAdapter();
			// 检查是否获得
			if (blueAdapter != null) {
				// 获得蓝牙，蓝牙没有开启，则请求开启
				if (!blueAdapter.isEnabled()) {
					Intent intent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					// 使蓝牙设备可见，方便配对
					Intent in = new Intent(
							BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
					// 直接开启，不经过提示
//					blueAdapter.enable();
				}else{
//					blueAdapter.disable();
				}
				
			} else {
				// 设备不支持蓝牙,显示弹出框提醒用户
//				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//				dialog.setTitle("No bluetooth devices");
//				dialog.setMessage("Your equipment does not support bluetooth, please change device");
//				dialog.setNegativeButton("cancel",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//
//							}
//						});
//				dialog.show();
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return  mBinder;
	}

}
