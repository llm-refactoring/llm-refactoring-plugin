package ca.ualberta.cs.serl.wikidev.city3d.animation;

import java.sql.Timestamp;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.User;

public class CommunicationBuildingData extends BuildingData{
	

	private int artifact_id;
	
	public CommunicationBuildingData(String type, int artifactId,
			Timestamp week, int height, int neighborhoodX, int neighborhoodY,
			int cityBlock, String url, int projectid, ArrayList<UserData> users, User lastEditor, Timestamp created, Timestamp lastModified) {
		super(type, week, height, neighborhoodX, neighborhoodY, cityBlock, url, projectid, users, lastEditor, created, lastModified);
		artifact_id = artifactId;
	}
	
	public CommunicationBuildingData(String type, int artifactId,
			Timestamp week, int height, int neighborhoodX, int neighborhoodY,
			int cityBlock, String url, int projectid, int buildingID, String lastEditorString, Timestamp created, Timestamp lastModified) {
		super(type, week, height, neighborhoodX, neighborhoodY, cityBlock, url, projectid, buildingID, lastEditorString, created, lastModified);
		artifact_id = artifactId;
	}

	public int getArtifact_id() {
		return artifact_id;
	}
	
	public String toString() {
		return type+artifact_id+"\t"+week;
	}
	
}
