package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cs.serl.wikidev.Document;
import ca.ualberta.cs.serl.wikidev.Project;
import ca.ualberta.cs.serl.wikidev.User;

/**
 * @author   User
 */
public class IArtifact implements Comparable<IArtifact>{

	//protected HashMap<IArtifact, Integer> relatedArtifacts;
	/**
	 * @uml.property  name="directlyRelatedArtifacts"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="artifact:ca.ualberta.cs.serl.wikidev.artifacts.RelatedArtifact"
	 */
	protected ArrayList<RelatedArtifact> directlyRelatedArtifacts;
	//protected HashMap<IArtifact, HashMap<IArtifact, Integer>> indirectlyRelatedArtifacts;
	/**
	 * @uml.property  name="owner"
	 * @uml.associationEnd  
	 */
	protected User owner;
	/**
	 * @uml.property  name="timestamp"
	 */
	protected Timestamp timestamp;
	/**
	 * @uml.property  name="project"
	 * @uml.associationEnd  inverse="relatedArtifacts:ca.ualberta.cs.serl.wikidev.Project"
	 */
	protected Project project;
	/**
	 * @uml.property  name="document"
	 * @uml.associationEnd  
	 */
	protected Document document;
	/**
	 * @uml.property  name="index"
	 */
	protected int index;
	/**
	 * @uml.property  name="uid"
	 */
	protected int uid;
	protected double[] coords;
	protected boolean enforced;
	private String type;
	private int id;
	protected User lastEditor;
	protected Timestamp lastModified;
	protected Timestamp created;
	
	public IArtifact(String type, int id) {
		this.type = type;
		this.id = id;
	}
	
	
	
	public User getLastEditor() {
		return lastEditor;
	}



	public Timestamp getLastModified() {
		return lastModified;
	}



	public Timestamp getCreated() {
		return created;
	}



	public void setCreated(Timestamp created) {
		this.created = created;
	}



	public IArtifact() {
	}

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		if(this instanceof ChangeSet) {
			return ((ChangeSet)this).getRevision();
		}
		else if(this instanceof Ticket) {
			return ((Ticket)this).getTicketID();
		}
		else if(this instanceof Wiki) {
			return ((Wiki)this).getPage_id();
		}
		else if(this instanceof Message) {
			return ((Message)this).getCommunicationID();
		}
		else {
			return this.id;
		}
	}



	public boolean isEnforced() {
		return enforced;
	}

	public void setEnforced(boolean enforced) {
		this.enforced = enforced;
	}

	public double[] getCoords() {
		return coords;
	}

	public void setCoords(double[] coords) {
		this.coords = coords;
	}

	/**
	 * @return
	 * @uml.property  name="uid"
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid
	 * @uml.property  name="uid"
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return
	 * @uml.property  name="document"
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document
	 * @uml.property  name="document"
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 * @uml.property  name="index"
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/*public void addRelatedArtifact(IArtifact artifact, int weight) {
		relatedArtifacts.put(artifact, weight);
	}*/
	
	public void addDirectlyRelatedArtifact(IArtifact artifact, double value, String word) {
		this.directlyRelatedArtifacts.add(new RelatedArtifact(artifact, value, word));
	}
	
	/*public void addIndirectlyRelatedArtifact(IArtifact artifact, IArtifact source) {
		if(!indirectlyRelatedArtifacts.containsKey(artifact)) {
			HashMap<IArtifact, Integer> map = new HashMap<IArtifact, Integer>();
			map.put(source, 1);
			indirectlyRelatedArtifacts.put(artifact, map);
		}
		else {
			HashMap<IArtifact, Integer> map = indirectlyRelatedArtifacts.get(artifact);
			if(map.containsKey(source)) {
				map.put(source, map.get(source)+1);
				indirectlyRelatedArtifacts.put(artifact, map);
			}
			else {
				map.put(source, 1);
				indirectlyRelatedArtifacts.put(artifact, map);
			}
		}
	}*/
	
	/*public HashMap<IArtifact, Integer> getRelatedArtifactsWithWeights() {
		HashMap<IArtifact, Integer> weightMap = new HashMap<IArtifact, Integer>();
		for(IArtifact artifact : indirectlyRelatedArtifacts.keySet()) {
			int paths = 0;
			HashMap<IArtifact, Integer> map = indirectlyRelatedArtifacts.get(artifact);
			for(IArtifact source : map.keySet()) {
				paths += map.get(source);
			}
			weightMap.put(artifact, paths);
		}
		return weightMap;
	}*/
	
	/*public int getNumberofRelatedArtifacts() {
		return directlyRelatedArtifacts.size() + indirectlyRelatedArtifacts.size();
	}*/
	
	public ArrayList<RelatedArtifact> getDirectlyRelatedArtifacts() {
		return directlyRelatedArtifacts;
	}

	/*public HashMap<IArtifact, HashMap<IArtifact, Integer>> getIndirectlyRelatedArtifacts() {
		return indirectlyRelatedArtifacts;
	}*/

	/**
	 * @return
	 * @uml.property  name="relatedArtifacts"
	 */
	/*public HashMap<IArtifact, Integer> getRelatedArtifacts() {
		return relatedArtifacts;
	}*/

	/**
	 * @return
	 * @uml.property  name="owner"
	 */
	public User getOwner() {
		return owner;
	}
	
	/**
	 * @return
	 * @uml.property  name="project"
	 */
	public Project getProject() {
		return project;
	}
	
	/**
	 * @param  project
	 * @uml.property  name="project"
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return
	 * @uml.property  name="projectID"
	 */
	//public int getProjectID() {
	//	return projectID;
	//}

	/**
	 * @return
	 * @uml.property  name="owner"
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return
	 * @uml.property  name="timestamp"
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 * @uml.property  name="timestamp"
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public boolean containsRelatedArtifact(IArtifact artifact) {
		
		for(RelatedArtifact related : directlyRelatedArtifacts) {
			if(related.getArtifact().equals(artifact)) {
				return true;
			}
		}
		return false;
	}
	
	public RelatedArtifact getRelatedArtifact(IArtifact artifact) {
		for(RelatedArtifact related : directlyRelatedArtifacts) {
			if(related.getArtifact().equals(artifact)) {
				return related;
			}
		}
		return null;
	}

	/**
	 * @return
	 * @uml.property  name="url"
	 */
	//public String getUrl() {
	//	return url;
	//}

	public Set<User> getAssociatedUsers(){return null;};
	public String toString() {
		return project.getProjectName();
	}

	@Override
	public int compareTo(IArtifact arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
