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
//		// ע���������չ㲥
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

	//���ڿ��������������豸����ʾ��Ϣ
	public class Bluetooth extends Binder{
		
		
		public Bluetooth(){
		}
		
		public void setBluetooth() {
			// ��ȡ����������
			blueAdapter = BluetoothAdapter.getDefaultAdapter();
			// ����Ƿ���
			if (blueAdapter != null) {
				// �������������û�п�������������
				if (!blueAdapter.isEnabled()) {
					Intent intent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					// ʹ�����豸�ɼ����������
					Intent in = new Intent(
							BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
					// ֱ�ӿ�������������ʾ
//					blueAdapter.enable();
				}else{
//					blueAdapter.disable();
				}
				
			} else {
				// �豸��֧������,��ʾ�����������û�
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
