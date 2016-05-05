/**
 * 
 */
package com.bluetooth.activitys;

import com.bluetooth.service.BluetoothService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * @author Lee
 *
 */
public class BaseActivity  extends Activity{
	
	public static final int UPDATE_TEXT = 1;// 用于handler中
	
	// 定义handler实例，当传回蓝牙设备返回的信息时，打印出来。此处的handler由调用服务的相关方法返回handler对象，
    // 实现服务于Activity之间的信息交流、分别由MainActivity和secondActivity继承
	protected Handler handler = new Handler(){
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
	
	protected BluetoothService.Bluetooth bluetooth;
	/**
	 * 一下取得三个服务，分别对应三个按钮，每个按钮开启服务不同的方法进行使用
	 * 通过向下转型得到Bluetooth对象，然后重写onServiceConnected方法，当绑定服务时启动不同的方法
	 */
	protected ServiceConnection conn1 = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bluetooth = (BluetoothService.Bluetooth) service;
		}
	};

}
