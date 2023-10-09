package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.User;

/**
 * @author   User
 */
public class ChangeSet extends IArtifact {
	
	/**
	 * @uml.property  name="changeSetID"
	 */
	private int changeSetID;
	/**
	 * @uml.property  name="externalSystemID"
	 */
	private int externalSystemID;
	/**
	 * @uml.property  name="comment"
	 */
	private String comment;
	/**
	 * @uml.property  name="externalAuthorUserName"
	 */
	private String externalAuthorUserName;
	/**
	 * @uml.property  name="project"
	 */
	private int project;
	/**
	 * @uml.property  name="revision"
	 */
	private int revision;
	/**
	 * @uml.property  name="changesetDetails"
	 */
	private ArrayList<ChangeSetDetail> changesetDetails;
	
	
	public ChangeSet(int changeSetID, String comment, String externalAuthorUserName,
			int externalSystemID, Timestamp timestamp, int revision, int project) {
		this.changeSetID = changeSetID;
		this.comment = comment;
		this.externalAuthorUserName = externalAuthorUserName;
		this.externalSystemID = externalSystemID;
		this.timestamp = timestamp;
		this.revision = revision;
		this.project = project;
		this.directlyRelatedArtifacts = new ArrayList<RelatedArtifact>();
		//this.indirectlyRelatedArtifacts = new HashMap<IArtifact, HashMap<IArtifact, Integer>>();
		this.changesetDetails = new ArrayList<ChangeSetDetail>();
	}


	/**
	 * @return
	 * @uml.property  name="revision"
	 */
	public int getRevision() {
		return revision;
	}


	/**
	 * @param revision
	 * @uml.property  name="revision"
	 */
	public void setRevision(int revision) {
		this.revision = revision;
	}


	public ArrayList<ChangeSetDetail> getChangesetDetails() {
		return changesetDetails;
	}


	public void setChangesetDetails(ArrayList<ChangeSetDetail> changesetDetails) {
		this.changesetDetails = changesetDetails;
	}


	/**
	 * @return
	 * @uml.property  name="externalAuthorUserName"
	 */
	public String getExternalAuthorUserName() {
		return externalAuthorUserName;
	}


	/**
	 * @return
	 * @uml.property  name="changeSetID"
	 */
	public int getChangeSetID() {
		return changeSetID;
	}


	/**
	 * @return
	 * @uml.property  name="externalSystemID"
	 */
	public int getExternalSystemID() {
		return externalSystemID;
	}


	/**
	 * @return
	 * @uml.property  name="comment"
	 */
	public String getComment() {
		return comment;
	}


	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		users.add(owner);
		return users;
	}


	@Override
	public String toString() {
		return "Changeset"+revision+" "+super.toString();
	}


	public int compareTo(IArtifact o) {
		if(o instanceof ChangeSet) {
			if(this.equals((ChangeSet)o)) {
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

	public void setAuthorForChangeSetDetails() {
		for(ChangeSetDetail changeSetDetail : this.changesetDetails) {
			changeSetDetail.setOwner(this.owner);
		}
	}

}
