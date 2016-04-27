package com.example.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BlueToothConnectThread extends Thread {

	public BluetoothSocket socket;
	private BluetoothDevice device;
	private BluetoothAdapter mBluetoothAdapter;
	//客户端UUID
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	//传递进来的device是要连接的设备，由调用的类提供地址
	public BlueToothConnectThread(BluetoothDevice mdevice,BluetoothAdapter adapter) {
		this.device = mdevice;
		this.mBluetoothAdapter = adapter;
		try {
			//构建BluetoothSocket对象，要通过UUID来构建
			socket = mdevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	//run方法启动连接
	public void run(){
		mBluetoothAdapter.cancelDiscovery();//取消设备查找
		//socket通信
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
	//关闭连接
    public void cancel() {  
        try {  
            socket.close();  
        } catch (IOException e) {  
            Log.e("app", "close() of connect  socket failed", e);  
        }  
    }  
}
