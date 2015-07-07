package vit.argon.ledisplay;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class DeviceSelectActivity extends ListActivity{
	
	private ArrayAdapter<String> mAdapter;
	private BroadcastReceiver mReceiver;
	private BluetoothAdapter btAdapter;
	private Set<String> vDev;
	String[] devicesList;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		vDev = InputConvertActivity.visibleDevices;
		setArrayAdapter(vDev);
		// Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    vDev.add(device.getName() + "\n" + device.getAddress());
                    setArrayAdapter(vDev);
                    
                }
                
            }
        };
        
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		InputConvertActivity.connectDevice(devicesList[position].substring(devicesList[position].length() - 17));
		finish();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		btAdapter.startDiscovery();
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	private void setArrayAdapter(Set<String> set){
		devicesList = new String[set.size()];
		devicesList = set.toArray(devicesList);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devicesList);
		setListAdapter(mAdapter);
	}
		
}
