package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SongListGroup extends SongListChild implements Parcelable {
	private SectionType mType;
	private ArrayList<SongListChild> Songs;
	private int mArraySize;
	private int mMaxRandomSongs;

	
	//Functions
	public SongListGroup () {
		mType = SectionType.RANDOM;
		Songs = new ArrayList<SongListChild>();
		mArraySize = Songs.size();
		mMaxRandomSongs = 3;
	}
	
	public SongListGroup (SongListGroup slg) {
		this.mType = slg.mType;
		this.Songs = slg.Songs;
		this.mArraySize = slg.mArraySize;
		this.mMaxRandomSongs = slg.mMaxRandomSongs;
	}
	
	public String getTypeAsString () {
		switch(mType) {
			case RANDOM:
				return "Random";
			case ORDERED:
				return "Ordered";
			default:
				return "ERROR";
		}
	}

	
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
	
	public void setType(int type) {
		if (type >= 0 && type <= 1){
			mType = SectionType.values()[type];
		}
	}
	
	public int getMaxRandomSongs() {
		return mMaxRandomSongs;
	}

	public void setMaxRandomSongs(int maxRandomSongs) {
		mMaxRandomSongs = maxRandomSongs;
	}
	
	
	public boolean hasChildren() {
		if (Songs == null) {
			return false;
		} else {
			return true;
		}
	}
	
	//Parcelable 
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		mArraySize = Songs.size();
		dest.writeInt (mType.getValue());
		dest.writeInt(mArraySize);
		dest.writeArray(Songs.toArray());
		
	}
	
	public static final Parcelable.Creator<SongListGroup> CREATOR = new Parcelable.Creator<SongListGroup>() {
		public SongListGroup createFromParcel(Parcel in) {
		    return new SongListGroup(in);
		}
	
	public SongListGroup[] newArray(int size) {
	    return new SongListGroup[size];
		}
	};
	
	private SongListGroup (Parcel in) {
		int v = 0;
		in.writeInt(v);
		mType.setValue(v);
		in.writeInt(mArraySize);
		SongListChild [] songArray = new SongListGroup [mArraySize];
		in.writeArray(songArray);
		for (int i = 0; i < mArraySize; i++) {
			Songs.add(songArray[i]);
		}
	}



}

