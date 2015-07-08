package vit.argon.ledisplay;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;

public class AttributeFragment extends Fragment{
	
	private Spinner textFont;
	private NumberPicker textSize;
	private TextView bold;
	private TextView italic;
	public Typeface font;
	
	private int txtSize;
	private int txtFont;
	private String TEXT_SIZE_KEY = "textsize";
	private String TEXT_FONT_KEY = "textfont";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		if(savedInstanceState != null){
			txtSize = savedInstanceState.getInt(TEXT_SIZE_KEY);
			txtFont = savedInstanceState.getInt(TEXT_FONT_KEY);
		}
		
		return inflater.inflate(R.layout.attribute_menu, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		textFont = (Spinner) getActivity().findViewById(R.id.spinner1);
		if(txtFont != 0)
			textFont.setSelection(txtFont);
		
		textSize = (NumberPicker) getActivity().findViewById(R.id.numberPicker1);
		textSize.setMaxValue(16);
		textSize.setMinValue(8);
		if(txtSize != 0)
			textSize.setValue(txtSize);
		else
			textSize.setValue(12);
		//String array [] = {"8","12","16"};  
		//textSize.setDisplayedValues(array);
		
		bold = (TextView) getActivity().findViewById(R.id.textView1);
		italic = (TextView) getActivity().findViewById(R.id.textView2);
		
		textFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
				
				//TODO - write code to set up chosen font
				//setFont(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/" + parent.getItemAtPosition(pos) + ".ttf"));
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		textSize.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				setTextSize(newVal);
			}
		});
		
		bold.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO - setup font change on textview click
			}
		});
		
		italic.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO - setup font change on textview click
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt(TEXT_SIZE_KEY, getTextSize());
		savedInstanceState.putInt(TEXT_FONT_KEY, textFont.getSelectedItemPosition());
	}
	
	public void setFont(Typeface newfont){
		font = newfont;
	}
	
	public void setTextSize(int size){
		txtSize = size;
	}
	
	public Typeface getFont(){
		return font;
	}
	
	public int getTextSize(){
		return txtSize;
	}

}
