//package com.example.tools;
//
//import com.example.activitys.MainActivity;
//
//import android.app.AlertDialog;
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.IBinder;
//import android.widget.Toast;
//
//public class Bluetooth extends Service {
//	
////private static final int RESULT_FIRST_USER = 1;
//	//	private static final String RESULT_FIRST_USER = 1;
//	private BluetoothAdapter blueAdapter;
////	private Context context;
////	private MainActivity mainActivity;
//	
//	public Bluetooth(MainActivity mainActivity, BluetoothAdapter blueAdapter){
//		this.blueAdapter = blueAdapter;
//	}
//	
//	private void setBluetooth() {
//		
//		
//		// 获取蓝牙适配器
//		blueAdapter = BluetoothAdapter.getDefaultAdapter();
//		// 检查是否获得
//		if (blueAdapter != null) {
//			// 获得蓝牙，蓝牙没有开启，则请求开启
//			if (!blueAdapter.isEnabled()) {
//				 Intent intent = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				startActivityForResult(intent,RESULT_FIRST_USER);
//				// 使蓝牙设备可见，方便配对
//				Intent in = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//				in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
//				startActivity(in);
//				// 直接开启，不经过提示
////				blueAdapter.enable();
//			}else{
////				Toast.makeText(this, "Bluetooth have started", Toast.LENGTH_SHORT).show();
//			}
//			
//		} else {
//			// 设备不支持蓝牙,显示弹出框提醒用户
//			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//			dialog.setTitle("No bluetooth devices");
//			dialog.setMessage("Your equipment does not support bluetooth, please change device");
//			dialog.setNegativeButton("cancel",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//
//						}
//					});
//			dialog.show();
//		}
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
