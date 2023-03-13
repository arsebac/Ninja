package com.duckies.gdx.ninja.pojo;

import com.badlogic.gdx.math.Vector2;

public class Warp {
	Vector2 departureCoords;
	String departureMap;

	Vector2 arrivalCoors;

	String arrivalMap;

	public Warp() {
	}

	public Warp(Vector2 departureCoords, String departureMap, Vector2 arrivalCoors, String arrivalMap) {
		this.departureCoords = departureCoords;
		this.departureMap = departureMap;
		this.arrivalCoors = arrivalCoors;
		this.arrivalMap = arrivalMap;
	}

	public Vector2 getDepartureCoords() {
		return departureCoords;
	}

	public void setDepartureCoords(Vector2 departureCoords) {
		this.departureCoords = departureCoords;
	}

	public String getDepartureMap() {
		return departureMap;
	}

	public void setDepartureMap(String departureMap) {
		this.departureMap = departureMap;
	}

	public Vector2 getArrivalCoors() {
		return arrivalCoors;
	}

	public void setArrivalCoors(Vector2 arrivalCoors) {
		this.arrivalCoors = arrivalCoors;
	}

	public String getArrivalMap() {
		return arrivalMap;
	}

	public void setArrivalMap(String arrivalMap) {
		this.arrivalMap = arrivalMap;
	}
}
