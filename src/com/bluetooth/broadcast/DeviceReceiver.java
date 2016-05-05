package com.bluetooth.broadcast;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceReceiver extends BroadcastReceiver {

	private List<String> deviceList;
	public DeviceReceiver(List<String> deviceList){
		this.deviceList = deviceList;
	}
	/**
	 * @author lee
	 * 重写onReceive
	 * 查看接收到的广播是否为蓝牙相关，此处为扫描发现蓝牙，接受系统发送的相关广播
	 * 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();//获得系统发送的广播信息
		
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice btd = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			//获得绑定状态，获取相关返回数据，BOND_BONDED=12表示已经绑定
			if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
				deviceList.add(btd.getName() + '\n' + btd.getAddress());
			}
		}
	}
	public List<String> getDeviceList(){
		return deviceList;
	}

}
