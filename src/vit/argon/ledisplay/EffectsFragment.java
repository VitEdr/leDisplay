package vit.argon.ledisplay;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class EffectsFragment extends Fragment{
	
	private Spinner effects;
	
	private TextView bright;
	private TextView speed;
	
	private NumberPicker brPicker;
	private NumberPicker spPicker;
	
	private int brightness;
	private int speedness;
	private int effect;
		
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.effects_menu, container, false);
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		effects = (Spinner) getActivity().findViewById(R.id.spinner2);
		
		bright = (TextView) getActivity().findViewById(R.id.textView7);
		speed = (TextView) getActivity().findViewById(R.id.textView8);
		
		brPicker = (NumberPicker) getActivity().findViewById(R.id.numberPicker2);
		spPicker = (NumberPicker) getActivity().findViewById(R.id.numberPicker3);
		brPicker.setMaxValue(5);
		brPicker.setMinValue(1);
		spPicker.setMaxValue(8);
		spPicker.setMinValue(1);
		
		effects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
				
				//TODO - write code to set up chosen effect		
				setEffect(pos);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		brPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				setBright(newVal);
			}
		});
		
		spPicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				setSpeed(newVal);
			}
		});
		
	}
	
	public void setSpeed(int speed){
		speedness = speed;
	}
	
	public void setBright(int bright){
		brightness = bright;
	}
	
	public void setEffect(int pos){
		effect = pos;
	}
	
	public int getSpeed(){
		return speedness;
	}
	
	public int getBright(){
		return brightness;
	}
	
	public int getEffect(){
		return effect;
	}

}
