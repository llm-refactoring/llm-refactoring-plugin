package ca.ualberta.cs.serl.wikidev.clustering;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import ca.ualberta.cs.serl.wikidev.User;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;

public class Cluster {
	
	/**
	 * @uml.property  name="artifacts"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.artifacts.IArtifact"
	 */
	private ArrayList<IArtifact> artifacts;
	private HashMap<IArtifact, ClusterPoint> coordinates;
	/**
	 * @uml.property  name="users"
	 */
	private HashSet<User> users;
	/**
	 * @uml.property  name="words"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private HashSet<String> words;
	/**
	 * @uml.property  name="fromdate"
	 */
	private Timestamp fromdate;
	/**
	 * @uml.property  name="todate"
	 */
	private Timestamp todate;
	/**
	 * @uml.property  name="project"
	 */
	private int project;
	/**
	 * @uml.property  name="index"
	 */
	private int index;
	private String cluster_set_name;
	private double threshold;
	private int clusterid;
	private String projectname;

	//private ArrayList<double[]> coordinates;
	//private ArrayList<ClusterPoint> points;
	//private ClusterPoint medoid;
	//private ArrayList<ClusterPoint> nonMedoids;
	
	public Cluster() {
		artifacts = new ArrayList<IArtifact>();
		coordinates = new HashMap<IArtifact, ClusterPoint>();
		words = new HashSet<String>();
		users = new HashSet<User>();
	}
	
	public Cluster(Timestamp todate, String project, double threshold, String terms, int clusterid) {
		this.todate = todate;
		this.projectname = project;
		this.words = new HashSet<String>();
		String[] words = terms.split("\t");
		for(int i=0; i<words.length; i++) {
			this.words.add(words[i]);
		}
		artifacts = new ArrayList<IArtifact>();
		this.clusterid = clusterid;
		this.threshold = threshold;
	}
	
	
	
	public String getProjectname() {
		return projectname;
	}

	public int getClusterid() {
		return clusterid;
	}

	public void addCoordinate(IArtifact artifact, ClusterPoint point) {
		coordinates.put(artifact, point);
	}



	public HashMap<IArtifact, ClusterPoint> getCoordinates() {
		return coordinates;
	}







	public void setCoordinates(HashMap<IArtifact, ClusterPoint> coordinates) {
		this.coordinates = coordinates;
	}







	public String getCluster_set_name() {
		return cluster_set_name;
	}



	public void setCluster_set_name(String cluster_set_name) {
		this.cluster_set_name = cluster_set_name;
	}



	public double getThreshold() {
		return threshold;
	}



	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}



	/**
	 * @param fromdate
	 * @uml.property  name="fromdate"
	 */
	public void setFromdate(Timestamp fromdate) {
		this.fromdate = fromdate;
	}



	/**
	 * @param todate
	 * @uml.property  name="todate"
	 */
	public void setTodate(Timestamp todate) {
		this.todate = todate;
	}



	/**
	 * @param project
	 * @uml.property  name="project"
	 */
	public void setProject(int project) {
		this.project = project;
	}



	/**
	 * @param index
	 * @uml.property  name="index"
	 */
	public void setIndex(int index) {
		this.index = index;
	}



	/**
	 * @return
	 * @uml.property  name="fromdate"
	 */
	public Timestamp getFromdate() {
		return fromdate;
	}



	/**
	 * @return
	 * @uml.property  name="todate"
	 */
	public Timestamp getTodate() {
		return todate;
	}



	/**
	 * @return
	 * @uml.property  name="project"
	 */
	public int getProject() {
		return project;
	}



	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}



	public void addArtifact(IArtifact entity) {
		if (!artifacts.contains(entity)) {
			artifacts.add(entity);
		}
	}

	public void addArtifacts(ArrayList<IArtifact> entities) {
		if (!this.artifacts.containsAll(entities)) {
			this.artifacts.addAll(entities);
		}
	}

	public ArrayList<IArtifact> getArtifacts() {
		return artifacts;
	}
	
	public void setUsersAndWords() {
		for(IArtifact artifact1 : artifacts) {
			users.addAll(artifact1.getAssociatedUsers());
			for(IArtifact artifact2 : artifacts) {
				if(artifact1.containsRelatedArtifact(artifact2)) {
					words.add(artifact1.getRelatedArtifact(artifact2).getWord());
				}
			}
		}
	}

	public HashSet<User> getUsers() {
		return users;
	}

	public HashSet<String> getWords() {
		return words;
	}
	
	public boolean equals(Object o) {
		Cluster c = (Cluster)o;
		if(this.artifacts.size() == c.artifacts.size()) {
			int counter = 0;
			for(IArtifact artifact : c.artifacts) {
				if(this.artifacts.contains(artifact)) {
					counter++;
				}
			}
			if(counter == this.artifacts.size()) {
				return true;
			}
			else {
				return false;
			}
		}
		else{
			return false;
		}
	}

}
