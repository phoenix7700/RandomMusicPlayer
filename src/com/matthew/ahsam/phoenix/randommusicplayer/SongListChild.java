package com.matthew.ahsam.phoenix.randommusicplayer;

import android.os.Parcel;
import android.os.Parcelable;

public class SongListChild implements Parcelable{
	private String mName;
	private String mArtist;
	private String mAlbum;
	private String mFullPath;
	private boolean mDirectory;
	private boolean mSelected;
	private	int mPosition;
	
	//Functions
	SongListChild () {
		mName = "Unknown";
		mFullPath = null;
		mDirectory = false;
		mSelected = false;
		mPosition = 0;
	}
	
	public SongListChild(SongListChild slc) {
		this.mName = slc.mName;
		this.mArtist = slc.mArtist;
		this.mAlbum = slc.mAlbum;
		this.mFullPath = slc.mFullPath;
		this.mDirectory = slc.mDirectory;
		this.mSelected = slc.mSelected;
		this.mPosition = slc.mPosition;
	}

	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean selected) {
		mSelected = selected;
	}

	public String getFullPath() {
		return mFullPath;
	}

	public void setFullPath(String fullPath) {
		mFullPath = fullPath;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public boolean isDirectory() {
		return mDirectory;
	}

	public void setDirectory(boolean directory) {
		mDirectory = directory;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}
	
	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String artist) {
		mArtist = artist;
	}
	
	public String getAlbum() {
		return mAlbum;
	}

	public void setAlbum(String album) {
		mAlbum = album;
	}

	//Parcelable
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mFullPath);
		boolean [] boolarray = new boolean [2];
		boolarray [0] = mDirectory;
		boolarray [1] = mSelected;
		dest.writeBooleanArray(boolarray);
		dest.writeInt(mPosition);
	}
	
	public static final Parcelable.Creator<SongListChild> CREATOR = new Parcelable.Creator<SongListChild>() {
		public SongListChild createFromParcel(Parcel in) {
		    return new SongListChild(in);
		}
	
	public SongListChild[] newArray(int size) {
	    return new SongListChild[size];
		}
	};
	
	private SongListChild (Parcel in) {
		
	}


}
