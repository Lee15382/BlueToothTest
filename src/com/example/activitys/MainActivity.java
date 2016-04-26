package com.example.activitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothbroadcast.DeviceReceiver;
import com.example.bluetoothtest.R;

public class MainActivity extends Activity implements OnClickListener {

	private BluetoothAdapter blueAdapter;
	private DeviceReceiver mydevice;
	private ArrayAdapter<String> adapter;
	private List<String> deviceList = new ArrayList<String>();
	private ListView deviceListView;
	private Button start;
	private Button findBlue;
	private boolean hasregister = false;

	@Override
	protected void onStart() {
		// ע���������չ㲥
		if (!hasregister) {
			hasregister = true;
			mydevice = new DeviceReceiver(deviceList);
			IntentFilter filterStart = new IntentFilter(
					BluetoothDevice.ACTION_FOUND);
			IntentFilter filterEnd = new IntentFilter(
					BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			registerReceiver(mydevice, filterStart);
			registerReceiver(mydevice, filterEnd);
		}
		super.onStart();
	}
	//����㲥
	@Override
	protected void onDestroy() {
		if (blueAdapter != null && blueAdapter.isDiscovering()) {
			blueAdapter.cancelDiscovery();
		}
		if (hasregister) {
			hasregister = false;
			unregisterReceiver(mydevice);
		}
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		// ��listview
		deviceListView = (ListView) findViewById(R.id.devicelist);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, deviceList);
		deviceListView.setAdapter(adapter);
		// �󶨰�ťԪ��
		start = (Button) findViewById(R.id.start);
		findBlue = (Button) findViewById(R.id.findBlue);
		start.setOnClickListener(this);
		findBlue.setOnClickListener(this);
	}

	// ��ť����¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			setBluetooth();
			Toast.makeText(this, "start buletooth", Toast.LENGTH_SHORT).show();
			break;
		case R.id.findBlue:
			if(blueAdapter.isDiscovering()){
				blueAdapter.cancelDiscovery();
				findBlue.setText("Repeat Searcher");
			}else{
				findAvalibleDevice();
				blueAdapter.startDiscovery();
				findBlue.setText("Stop Searcher");
			}
			
		default:
			break;
		}
	}

	private void setBluetooth() {
		// ��ȡ����������
		blueAdapter = BluetoothAdapter.getDefaultAdapter();
		// ����Ƿ���
		if (blueAdapter != null) {
			// �������������û�п�������������
			if (!blueAdapter.isEnabled()) {
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(intent, RESULT_FIRST_USER);
				// ʹ�����豸�ɼ����������
				Intent in = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
				startActivity(in);
				// ֱ�ӿ�������������ʾ
				blueAdapter.enable();
			}
		} else {
			// �豸��֧������,��ʾ�����������û�
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("No bluetooth devices");
			dialog.setMessage("Your equipment does not support bluetooth, please change device");
			dialog.setNegativeButton("cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			dialog.show();
		}
	}

	private void findAvalibleDevice() {
		// ������Ѿ��󶨵������豸�б�
		Set<BluetoothDevice> device = blueAdapter.getBondedDevices();

		if (blueAdapter != null && blueAdapter.isDiscovering()) {
			deviceList.clear();
			adapter.notifyDataSetChanged();
		}
		if (device.size() > 0) { // �����Ѿ���Թ��������豸
			for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext();) {
				BluetoothDevice btd = it.next();
				deviceList.add(btd.getName() + '\n' + btd.getAddress());
				adapter.notifyDataSetChanged();
			}
		} else { // �������Ѿ���Թ��������豸
			deviceList.add("No can be matched to use bluetooth");
			adapter.notifyDataSetChanged();
		}
	}

}
