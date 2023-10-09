package ca.ualberta.cs.serl.wikidev.city3d.animation;

import java.sql.Timestamp;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.User;

public class FileBuildingData extends BuildingData{
	
	
	public FileBuildingData(String type, Timestamp week, int height,
			int neighborhoodX, int neighborhoodY, int cityBlock, String url,
			int projectid, ArrayList<UserData> users, User lastEditor, Timestamp created, Timestamp lastModified) {
		super(type, week, height, neighborhoodX, neighborhoodY, cityBlock, url, projectid, users, lastEditor, created, lastModified);
	}
	
	public FileBuildingData(String type, Timestamp week, int height,
			int neighborhoodX, int neighborhoodY, int cityBlock, String url,
			int projectid, int buildingID, String lastEditorString, Timestamp created, Timestamp lastModified) {
		super(type, week, height, neighborhoodX, neighborhoodY, cityBlock, url, projectid, buildingID, lastEditorString, created, lastModified);
	}	
	
	public String toString() {
		return url;
	}

}
