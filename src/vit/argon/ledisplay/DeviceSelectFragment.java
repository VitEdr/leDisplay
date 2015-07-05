package vit.argon.ledisplay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class DeviceSelectFragment extends DialogFragment {
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
						
		String[] devicesList = new String[InputConvertActivity.visibleDevices.size()];
		devicesList = InputConvertActivity.visibleDevices.toArray(devicesList);
		final String[] devicesArray = devicesList;		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose device").setItems(devicesArray, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				InputConvertActivity.connectDevice(devicesArray[which].substring(devicesArray[which].length() - 17));
			}
		});
		return builder.create();
	}
	/*@Override
	public void onResume(){
		super.onResume();
		
		final String[] devicesArray = devicesList;
		builder.setTitle("Choose device").setItems(devicesArray, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				ButtonActivity.connectDevice(devicesArray[which]);
			}
		});
	}*/
}
