package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InputListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList <SongListChild> list;
	
	InputListAdapter (Context c, ArrayList <SongListChild> l) {
		context = c;
		list = l;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		SongListChild child = (SongListChild) getItem(position);
		if(view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.songlist_child, null);
		}
		if (!child.isSelected()) {
			view.setBackgroundColor(view.getResources().getColor(android.R.color.background_light));
		} else {
			view.setBackgroundColor(view.getResources().getColor(R.color.BlueTintBackground));
		}
		child.setPosition(position);
		view.setTag(child);
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		tv.setText(child.getName().toString());
		return view;
	}
	
}
