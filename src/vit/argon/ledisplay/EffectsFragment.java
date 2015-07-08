package vit.argon.ledisplay;

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
	
	//save variables
	private String EFFECT_KEY = "effect";
	private String BRIGHT_KEY = "bright";
	private String SPEED_KEY = "speed";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		//Restore saved parameters
		if(savedInstanceState != null){
			effect = savedInstanceState.getInt(EFFECT_KEY);
			brightness = savedInstanceState.getInt(BRIGHT_KEY);
			speedness = savedInstanceState.getInt(SPEED_KEY);
		}
        return inflater.inflate(R.layout.effects_menu, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		effects = (Spinner) getActivity().findViewById(R.id.spinner2);
		if(effect != 0)
			effects.setSelection(effect);
		//Init pickers
		bright = (TextView) getActivity().findViewById(R.id.textView7);
		speed = (TextView) getActivity().findViewById(R.id.textView8);
		//Setup br picker
		brPicker = (NumberPicker) getActivity().findViewById(R.id.numberPicker2);
		brPicker.setMaxValue(5);
		brPicker.setMinValue(1);
		if(brightness != 0){
			brPicker.setValue(brightness);
			setBright(brightness);
			}
		else
			setBright(1);
		//Setup sp picker
		spPicker = (NumberPicker) getActivity().findViewById(R.id.numberPicker3);
		spPicker.setMaxValue(7);
		spPicker.setMinValue(1);
		if(speedness != 0){
			spPicker.setValue(speedness);
			setSpeed(speedness);
			}
		else
			setSpeed(1);
		
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
				brPicker.setValue(newVal);
			}
		});
		
		spPicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				setSpeed(newVal);
				spPicker.setValue(newVal);
			}
		});
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt(EFFECT_KEY, getEffect());
		savedInstanceState.putInt(BRIGHT_KEY, getBright());
		savedInstanceState.putInt(SPEED_KEY, getSpeed());		
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
