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
//		// ��ȡ����������
//		blueAdapter = BluetoothAdapter.getDefaultAdapter();
//		// ����Ƿ���
//		if (blueAdapter != null) {
//			// �������������û�п�������������
//			if (!blueAdapter.isEnabled()) {
//				 Intent intent = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				startActivityForResult(intent,RESULT_FIRST_USER);
//				// ʹ�����豸�ɼ����������
//				Intent in = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//				in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
//				startActivity(in);
//				// ֱ�ӿ�������������ʾ
////				blueAdapter.enable();
//			}else{
////				Toast.makeText(this, "Bluetooth have started", Toast.LENGTH_SHORT).show();
//			}
//			
//		} else {
//			// �豸��֧������,��ʾ�����������û�
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
