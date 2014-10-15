package com.matthew.ahsam.phoenix.randommusicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TabSongListFragment extends Fragment {
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_songlist,parent,false);
		
		Toast t =Toast.makeText(v.getContext(), "Long click and drag songs to the list", Toast.LENGTH_LONG);
		t.show();
		
		return v;
	}
}
