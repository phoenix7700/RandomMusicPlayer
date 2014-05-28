package com.matthew.ahsam.phoenix.randommusicplayer;

import java.io.File;

public class FileSort extends Quicksort<File> {

	@Override
	protected int compare(File f1, File f2) {
		int cmp = f1.getName().compareTo(f2.getName());
		if (cmp >= 1) {
			return 1;
		} else if (cmp <= -1) {
			return -1;
		} else {
			return 0;
		}
	}
	
	FileSort (File [] files) {
		setFullArray(files);
	}
}
