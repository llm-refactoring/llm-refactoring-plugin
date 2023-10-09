package ca.ualberta.cs.serl.wikidev.city3d.animation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cs.serl.wikidev.DataManager;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSetDetail;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.city3d.Layout;

public class ClusterData {
	
	private Timestamp week;
	private HashMap<IArtifact, Double> artifacts;
	private HashMap<String, Double> terms;
	private HashMap<BuildingData, Double> buildings;
	private ArrayList<String> artifactStrings;
	
	public ClusterData(Timestamp week) {
		this.week = week;
		this.artifacts = new HashMap<IArtifact, Double>();
		this.terms = new HashMap<String, Double>();
		this.buildings = new HashMap<BuildingData, Double>();
		this.artifactStrings = new ArrayList<String>();
	}

	public HashMap<String, Double> getTerms() {
		return terms;
	}
	
	public double getArtifact(String art) {
		for(IArtifact artifact : artifacts.keySet()) {
			if(artifact.toString().equals(art)) {
				return artifacts.get(artifact);
			} 
		}
		return Double.MAX_VALUE;
	}

	public void addArtifacts(HashMap<IArtifact, Double> newArtifacts) {
		if(artifacts.keySet().isEmpty()) {
			artifacts.putAll(newArtifacts);
			for(IArtifact artifact :artifacts.keySet()) {
				artifactStrings.add(artifact.toString());
			}
		}
		else {
			for(IArtifact artifact : newArtifacts.keySet()) {
				if(artifactStrings.contains(artifact.toString())) {
					if(getArtifact(artifact.toString()) > newArtifacts.get(artifact)) {
						artifacts.put(artifact, newArtifacts.get(artifact));
						artifactStrings.add(artifact.toString());
					}
				}
				else {
					artifacts.put(artifact, newArtifacts.get(artifact));
					artifactStrings.add(artifact.toString());
				}
			}
		}
	}
	
	public void addTerms(HashMap<String, Double> newTerms) {
		if(terms.keySet().isEmpty()) {
			terms.putAll(newTerms);
		}
		else {
			for(String term : newTerms.keySet()) {
				if(terms.containsKey(term)) {
					if(terms.get(term) > newTerms.get(term)) {
						terms.put(term, newTerms.get(term));
					}
				}
				else {
					terms.put(term, newTerms.get(term));
				}
			}
		}
	}
	
	public void calculateBuildingData() {
		for(IArtifact artifact : artifacts.keySet()) {
			if(!artifact.getType().equals("ChangeSet")) {
				try {
					ArrayList<CommunicationBuildingData> buildingHistory = DataManager.getCommunicationsBuildingHistory(artifact.getType(), artifact.getId(), artifact.getProject().getProjectID());
					for(CommunicationBuildingData building : buildingHistory) {
						if(building.getWeek().equals(week)) {
							buildings.put(building, artifacts.get(artifact));
							break;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				ChangeSet changeset = (ChangeSet)artifact;
				for(ChangeSetDetail detail : changeset.getChangesetDetails()) {
					String url = Layout.URLprefix+changeset.getProject().getProjectName()+"_File:"+detail.getFilename();
					try {
						ArrayList<FileBuildingData> buildingHistory = DataManager.getFilesBuildingHistory(url);
						for(FileBuildingData building : buildingHistory) {
							if(building.getWeek().equals(week)) {
								buildings.put(building, artifacts.get(artifact));
								break;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public HashMap<BuildingData, Double> getBuildings() {
		return buildings;
	}
}
