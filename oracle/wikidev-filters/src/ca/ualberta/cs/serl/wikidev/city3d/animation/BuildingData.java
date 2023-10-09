package ca.ualberta.cs.serl.wikidev.city3d.animation;

import java.sql.Timestamp;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.User;

public class BuildingData {
	
	protected String type;
	protected Timestamp week;
	protected int height;
	protected int neighborhoodX;
	protected int neighborhoodY;
	protected int cityBlock;
	protected String url;
	protected int projectid;
	protected ArrayList<UserData> users;
	protected int buildingID;
	protected User lastEditor;
	protected String lastEditorString;
	protected Timestamp created;
	protected Timestamp lastModified;
	
	public BuildingData(String type, Timestamp week, int height,
			int neighborhoodX, int neighborhoodY, int cityBlock, String url,
			int projectid, ArrayList<UserData> users, User lastEditor, Timestamp created, Timestamp lastModified) {
		this.type = type;
		this.week = week;
		this.height = height;
		this.neighborhoodX = neighborhoodX;
		this.neighborhoodY = neighborhoodY;
		this.cityBlock = cityBlock;
		this.url = url;
		this.projectid = projectid;
		this.users = users;
		this.lastEditor = lastEditor;
		this.created = created;
		this.lastModified = lastModified;
	}
	
	public BuildingData(String type, Timestamp week, int height,
			int neighborhoodX, int neighborhoodY, int cityBlock, String url,
			int projectid, int buildingID, String lastEditorString, Timestamp created, Timestamp lastModified) {
		this.type = type;
		this.week = week;
		this.height = height;
		this.neighborhoodX = neighborhoodX;
		this.neighborhoodY = neighborhoodY;
		this.cityBlock = cityBlock;
		this.url = url;
		this.projectid = projectid;
		this.buildingID = buildingID;
		this.lastEditorString = lastEditorString;
		this.created = created;
		this.lastModified = lastModified;
	}
	
	
	
	public String getLastEditorString() {
		return lastEditorString;
	}

	public void setLastEditor(User lastEditor) {
		this.lastEditor = lastEditor;
	}

	public User getLastEditor() {
		return lastEditor;
	}

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public int getBuildingID() {
		return buildingID;
	}

	public String getType() {
		return type;
	}


	public Timestamp getWeek() {
		return week;
	}


	public int getHeight() {
		return height;
	}


	public int getNeighborhoodX() {
		return neighborhoodX;
	}


	public int getNeighborhoodY() {
		return neighborhoodY;
	}


	public int getCityBlock() {
		return cityBlock;
	}
	
	public void setCityBlock(int cityBlock) {
		this.cityBlock = cityBlock;
	}


	public String getUrl() {
		return url;
	}


	public int getProjectid() {
		return projectid;
	}


	public ArrayList<UserData> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<UserData> users) {
		this.users = users;
	}

}
