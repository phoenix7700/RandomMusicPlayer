package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

public class SongListGroup {
	private String mName;
	private ArrayList<SongListChild> Songs;

	public ArrayList<SongListChild> getSongs() {
		return Songs;
	}

	public void setSongs(ArrayList<SongListChild> songs) {
		Songs = songs;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}

