package vit.argon.ledisplay;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ParseInputActivity extends Activity{
	public final static String PARSED = "course.examples.ui.button.PARSED";
	public static int TEXT_LENGTH;										//# of symbols
	private static int TEXT_WIDTH;						//in pixels
	public static final int TEXT_HEIGHT = 12;							//in pixels
	private static final int LINE_LENGTH = 36;							//# of bytes in massage
	public static int N_LINES;		//# of lines in a massage
	public byte msg_seq = 0x04;											//massage sequence number
	public byte line_num = 0x00;
	byte[][] msg4;
	public String pix = "";
	public Bitmap image;
	
			
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			TEXT_LENGTH = InputConvertActivity.editText.length();
			N_LINES = RoundLines(TEXT_LENGTH*TEXT_HEIGHT);
			TEXT_WIDTH = TEXT_LENGTH*8;
			image = Bitmap.createBitmap(TEXT_WIDTH, TEXT_HEIGHT, Bitmap.Config.ARGB_8888);
			
			setContentView(new AdTextView(this));
			
			msg4 = new byte[N_LINES][LINE_LENGTH];
			
			ParseInput(image);
			/*rearrange parsed massage into 1D array*/
			byte[] msg_back = new byte[0];
			for(int line = 0; line < N_LINES; line++)
				msg_back = concatArray(msg_back, msg4[line]);
			
			/*Send back result*/
			Intent answerIntent = new Intent();
			answerIntent.putExtra(PARSED, msg_back);
			setResult(RESULT_OK, answerIntent);
			finish();
			
		}
		
		public void ParseInput(Bitmap image){
	    	
			int i = 0;//msg massive ...
			int j = 3;//... indexes
			msg4[i][0] = msg_seq;//write 1st line message attribute 
			msg4[i][2] = line_num;//write 1st line number
			
			for(int y = 1; y <= TEXT_HEIGHT; y++){
				for(int x = 1; x <= TEXT_WIDTH; x++){
					/*parse ONE pixel*/
					if (image.getPixel(x-1,y-1) == Color.GREEN)
						pix += "1";
					else
						pix += "0";
					/**/
					
					/*parse ONE byte*/
					if(x % 8 == 0){//charwise recording of message 
						msg4[i][j] = (byte) Integer.parseInt(pix, 2);
						j++;//goto next byte in msg massive
						pix = "";
					}
					/**/
					
					/*Goto next line*/
					if((j == LINE_LENGTH - 1) || ((y == TEXT_HEIGHT) && (x == TEXT_WIDTH))){
						msg4[i][1] = (byte) (j-2);//measure and write line length
						for(int indx = 0; indx < j; indx++)
							msg4[i][j] += msg4[i][indx];// count control sum
						if(i == N_LINES - 1) break;
						i++;//goto next line
						line_num += 1;//next line number
						msg4[i][0] = msg_seq;//write new line message attribute 
						msg4[i][2] = line_num;//write new line number
						j = 3;//start body part of line
					}
					/**/
				}
				
			}	
	    }
		
		/*Method to concatenate two arrays*/
		private static byte[] concatArray(byte[]a, byte[] b){
			if (a == null)
				return b;
			if (b == null)
				return a;
			byte[] r = new byte[a.length + b.length];
			System.arraycopy(a, 0, r, 0, a.length);
			System.arraycopy(b, 0, r, a.length, b.length);
			return r;
		}
		
		private static int RoundLines(int a){
			int b = a/32;
			
			if (a%32 > 0) b += 1;
			
			return b;
		}
		
class AdTextView extends View{
	
	public Canvas mainvas = new Canvas(image);
	public Paint paint = new Paint();
	
		public AdTextView(Context context){
			super(context);
			
			mainvas.drawColor(Color.GREEN);
			mainvas.drawText(InputConvertActivity.editText.getText().toString(), 0, 10, paint);
			
		}
		@Override
		protected void onDraw(Canvas canvas) {
			
			paint.setColor(Color.WHITE);
			paint.setTextSize(12);
			/*paint.setAntiAlias(true);
			canvas.drawColor(Color.CYAN);*/
			pix = "# of lines = ";//msg4[0].length + " " + msg4[1].length + " " + msg4[2].length;
			canvas.drawText(InputConvertActivity.editText.getText().toString(), 0, 20, paint);
			canvas.drawText(pix + N_LINES, 0, 200, paint);
			canvas.drawBitmap(image, 40, 80, null);
		}
}
}
