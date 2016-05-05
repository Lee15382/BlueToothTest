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
	 * ��дonReceive
	 * �鿴���յ��Ĺ㲥�Ƿ�Ϊ������أ��˴�Ϊɨ�跢������������ϵͳ���͵���ع㲥
	 * 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();//���ϵͳ���͵Ĺ㲥��Ϣ
		
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice btd = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			//��ð�״̬����ȡ��ط������ݣ�BOND_BONDED=12��ʾ�Ѿ���
			if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
				deviceList.add(btd.getName() + '\n' + btd.getAddress());
			}
		}
	}
	public List<String> getDeviceList(){
		return deviceList;
	}

}
