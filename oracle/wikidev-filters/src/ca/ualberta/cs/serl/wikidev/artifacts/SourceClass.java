package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.User;

/**
 * @author   User
 */
public class SourceClass extends IArtifact {
	
	/**
	 * @uml.property  name="classID"
	 */
	private int classID;
	/**
	 * @uml.property  name="className"
	 */
	private String className;
	/**
	 * @uml.property  name="editors"
	 */
	private ArrayList<User> editors;
	/**
	 * @uml.property  name="dateCreated"
	 */
	private Timestamp dateCreated;
	/**
	 * @uml.property  name="parents"
	 */
	private ArrayList<SourceClass> parents;
	/**
	 * @uml.property  name="childs"
	 */
	private ArrayList<SourceClass> childs;
	/**
	 * @uml.property  name="referenced"
	 */
	private ArrayList<SourceClass> referenced;
	/**
	 * @uml.property  name="usageWeights"
	 */
	private HashMap<User, Integer> usageWeights;
	
	
	
	public SourceClass(int classID, String className, Timestamp dateCreated, int ownerID, Timestamp lastModified, String url, int projectID) {
		this.classID = classID;
		this.className = className;
		this.dateCreated = dateCreated;
		//this.ownerID = ownerID;
		this.timestamp = lastModified;
		//this.url = url;
		//this.projectID = projectID;
		this.directlyRelatedArtifacts = new ArrayList<RelatedArtifact>();
		//this.indirectlyRelatedArtifacts = new HashMap<IArtifact, HashMap<IArtifact, Integer>>();
	}


	/**
	 * @return
	 * @uml.property  name="className"
	 */
	public String getClassName() {
		return className;
	}


	/**
	 * @return
	 * @uml.property  name="classID"
	 */
	public int getClassID() {
		return classID;
	}


	/**
	 * @return
	 * @uml.property  name="editors"
	 */
	public ArrayList<User> getEditors() {
		return editors;
	}


	/**
	 * @param  editors
	 * @uml.property  name="editors"
	 */
	public void setEditors(ArrayList<User> editors) {
		this.editors = editors;
	}


	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		users.add(owner);
		users.addAll(editors);
		return users;
	}
	
	public String toString() {
		return "SourceClass "+classID+" "+super.toString();//+": "+className;
	}


	public int compareTo(IArtifact o) {
		if(o instanceof SourceClass) {
			if(this.equals((SourceClass)o)) {
				return 0;
			}
			else {
				return 1;
			}
		}
		else {
			return 1;
		}
	}

}
