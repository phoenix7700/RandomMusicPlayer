package com.matthew.ahsam.phoenix.randommusicplayer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

public class NumberPickerFragment extends DialogFragment {
	
	private NumberPicker mNumberPickerRandomSongNumber;
	String [] mNumberList = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}; 
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.number_picker_dialog,parent,false);
		
		mNumberPickerRandomSongNumber = (NumberPicker) v.findViewById(R.id.numberOfRandomSongsPicker);
		mNumberPickerRandomSongNumber.setDisplayedValues (mNumberList);
		mNumberPickerRandomSongNumber.setMaxValue(20);
		mNumberPickerRandomSongNumber.setMinValue(0);
		mNumberPickerRandomSongNumber.setWrapSelectorWheel(false);
		mNumberPickerRandomSongNumber.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				Log.e("INPICKER", "Inside Picker");
			}
		});
		
		Button mButtonPickerOk = (Button) v.findViewById(R.id.buttonNumberPickerOk);
		mButtonPickerOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Store value on number picker into the correct group
				MainActivity ma = (MainActivity) v.getContext();
				ma.getSongList().get(ma.getPreviousSelectedGroupSongList()).setMaxRandomSongs(mNumberPickerRandomSongNumber.getValue());
				getDialog().dismiss();
				Log.e("INButton", "Inside Button");
			}
		});
		
		
		return v;
	}
}
