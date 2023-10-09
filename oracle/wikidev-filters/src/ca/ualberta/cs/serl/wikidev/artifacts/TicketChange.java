package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;
import java.util.Date;

import ca.ualberta.cs.serl.wikidev.User;

/**
 * @author   User
 */
public class TicketChange {
	
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	/**
	 * @uml.property  name="project_id"
	 */
	private int project_id;
	/**
	 * @uml.property  name="ticket_id"
	 */
	private int ticket_id;
	/**
	 * @uml.property  name="project_ticket_id"
	 */
	private int project_ticket_id;
	/**
	 * @uml.property  name="timestamp"
	 */
	private Timestamp timestamp;
	/**
	 * @uml.property  name="fieldChanged"
	 */
	private String fieldChanged;
	/**
	 * @uml.property  name="externalAuthor"
	 * @uml.associationEnd  
	 */
	private User externalAuthor;
	/**
	 * @uml.property  name="externalAuthorUsername"
	 */
	private String externalAuthorUsername;
	/**
	 * @uml.property  name="oldValue"
	 */
	private String oldValue;
	/**
	 * @uml.property  name="newValue"
	 */
	private String newValue;
	
	

	public TicketChange(String externalAuthorUsername,
			String fieldChanged, int id, String newValue, String oldValue,
			int ticket_dr_id, int ticket_id, int ticket_project_name,
			Timestamp timestamp) {
		this.externalAuthorUsername = externalAuthorUsername;
		this.fieldChanged = fieldChanged;
		this.id = id;
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.project_ticket_id = ticket_dr_id;
		this.ticket_id = ticket_id;
		this.project_id = ticket_project_name;
		this.timestamp = timestamp;
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return
	 * @uml.property  name="project_id"
	 */
	public int getProject_id() {
		return project_id;
	}

	/**
	 * @param ticket_project_name
	 * @uml.property  name="project_id"
	 */
	public void setProject_id(int ticket_project_name) {
		this.project_id = ticket_project_name;
	}

	/**
	 * @return
	 * @uml.property  name="ticket_id"
	 */
	public int getTicket_id() {
		return ticket_id;
	}

	/**
	 * @param ticket_id
	 * @uml.property  name="ticket_id"
	 */
	public void setTicket_id(int ticket_id) {
		this.ticket_id = ticket_id;
	}

	/**
	 * @return
	 * @uml.property  name="project_ticket_id"
	 */
	public int getProject_ticket_id() {
		return project_ticket_id;
	}

	/**
	 * @param ticket_dr_id
	 * @uml.property  name="project_ticket_id"
	 */
	public void setProject_ticket_id(int ticket_dr_id) {
		this.project_ticket_id = ticket_dr_id;
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

	/**
	 * @return
	 * @uml.property  name="fieldChanged"
	 */
	public String getFieldChanged() {
		return fieldChanged;
	}

	/**
	 * @param fieldChanged
	 * @uml.property  name="fieldChanged"
	 */
	public void setFieldChanged(String fieldChanged) {
		this.fieldChanged = fieldChanged;
	}

	/**
	 * @return
	 * @uml.property  name="oldValue"
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue
	 * @uml.property  name="oldValue"
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return
	 * @uml.property  name="newValue"
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue
	 * @uml.property  name="newValue"
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return
	 * @uml.property  name="externalAuthor"
	 */
	public User getExternalAuthor() {
		return externalAuthor;
	}

	/**
	 * @param externalAuthorUsername
	 * @uml.property  name="externalAuthorUsername"
	 */
	public void setExternalAuthorUsername(String externalAuthorUsername) {
		this.externalAuthorUsername = externalAuthorUsername;
	}

	/**
	 * @return
	 * @uml.property  name="externalAuthorUsername"
	 */
	public String getExternalAuthorUsername() {
		return externalAuthorUsername;
	}

	/**
	 * @param  externalAuthor
	 * @uml.property  name="externalAuthor"
	 */
	public void setExternalAuthor(User externalAuthor) {
		this.externalAuthor = externalAuthor;
	}
	
	
	
	
}
