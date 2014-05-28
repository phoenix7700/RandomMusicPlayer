package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

public class SongListGroup extends SongListChild {
	
	private enum SongType { RANDOM, ORDERED }
	private SongType mType;
	private ArrayList<SongListChild> Songs;

	public ArrayList<SongListChild> getSongs() {
		return Songs;
	}

	public void setSongs(ArrayList<SongListChild> songs) {
		Songs = songs;
	}

	public SongType getType() {
		return mType;
	}

	public void setType(SongType type) {
		mType = type;
	}
	
	public boolean hasChildren() {
		if (Songs == null) {
			return false;
		} else {
			return true;
		}
	}
}

