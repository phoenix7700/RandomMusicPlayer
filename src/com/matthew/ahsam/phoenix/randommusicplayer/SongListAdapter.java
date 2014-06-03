package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SongListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<SongListGroup> groups;
		private int mTempPosition;
		
		public SongListAdapter (Context c, ArrayList<SongListGroup> g) {
			context = c;
			groups = g;
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
			if (groups.get(groupPosition).getSongs() != null ) {
					ArrayList<SongListChild> chList = groups.get(groupPosition).getSongs();
					return chList.size();
			}
			return 0;
		}
		
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}
		
		public int getGroupCount() {
			return groups.size();
		}
		
		public int getGroupPosition (SongListGroup slg) {
			for (int i=0; i < getGroupCount(); i++) {
				if (slg.equals(getGroup(i)))
					return i;
			}
			return -1;
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
			if (view != null) {
				view.setOnDragListener(new View.OnDragListener() {
					
					@Override
					public boolean onDrag(View v, DragEvent event) {
						switch (event.getAction()) {
						case DragEvent.ACTION_DRAG_STARTED:
							break;
						case DragEvent.ACTION_DRAG_ENTERED:
							v.setSelected(true);
							v.setBackgroundColor(v.getResources().getColor(R.color.BlueTintBackground));
							break;
						case DragEvent.ACTION_DRAG_LOCATION:
							break;
						case DragEvent.ACTION_DRAG_EXITED:
							v.setBackgroundColor(v.getResources().getColor(android.R.color.background_light));
							break;
						case DragEvent.ACTION_DROP:
							ViewGroup view = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(R.id.expandableListViewSongList);
							ViewGroup vgChild;
							int i=0,j=0;
							while(i < groups.size()){
								vgChild = (ViewGroup) view.getChildAt(j);
								View vChild = vgChild.getChildAt(0);
								if (vChild.getId() == R.id.tvGroup) {
									if (view.getChildAt(j).getTop() == v.getTop()) {
										mTempPosition = i;
										break;
									}
									i++;
								}
								j++;
							}
							v.setBackgroundColor(v.getResources().getColor(android.R.color.background_light));
							SongListChild slc = (SongListChild) event.getLocalState();
							groups.get(mTempPosition).getSongs().add(slc);
							notifyDataSetChanged();
							break;
						case DragEvent.ACTION_DRAG_ENDED:
							break;
							
						default:
							break;
						}
						return true;
					}
				});
			}
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
