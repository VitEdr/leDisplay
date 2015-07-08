package vit.argon.ledisplay;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import vit.argon.ledisplay.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

public class InputConvertActivity extends Activity{
	//Fragment objects
	private EffectsFragment mEffectsFragment;
	private AttributeFragment mAttributeFragment;
	private TemplateFragment mTemplateFragment;
	//Data Object
	public DataPackage mData;
	private static byte[][] head;
	private static byte[][] body;
	private static byte[][] tail;
	//UI elements
	private ImageView attributes;
	private ImageView effects;
	private ImageView template;
	private ImageView txtediter;
	private ImageView preview;
	private ImageView send;
	
	private TextView check;
	public static EditText editText; 
	//
	private FragmentManager fragMan;
	//Helper vars and consts
	private byte[] parsed_msg;
	private static Typeface font;
	private int pos;//led display number 1-8 
	private static final int MESSAGE_PARSED = 0;
	public int BODY_LENGTH;
	private boolean touch_a = false;//for attributes
	private boolean touch_e = false;//for effects
	private boolean touch_t = false;//for template
	//BLUETOOTH vars and consts
	private static final int REQUEST_ENABLE_BT = 1;
	private static BluetoothAdapter LEDisplay = null;
	private static BluetoothSocket btSocket = null;
	private static OutputStream outStream = null;
	//private BroadcastReceiver mReceiver;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static Set<String> visibleDevices = new HashSet<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//Create fragments' instances
		mAttributeFragment = new AttributeFragment();
		mEffectsFragment = new EffectsFragment();
		mTemplateFragment = new TemplateFragment();
		mData = new DataPackage();
		pos = 3;
		//UI elements initialization
		editText = (EditText) findViewById(R.id.editText1);
		check = (TextView) findViewById(R.id.textView1);
		
		preview = (ImageView) findViewById(R.id.imageView8);
		txtediter = (ImageView) findViewById(R.id.imageView2);
				
		PreviewDrawer("");
		//Fragment Manager initialization
		fragMan = getFragmentManager();
		
		//Fragments summoning clickable ImageViews 
		attributes = (ImageView) findViewById(R.id.imageView3);
		template = (ImageView) findViewById(R.id.imageView5);
		effects = (ImageView) findViewById(R.id.imageView4);
		send = (ImageView) findViewById(R.id.imageView7);
				
