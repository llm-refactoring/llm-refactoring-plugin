package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.DataManager;
import ca.ualberta.cs.serl.wikidev.Project;
import ca.ualberta.cs.serl.wikidev.User;


/**
 * @author   User
 */
public class Wiki extends IArtifact {
	
	/**
	 * @uml.property  name="page_id"
	 */
	private int page_id;
	/**
	 * @uml.property  name="page_namespace_id"
	 */
	private int page_namespace_id;
	/**
	 * @uml.property  name="project"
	 * @uml.associationEnd  inverse="wikis:ca.ualberta.cs.serl.wikidev.Project"
	 */
	private int project;
	/**
	 * @uml.property  name="page_title"
	 */
	private String page_title;
	/**
	 * @uml.property  name="revisions"
	 */
	private ArrayList<WikiRevision> revisions;
	
	
	
	

	public Wiki(int page_id, int page_namespace_id, String page_title, Timestamp timestamp) {
		this.page_id = page_id;
		this.page_namespace_id = page_namespace_id;
		this.page_title = page_title;
		this.timestamp = timestamp;
		this.directlyRelatedArtifacts = new ArrayList<RelatedArtifact>();
		this.revisions = new ArrayList<WikiRevision>();
	}

	
	
	/**
	 * @return
	 * @uml.property  name="page_id"
	 */
	public int getPage_id() {
		return page_id;
	}



	/**
	 * @param page_id
	 * @uml.property  name="page_id"
	 */
	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}



	/**
	 * @return
	 * @uml.property  name="page_namespace_id"
	 */
	public int getPage_namespace_id() {
		return page_namespace_id;
	}



	/**
	 * @param page_namespace_id
	 * @uml.property  name="page_namespace_id"
	 */
	public void setPage_namespace_id(int page_namespace_id) {
		this.page_namespace_id = page_namespace_id;
	}



	/**
	 * @return
	 * @uml.property  name="project"
	 */
	public int getProjectID() {
		return project;
	}



	/**
	 * @param project
	 * @uml.property  name="project"
	 */
	public void setProjectID(int project) {
		this.project = project;
	}



	/**
	 * @return
	 * @uml.property  name="page_title"
	 */
	public String getPage_title() {
		return page_title;
	}

	/**
	 * @param page_title
	 * @uml.property  name="page_title"
	 */
	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}



	public ArrayList<WikiRevision> getRevisions() {
		return revisions;
	}

	public void setRevisions(ArrayList<WikiRevision> revisions) {
		this.revisions = revisions;
	}
	
	public int getNumberOfRevisionsBeforeDate(Timestamp timestamp) {
		int count = 0;
		for(WikiRevision rev : revisions) {
			if(rev.getRev_timestamp().before(timestamp)) {
				count++;
			}
		}
		return count;
	}
	
	public void setLastEditorAndTimeLastModifiedBeforeDate(Timestamp timestamp) {
		lastModified = created;
		lastEditor = owner;
		for(WikiRevision rev : revisions) {
			if(rev.getRev_timestamp().before(timestamp) && rev.getRev_timestamp().after(lastModified)) {
				lastModified = rev.getRev_timestamp();
				lastEditor = rev.getRev_user();
			}
		}
	}
	
	public void setTimeCreatedBeforeDate(Timestamp timestamp) {
		created = Timestamp.valueOf("2300-01-01 00:00:00");
		for(WikiRevision rev : revisions) {
			if(rev.getRev_timestamp().before(timestamp) && rev.getRev_timestamp().before(this.getCreated())) {
				created = rev.getRev_timestamp();
			}
		}
	}

	public HashMap<User, Double> getUserContributionBeforeDate(Timestamp timestamp, ArrayList<User> users) {
		HashMap<User, Double> userChanges = new HashMap<User, Double>();
		for(User user : users) {
			userChanges.put(user, 0.0);
		}
		for(WikiRevision rev : revisions) {
			if(rev.getRev_timestamp().before(timestamp)) {
				userChanges.put(rev.getRev_user(), userChanges.get(rev.getRev_user())+1.0);
			}
		}
		if (getNumberOfRevisionsBeforeDate(timestamp) != 0) {
			for (User user : userChanges.keySet()) {
				userChanges.put(user, userChanges.get(user)
						/ (double) getNumberOfRevisionsBeforeDate(timestamp));
			}
		}
		return userChanges;
	}
	

	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		for(WikiRevision rev : revisions) {
			users.add(rev.getRev_user());
		}
		return users;
	}

	@Override
	public String toString() {
		return "Wiki"+page_id+" "+super.toString();
	}

	public int compareTo(IArtifact o) {
		if(o instanceof Wiki) {
			if(this.equals((Wiki)o)) {
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
