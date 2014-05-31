package com.matthew.ahsam.phoenix.randommusicplayer;

public class SongListChild {
	private String mName;
	private String mFullPath;
	private boolean mDirectory;
	private boolean mSelected;

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
}
