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
	//�ͻ���UUID
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	//���ݽ�����device��Ҫ���ӵ��豸���ɵ��õ����ṩ��ַ
	public BlueToothConnectThread(BluetoothDevice mdevice,BluetoothAdapter adapter) {
		this.device = mdevice;
		this.mBluetoothAdapter = adapter;
		try {
			//����BluetoothSocket����Ҫͨ��UUID������
			socket = mdevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	//run������������
	public void run(){
		mBluetoothAdapter.cancelDiscovery();//ȡ���豸����
		//socketͨ��
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
	//�ر�����
    public void cancel() {  
        try {  
            socket.close();  
        } catch (IOException e) {  
            Log.e("app", "close() of connect  socket failed", e);  
        }  
    }  
}
