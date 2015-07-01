package vit.argon.ledisplay;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class EffectsFragment extends Fragment{
	private TextView textView1;
	private TextView textView2;
	private Spinner spinner;
	private NumberPicker numPicker1;
	private NumberPicker numPicker2;
		
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.effects_menu, container, false);
	}

}
