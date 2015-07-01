package vit.argon.ledisplay;

import vit.argon.ledisplay.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class InputConvertActivity extends Activity{
	
	private EffectsFragment mEffectsFragment;
	private AttributeFragment mAttributeFragment;
	private TemplateFragment mTemplateFragment;
	private ImageView attributes;
	private ImageView effects;
	private ImageView template;
	private TextView check;
	private FragmentManager fragMan;
	
	private ImageView preview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		check = (TextView) findViewById(R.id.textView1);		
		preview = (ImageView) findViewById(R.id.imageView8);
				
		PreviewDrawer("");
		
		fragMan = getFragmentManager();
		
		//Fragments summoning clickable ImageViews 
		attributes = (ImageView) findViewById(R.id.imageView3);
		effects = (ImageView) findViewById(R.id.imageView5);
		template = (ImageView) findViewById(R.id.imageView4);
		
		attributes.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mAttributeFragment = new AttributeFragment();
				attributes.setPressed(true);
				//attributes.setEnabled(false);
				fragmentRegister(mAttributeFragment);
				
			}
		});
		
		effects.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				mEffectsFragment = new EffectsFragment();
				fragmentRegister(mEffectsFragment);
				
			}
		});
		
		template.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mTemplateFragment = new TemplateFragment();
				fragmentRegister(mTemplateFragment);
				
			}
		});
		//FscI
	}
	
	public void fragmentRegister(Fragment fragment){
		FragmentTransaction fragTrans = fragMan.beginTransaction();
		fragTrans.replace(R.id.framelayout1, fragment);
		fragMan.popBackStack();
		fragTrans.addToBackStack(null);
		fragTrans.commit();
		
		getFragmentManager().executePendingTransactions();
	}
	
	public void PreviewDrawer(String text){
		//Draw input text on imageView1
		
		final int WIDTH = getResources().getDisplayMetrics().widthPixels*9/10;
		final int HEIGHT = WIDTH/12;
		check.setText("Width= " + WIDTH + " Height= " + HEIGHT);
		final Bitmap image = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
		
		final Canvas mainvas = new Canvas(image);
		final Paint paint = new Paint();
		
		paint.setColor(Color.DKGRAY);
										
		//Create a matrix of rectangles
		int Y_SIZE = 12;
		final float RECT_SIZE = (float) (WIDTH/290.0);
		int X_SIZE = 144;//WIDTH/(2*RECT_SIZE);
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
}
