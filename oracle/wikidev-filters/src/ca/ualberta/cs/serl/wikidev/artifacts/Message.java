package ca.ualberta.cs.serl.wikidev.artifacts;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.Project;
import ca.ualberta.cs.serl.wikidev.User;

/**
 * @author  User
 */
public class Message extends IArtifact {
	
	/**
	 * @uml.property  name="communicationID"
	 */
	private int communicationID;
	/**
	 * @uml.property  name="subject"
	 */
	private String subject;
	/**
	 * @uml.property  name="body"
	 */
	private String body;
	/**
	 * @uml.property  name="project_id"
	 */
	private int project_id;
	/**
	 * @uml.property  name="author"
	 */
	private String author;
	/**
	 * @uml.property  name="address"
	 */
	private String address;
	/**
	 * @uml.property  name="deleted"
	 */
	private int deleted;
	/**
	 * @uml.property  name="mid_header"
	 */
	private String mid_header;
	/**
	 * @uml.property  name="user_name"
	 */
	private String user_name;
	/**
	 * @uml.property  name="refid_header"
	 */
	private String refid_header;
	


	

	public String getRefid_header() {
		return refid_header;
	}


	/**
	 * @return
	 * @uml.property  name="project_id"
	 */
	public int getProject_id() {
		return project_id;
	}


	/**
	 * @param project_id
	 * @uml.property  name="project_id"
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}


	/**
	 * @return
	 * @uml.property  name="author"
	 */
	public String getAuthor() {
		return author;
	}


	/**
	 * @param author
	 * @uml.property  name="author"
	 */
	public void setAuthor(String author) {
		this.author = author;
	}


	/**
	 * @return
	 * @uml.property  name="address"
	 */
	public String getAddress() {
		return address;
	}


	/**
	 * @param address
	 * @uml.property  name="address"
	 */
	public void setAddress(String address) {
		this.address = address;
	}


	/**
	 * @return
	 * @uml.property  name="deleted"
	 */
	public int getDeleted() {
		return deleted;
	}


	/**
	 * @param deleted
	 * @uml.property  name="deleted"
	 */
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}


	/**
	 * @return
	 * @uml.property  name="mid_header"
	 */
	public String getMid_header() {
		return mid_header;
	}


	/**
	 * @param mid_header
	 * @uml.property  name="mid_header"
	 */
	public void setMid_header(String mid_header) {
		this.mid_header = mid_header;
	}


	/**
	 * @param communicationID
	 * @uml.property  name="communicationID"
	 */
	public void setCommunicationID(int communicationID) {
		this.communicationID = communicationID;
	}


	/**
	 * @param subject
	 * @uml.property  name="subject"
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


	/**
	 * @param body
	 * @uml.property  name="body"
	 */
	public void setBody(String body) {
		this.body = body;
	}


	public Message(String address, String author, String body,
			int communicationID, int deleted,
			String mid_header, int project_id, String subject, Timestamp timestamp, String user_name, String refid_header) {
		this.address = address;
		this.author = author;
		this.body = body;
		this.communicationID = communicationID;
		this.deleted = deleted;
		this.mid_header = mid_header;
		this.project_id = project_id;
		this.subject = subject;
		this.timestamp = timestamp;
		this.directlyRelatedArtifacts = new ArrayList<RelatedArtifact>();
		//this.indirectlyRelatedArtifacts = new HashMap<IArtifact, HashMap<IArtifact, Integer>>();
		this.user_name = user_name;
		this.refid_header = refid_header;
	}


	/**
	 * @return
	 * @uml.property  name="user_name"
	 */
	public String getUser_name() {
		return user_name;
	}


	/**
	 * @param user_name
	 * @uml.property  name="user_name"
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		users.add(owner);
		return users;
	}

	/**
	 * @return
	 * @uml.property  name="communicationID"
	 */
	public int getCommunicationID() {
		return communicationID;
	}

	/**
	 * @return
	 * @uml.property  name="subject"
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return
	 * @uml.property  name="body"
	 */
	public String getBody() {
		return body;
	}
	
	public void setLastEditorAndTimeLastModifiedBeforeDate(Timestamp timestamp) {
		lastModified = this.timestamp;
		lastEditor = this.owner;
	}
	
	public void setTimeCreatedBeforeDate(Timestamp timestamp) {
		created = this.timestamp;
	}


	
	public String toString() {
		/*String s = communicationType+" "+communicationID+" between ";
		for(int i=0; i<participants.size()-1; i++) {
			s += participants.get(i).toString()+", ";
		}
		s += participants.get(participants.size()-1).toString()+" Subject: "+subject;
		return s;*/
		return "Message"+this.communicationID+" "+super.toString();
	}


	public int compareTo(IArtifact o) {
		if(o instanceof Message) {
			if(this.equals((Message)o)) {
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
