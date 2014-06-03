package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

public class SongListGroup extends SongListChild {
	
	private enum SectionType { RANDOM, ORDERED }
	private SectionType mType;
	private ArrayList<SongListChild> Songs;

	public ArrayList<SongListChild> getSongs() {
		return Songs;
	}

	public void setSongs(ArrayList<SongListChild> songs) {
		Songs = songs;
	}

	public SectionType getType() {
		return mType;
	}

	public void setType(SectionType type) {
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