		//"T" button onClickListener
		txtediter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(editText.getVisibility() == View.INVISIBLE){
					txtediter.setImageResource(R.drawable.t2);
					editText.setVisibility(View.VISIBLE);
					}
				else{
					txtediter.setImageResource(R.drawable.t1);
					editText.setVisibility(View.INVISIBLE);
				}
			}
		});
		//"Aa" button onClickListener
		attributes.setOnClickListener(new OnClickListener(){
			 
			@Override
			public void onClick(View v){
				if(fragMan.findFragmentById(R.id.framelayout1) != null){
					if (fragMan.findFragmentById(R.id.framelayout1) == mEffectsFragment)
						touch_e = fragmentRegister(mEffectsFragment, touch_e, effects, R.drawable.e1, R.drawable.e2);
					if (fragMan.findFragmentById(R.id.framelayout1) == mTemplateFragment)
						touch_t = fragmentRegister(mTemplateFragment, touch_t, template, R.drawable.s1, R.drawable.s2);
				}							
				touch_a = fragmentRegister(mAttributeFragment, touch_a, attributes, R.drawable.a1, R.drawable.a2);
								
			}
		});
				
		//"(square)" button onClickListener
		effects.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(fragMan.findFragmentById(R.id.framelayout1) != null){
					if (fragMan.findFragmentById(R.id.framelayout1) == mAttributeFragment)
						touch_a = fragmentRegister(mAttributeFragment, touch_a, attributes, R.drawable.a1, R.drawable.a2);
					if (fragMan.findFragmentById(R.id.framelayout1) == mTemplateFragment)
						touch_t = fragmentRegister(mTemplateFragment, touch_t, template, R.drawable.s1, R.drawable.s2);
				}	
				touch_e = fragmentRegister(mEffectsFragment, touch_e, effects, R.drawable.e1, R.drawable.e2);
												
			}
		});
		
		//"(pencil)" button onClickListener
		template.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(fragMan.findFragmentById(R.id.framelayout1) != null){
					if (fragMan.findFragmentById(R.id.framelayout1) == mAttributeFragment)
						touch_a = fragmentRegister(mAttributeFragment, touch_a, attributes, R.drawable.a1, R.drawable.a2);
					if (fragMan.findFragmentById(R.id.framelayout1) == mEffectsFragment)
						touch_e = fragmentRegister(mEffectsFragment, touch_e, effects, R.drawable.e1, R.drawable.e2);
				}
				touch_t = fragmentRegister(mTemplateFragment, touch_t, template, R.drawable.s1, R.drawable.s2);
				
			}
		});
		
		//FscI
		
		//Input parse activation
		editText.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event){
				
				if(event.getAction() == KeyEvent.ACTION_DOWN){
					if(keyCode == KeyEvent.KEYCODE_ENTER){
						PreviewDrawer(editText.getText().toString());
						
						//Hide keyboard
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
							//
						
						Intent startParseIntent = new Intent (InputConvertActivity.this, ParseInputActivity.class);
						startActivityForResult(startParseIntent, MESSAGE_PARSED);
						}
				}
				
				return true;
			}
		});
		
		//BLUETOUTH & BROADCASTRECIEVER
		//Set a BlueTooth Adapter
        LEDisplay = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        
        // Create List of paired BT devices
        Set<BluetoothDevice> pairedDevices = LEDisplay.getBondedDevices();
                
        for(BluetoothDevice bt:pairedDevices)
        	visibleDevices.add(bt.getName() + "\n" + bt.getAddress());
                
        send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Maintain a count of user presses
				// Display count as text on the Button
				if(editText.getText().length() == 0){
					Toast toast = Toast.makeText(getApplicationContext(), 
							"Введите сообщение!", Toast.LENGTH_SHORT); 
					toast.show(); 	
				}
				else{

					DataConstructor();
					head = mData.getHead();
					body = mData.getBody();
					tail = mData.getTail();

					String msg = "", msg_a = "", msg_b = "";
					for(int bt = 0; bt < tail[0].length; bt++)
						msg += tail[0][bt] + " ";
					for(int bt = 0; bt < tail[1].length; bt++)
						msg_a += tail[1][bt] + " ";
					for(int bt = 0; bt < tail[2].length; bt++)
						msg_b += tail[2][bt] + " ";
					check.setText("tail0 " + msg + "\n" + "tail1 " + msg_a + "\n" + "tail2 " + msg_b);

					startActivity(new Intent(InputConvertActivity.this, DeviceSelectActivity.class));
				}
								
			}
		});
		///////////////////////////////*/
	}
			
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(requestCode == MESSAGE_PARSED && resultCode == RESULT_OK){
    		BODY_LENGTH = editText.getText().length()*12 + 4*ParseInputActivity.N_LINES;
    		parsed_msg = new byte[36*ParseInputActivity.N_LINES];
    		parsed_msg = data.getByteArrayExtra(ParseInputActivity.PARSED);
    		mData.setBody(parsed_msg, BODY_LENGTH);
    		/*String msg = "", msg_b = "";
    		for(int bt = 0; bt < parsed_msg.length; bt++)
    			msg += parsed_msg[bt] + " ";
    		for(int bt = 0; bt < BODY_LENGTH; bt++)
    			msg_b += mData.getBody()[0][bt] + " ";
    		check.setText("Made " + msg + "\n" + "Body " + msg_b);*/
    	}
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		//unregisterReceiver(mReceiver);
	}
			
	public void DataConstructor(){
		if(mEffectsFragment != null){
			mData.setEffect(pos, mEffectsFragment.getEffect());
			mData.setBrightness(mEffectsFragment.getBright());
			mData.setSpeed(pos, mEffectsFragment.getSpeed());
		}
		mData.setLength(pos, editText.length());
		//mData.setFontSize(mAttributeFragment.getTextSize());
		//mData.setBody(parsed_msg, BODY_LENGTH);
	}
		
	public boolean fragmentRegister(Fragment fragment, boolean touch, ImageView imageView, int id1, int id2){
		if(!touch){
			FragmentTransaction ft = fragMan.beginTransaction();
			ft.replace(R.id.framelayout1, fragment);
			ft.commit();
			imageView.setImageResource(id2);
			touch = true;
		}
		else{
			FragmentTransaction ft = fragMan.beginTransaction();
			ft.remove(fragment);
			ft.commit();
			imageView.setImageResource(id1);
			touch = false;
		}
		
		getFragmentManager().executePendingTransactions();
		return touch;
	}
	
	public void PreviewDrawer(String text){
					
		//Draw input text on imageView1
						
		final int WIDTH = getResources().getDisplayMetrics().widthPixels*9/10;
		final int HEIGHT = WIDTH/12;
		final Bitmap image = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
		
		final Canvas mainvas = new Canvas(image);
		final Paint paint = new Paint();
		
		paint.setColor(Color.DKGRAY);
		
		/*if (font != null){
			paint.setTypeface(font);
		}*/
		
		//Create a matrix of rectangles
		int Y_SIZE = 12;
		final float RECT_SIZE = (float) (WIDTH/290.0);
		int X_SIZE = 145;//WIDTH/(2*RECT_SIZE);
		RectF[][] GrayRect = new RectF[X_SIZE][Y_SIZE];
		
		final Bitmap textImage = Bitmap.createBitmap(X_SIZE, Y_SIZE, Bitmap.Config.ARGB_8888);
		final Canvas textvas = new Canvas(textImage);
		textvas.drawColor(Color.GREEN);
		textvas.drawText(text, 0, 10, paint);
		
		//
		//Initialize canvas and paint
		
		mainvas.drawColor(Color.BLACK);
		//mainvas.drawText(input.getText().toString(), 0, 10, paint);
		for(int x = 0; x < X_SIZE; x++){
			for(int y = 0; y < Y_SIZE; y++){
				GrayRect[x][y] = new RectF(RECT_SIZE*(2*x + 1), RECT_SIZE*(2*y + 1), 2*RECT_SIZE*(x + 1), 2*RECT_SIZE*(y + 1));
				if(textImage.getPixel(x, y) == Color.DKGRAY){
					paint.setColor(Color.RED);
					mainvas.drawRect(GrayRect[x][y], paint);
				}
				else{
					paint.setColor(Color.DKGRAY);
					mainvas.drawRect(GrayRect[x][y], paint);
				}
			}
		}
		
		preview.setImageResource(0);
		preview.setImageBitmap(image);
	}
	
	//BLUETOOTH Methods
	private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(LEDisplay==null) { 
        } else {
          if (!LEDisplay.isEnabled()) {
            //Prompt user to turn on Bluetooth
            Intent enableBtIntent = new Intent(LEDisplay.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
          }
        }
      }
            
      private static void sendData(byte[][] m) {
    	  
    	  // send whole Data Massage iteratively
    	  for (int msg_num = 0; msg_num < m.length; msg_num++){
    		for (int i = 0; i < m[msg_num].length; i++){
    			try {
    				outStream.write(m[msg_num][i]);
    			} catch (IOException e) {
    				String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
    				       
    			}
    		}
    	}
      }
      
      public static void connectDevice(String address){
          // Set up a pointer to the remote node using it's address.
          BluetoothDevice device = LEDisplay.getRemoteDevice(address);
          
          // Two things are needed to make a connection:
          //   A MAC address, which we got above.
          //   A Service ID or UUID.  In this case we are using the
          //     UUID for SPP.
          try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
          } catch (IOException e) {
            //errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
          }
          
          // Discovery is resource intensive.  Make sure it isn't going on
          // when you attempt to connect and pass your message.
          LEDisplay.cancelDiscovery();
          
          // Establish the connection.  This will block until it connects.
          try {
            btSocket.connect();
          } catch (IOException e) {
            try {
              btSocket.close();
            } catch (IOException e2) {
              //errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
          }
            
          // Create a data stream so we can talk to server.
          //Log.d(TAG, "...Создание Socket...");
        
          try {
            outStream = btSocket.getOutputStream();
          } catch (IOException e) {
            //errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
          }
      
                    
          sendData(head);
          sendData(body);
          sendData(tail);

          try {
        	  Thread.sleep(500);
          } catch (InterruptedException e) {
        	  e.printStackTrace();
          }
          
          try {
              btSocket.close();
          } catch (IOException e) { }
      }
}
