package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SongListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<SongListGroup> groups;
		
		public SongListAdapter (Context c, ArrayList<SongListGroup> g) {
			context = c;
			groups = g;
		}
		
		public void addGroup (SongListGroup group) {
			groups.add(group);
		}
		
		public void addItem (SongListChild item, SongListGroup group) {
			
		}
		
		public Object getChild (int groupPosition, int childPosition) {
			ArrayList<SongListChild> childList = groups.get(groupPosition).getSongs();
			return childList.get(childPosition);
		}
		
		public long getChildId (int groupPosition, int childPosition) {
			return childPosition;
		}
		
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
			SongListChild child = (SongListChild) getChild(groupPosition,childPosition);
			if(view == null) {
				LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = infalInflater.inflate(R.layout.songlist_child, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.tvChild);
			tv.setText(child.getName().toString());
			return view;
		}
		
		public int getChildrenCount (int groupPosition) {
			try {
					ArrayList<SongListChild> chList = groups.get(groupPosition).getSongs();
					return chList.size();
			}
			catch (Exception e) {
				e.printStackTrace();
				//Log.e("ChildCount",);
			}
			return 0;
		}
		
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}
		
		public int getGroupCount() {
			return groups.size();
		}
		
		public long getGroupId (int groupPosition) {
			return groupPosition;
		}
		
		public View getGroupView (int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
			SongListGroup group = (SongListGroup) getGroup(groupPosition);
			if (view == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(R.layout.songlist_group, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.tvGroup);
			tv.setText (group.getName());
			return view;
		}
		
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}
	
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

}
