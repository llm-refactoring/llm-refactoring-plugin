package ca.ualberta.cs.serl.wikidev.city3d.animation;

import java.sql.Timestamp;

public class UserData {

	private String username;
	private int R;
	private int G;
	private int B;
	private double contribution;
	private Timestamp week;
	private String cityType;
	
	public UserData(String username, int r, int g, int b, double contribution,
			Timestamp week, String cityType) {
		this.username = username;
		R = r;
		G = g;
		B = b;
		this.contribution = contribution;
		this.week = week;
		this.cityType = cityType;
	}

	public String getCityType() {
		return cityType;
	}

	public String getUsername() {
		return username;
	}

	public int getR() {
		return R;
	}

	public int getG() {
		return G;
	}

	public int getB() {
		return B;
	}

	public double getContribution() {
		return contribution;
	}

	public Timestamp getWeek() {
		return week;
	}
	
	
}
