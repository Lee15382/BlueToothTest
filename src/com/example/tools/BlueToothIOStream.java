package com.example.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BlueToothIOStream extends Thread {
	private InputStream inStream;
	private OutputStream outStream;
	private BluetoothSocket socket;
	private Handler handler;

	// 构造器
	public BlueToothIOStream(BluetoothSocket socket, Handler handler) {
		this.socket = socket;
		this.handler = handler;
	}

	public void run() {
		byte[] buff = new byte[1024];
		int len = 0;
		//通过传递进来的socket获取输入输出流
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
