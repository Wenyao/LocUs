package com.example.locus.entity;

import java.io.Serializable;

import com.example.locus.tilesystem.Point2D;
import com.example.locus.tilesystem.TileSystem;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8199692036850547691L;

	public static String UnknownName = "Unknown";

	private String name;
	private Sex sex;
	private String ip;
	private double latitude;
	private double longtitude;

	public User() {
		this(UnknownName, Sex.Unknown, null, Double.MIN_VALUE, Double.MIN_VALUE);
	}

	public User(String name, Sex sex, String ip, double latitude,
			double longtitude) {
		this.name = name;
		this.sex = sex;
		this.ip = ip;
		this.latitude = latitude;
		this.longtitude = longtitude;
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
		if (latitude == Double.MIN_VALUE || longtitude == Double.MIN_VALUE){
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

	@Override
	public String toString() {
		return "User [name=" + name + ", sex=" + sex + ", ip=" + ip + ", lat="
				+ latitude + ", lon=" + longtitude + ", tile="
				+ getTileNumber() + "]";
	}

}
