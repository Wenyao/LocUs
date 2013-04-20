package com.example.locus.entity;

public enum Sex {
	Unknown(0),
	Male(1),
	Female(2);
	
	
	private final int value;

    Sex(int value) {
        this.value = value;
    }
	
	public int getValue() {
        return value;
    }
}
