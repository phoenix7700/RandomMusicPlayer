package com.matthew.ahsam.phoenix.randommusicplayer;

public class SongListChild {
	private String mName;
	private String mFullPath;
	private boolean mDirectory;

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
}
