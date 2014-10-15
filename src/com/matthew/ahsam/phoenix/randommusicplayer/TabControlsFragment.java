package com.matthew.ahsam.phoenix.randommusicplayer;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matthew.ahsam.phoenix.randommusicplayer.R;


public class TabControlsFragment extends Fragment {
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_controls,parent,false);
		
		
		
		return v;
	}
}
