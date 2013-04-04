package com.example.locus.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.locus.tilesystem.Point2D;
import com.example.locus.tilesystem.TileSystem;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8199692036850547691L;

	public static String UnknownName = "Unknown";

	private String id;
	private String name;
	private Sex sex;
	private String ip;
	private double latitude;
	private double longtitude;
	private String interests;

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public User() {
		this(UnknownName, Sex.Unknown, null, Double.MIN_VALUE,
				Double.MIN_VALUE, null);
	}

	public User(String name, Sex sex, String ip, double latitude,
			double longtitude, String interests) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
		id = name + sdf.format(cal.getTime());
		this.name = name;
		this.sex = sex;
		this.ip = ip;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.interests = interests;
	}
	
	public User(String value){
		//TODO refactor
		String[] splitsStrings = value.split(",");
		id = splitsStrings[0];
		name = splitsStrings[1];
		sex = Sex.values()[Integer.parseInt(splitsStrings[2])];
		ip = splitsStrings[3];
		latitude = Double.parseDouble(splitsStrings[4]);
		longtitude = Double.parseDouble(splitsStrings[5]);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public String getTileNumber() {
		if (latitude == Double.MIN_VALUE || longtitude == Double.MIN_VALUE) {
			return null;
		}

		Point2D pixel = new Point2D();
		TileSystem.LatLongToPixelXY(latitude, longtitude,
				TileSystem.DefaultLevelOfDetail, pixel);

		Point2D tile = new Point2D();
		TileSystem
				.PixelXYToTileXY((int) pixel.getX(), (int) pixel.getY(), tile);

		return TileSystem.TileXYToQuadKey((int) tile.getX(), (int) tile.getY(),
				TileSystem.DefaultLevelOfDetail);
	}
	
	public String serialize(){
		return String.format("%s,%s,%d,%s,%f,%f", id, name, sex, ip, latitude, longtitude);
	}
	
	@Override
	public String toString() {
		return "User [id = "+ id + ", name = " + name + ", sex=" + sex + ", ip=" + ip + ", lat="
				+ latitude + ", lon=" + longtitude + ", tile="
				+ getTileNumber() + "]";
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			return id.equals(((User) o).id);
		}
		return false;
	}

}
