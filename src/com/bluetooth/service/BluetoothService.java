package com.bluetooth.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.bluetooth.activitys.MainActivity;
import com.bluetooth.broadcast.DeviceReceiver;

public class BluetoothService extends Service {
	public BluetoothAdapter blueAdapter;
	private Bluetooth mBinder = new Bluetooth();
	private boolean hasregister = false;
	private DeviceReceiver mydevice;
	private List<String> deviceList = new ArrayList<String>();
	private Message msg;
//	private Handler mhandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				Intent intent = new Intent("com.bluetooth.broadcast.BLUEINFO");
//				sendBroadcast(intent);
//				break;
//			default:
//				break;
//			}
//		}
//	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return startId;

	}

	@Override
	public void onDestroy() {
		blueAdapter.disable();
		super.onDestroy();
		// stopSelf();
	}

	// 用于开启蓝牙，搜索设备，显示信息
	public class Bluetooth extends Binder implements Serializable {

		public Bluetooth() {
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
					// Intent in = new Intent(
					// BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					// in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
					// 200);
					// in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// startActivity(in);
					// 直接开启，不经过提示
					// blueAdapter.enable();
				}
			}
		}

		public List<String> findAvalibleDevice(BluetoothAdapter blueAdapter) {
			Set<BluetoothDevice> device = blueAdapter.getBondedDevices();
			if (blueAdapter != null && blueAdapter.isDiscovering()) {
				deviceList.clear();
			}
			if (device.size() > 0) {
				for (Iterator<BluetoothDevice> it = device.iterator(); it
						.hasNext();) {
					BluetoothDevice btd = it.next();
					deviceList.add(btd.getName() + '\n' + btd.getAddress());
				}
			} else {
				deviceList.add("No can be matched to use bluetooth");
			}
			return deviceList;
		}

		public void blueIOSMethod(BluetoothService.Bluetooth bluetooth,
				BluetoothDevice mdevice, BluetoothAdapter adapter,
				Handler handler, MainActivity context) {

			BlueToothConnectThread connect = null;
			connect = new BlueToothConnectThread(mdevice, adapter);
			connect.start();
			BlueToothIOStream blueToothIOStream = new BlueToothIOStream(
					connect.socket, handler, context);
			blueToothIOStream.start();

			System.out.println("kk0");
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

}

/**
 * 
 * @author Lee 下面两个线程类，用于使蓝牙进行连接并接受输入流的时候使用
 * 
 */

class BlueToothConnectThread extends Thread {

	public BluetoothSocket socket;
	private BluetoothDevice device;
	private BluetoothAdapter mBluetoothAdapter;
	// 客户端UUID
	private final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// 传递进来的device是要连接的设备，由调用的类提供地址
	public BlueToothConnectThread(BluetoothDevice mdevice,
			BluetoothAdapter adapter) {
		this.device = mdevice;
		this.mBluetoothAdapter = adapter;
		try {
			// 构建BluetoothSocket对象，要通过UUID来构建
			socket = mdevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// run方法启动连接
	public void run() {
		mBluetoothAdapter.cancelDiscovery();// 取消设备查找
		// socket通信
		try {
			socket.connect();
		} catch (Exception e1) {

			try {
				socket.close();
			} catch (Exception e2) {

				e2.printStackTrace();
			}
			return;
		}
	}

	// 关闭连接
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			Log.e("app", "close() of connect  socket failed", e);
		}
	}
}

class BlueToothIOStream extends Thread {
	private InputStream inStream;
	private OutputStream outStream;
	private BluetoothSocket socket;
	private Handler handler;
	private Handler mhandler;
	private MainActivity context;

	// 构造器
	public BlueToothIOStream(BluetoothSocket socket, Handler handler, MainActivity context) {
		this.socket = socket;
		this.handler = handler;
		this.mhandler = mhandler;
		this.context = context;
		
	}

	public void run() {
		byte[] buff = new byte[1024];
		int len = 0;
		// 通过传递进来的socket获取输入输出流
		try {
			inStream = socket.getInputStream();
			outStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 读数据需不断监听，写不需要
		while (true) {
			try {
				// read方法获得的是整数，返回读取的字节数
				if ((len = inStream.read(buff)) > 0) {
					byte[] buf_data = new byte[len];
					for (int i = 0; i < len; i++) {
						buf_data[i] = buff[i];
					}
					String s = new String(buf_data);
					Message msg = new Message();
					msg.obj = s;
					msg.what = 1;
					handler.sendMessage(msg);
					Intent intent = new Intent("com.bluetooth.broadcast.BLUEINFO");
					intent.putExtra("info", s);
					context.sendBroadcast(intent);
					// context.send

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void write(byte[] buffer) {
		try {
			outStream.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cancelOutIn() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
