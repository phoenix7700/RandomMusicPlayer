package com.matthew.ahsam.phoenix.randommusicplayer;

public enum SectionType { RANDOM(0), ORDERED(1);
	private int value;
	private SectionType (int v) {
		value = v;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int v) {
		value = v;
	}
}