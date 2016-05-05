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
	
	public static final int UPDATE_TEXT = 1;// ����handler��
	
	// ����handlerʵ���������������豸���ص���Ϣʱ����ӡ�������˴���handler�ɵ��÷������ط�������handler����
    // ʵ�ַ�����Activity֮�����Ϣ�������ֱ���MainActivity��secondActivity�̳�
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
	 * һ��ȡ���������񣬷ֱ��Ӧ������ť��ÿ����ť��������ͬ�ķ�������ʹ��
	 * ͨ������ת�͵õ�Bluetooth����Ȼ����дonServiceConnected���������󶨷���ʱ������ͬ�ķ���
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
