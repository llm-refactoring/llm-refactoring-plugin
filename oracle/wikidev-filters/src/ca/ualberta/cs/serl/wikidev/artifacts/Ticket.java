package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.Project;
import ca.ualberta.cs.serl.wikidev.User;

/**
 * @author   User
 */
public class Ticket extends IArtifact {
	
	public static final String TICKET_TYPE_TASK = "task";
	public static final String TICKET_TYPE_ENHANCEMENT = "enhancement";
	public static final String TICKET_TYPE_DEFECT = "defect";
	
	public static final String TICKET_PRIORITY_LOW = "low";
	public static final String TICKET_PRIORITY_MEDIUM = "medium";
	public static final String TICKET_PRIOhRITY_HIGH = "high";
	
	public static final String TICKET_STATUS_REJECTED = "rejected";
	public static final String TICKET_STATUS_OPEN = "open";
	public static final String TICKET_STATUS_COMPLETED = "completed";
	

	/**
	 * @uml.property  name="ticketID"
	 */
	private int ticketID;
	//private int externalSystemID;
	/**
	 * @uml.property  name="reporter"
	 * @uml.associationEnd  
	 */
	private User reporter;
	//private String type;
	/**
	 * @uml.property  name="summary"
	 */
	private String summary;
	/**
	 * @uml.property  name="desription"
	 */
	private String desription;
	//private String priority;
	//private String status;
	/**
	 * @uml.property  name="ticketChanges"
	 */
	private ArrayList<TicketChange> ticketChanges;
	/**
	 * @uml.property  name="relatedUsers"
	 */
	private ArrayList<User> relatedUsers;
	/**
	 * @uml.property  name="dateOpened"
	 */
	private Timestamp dateOpened;
	/**
	 * @uml.property  name="ownerName"
	 */
	private String ownerName;
	/**
	 * @uml.property  name="reporterName"
	 */
	private String reporterName;
	/**
	 * @uml.property  name="project"
	 */
	private int project;
	/**
	 * @uml.property  name="project_ticket_id"
	 */
	private int project_ticket_id;
	private String priority;
	
	
	public Ticket(Timestamp dateOpened, String desription, Timestamp lastModified, String ownerName, 
			String reporterName, String summary,
			int ticketID, int project, int dr_id, String priority) {
		this.dateOpened = dateOpened;
		this.desription = desription;
		this.timestamp = lastModified;
		this.ownerName = ownerName;
		this.reporterName = reporterName;
		this.summary = summary;
		this.ticketID = ticketID;
		this.directlyRelatedArtifacts = new ArrayList<RelatedArtifact>();
		//this.indirectlyRelatedArtifacts = new HashMap<IArtifact, HashMap<IArtifact, Integer>>();
		this.project = project;
		this.project_ticket_id = dr_id;
		this.priority = priority;
	}
	
	public int getPriority() {
		return Integer.parseInt(""+priority.charAt(1));
	}


	public int getDr_id() {
		return project_ticket_id;
	}


	public void setDr_id(int dr_id) {
		this.project_ticket_id = dr_id;
	}


	public int getProjectID() {
		return project;
	}


	public void setProject_name(int project) {
		this.project = project;
	}


	/**
	 * @return
	 * @uml.property  name="ownerName"
	 */
	public String getOwnerName() {
		return ownerName;
	}


	/**
	 * @param ownerName
	 * @uml.property  name="ownerName"
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	/**
	 * @return
	 * @uml.property  name="reporterName"
	 */
	public String getReporterName() {
		return reporterName;
	}


	/**
	 * @param reporterName
	 * @uml.property  name="reporterName"
	 */
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}


	/**
	 * @return
	 * @uml.property  name="ticketID"
	 */
	public int getTicketID() {
		return ticketID;
	}



	/**
	 * @return
	 * @uml.property  name="summary"
	 */
	public String getSummary() {
		return summary;
	}


	/**
	 * @return
	 * @uml.property  name="desription"
	 */
	public String getDesription() {
		return desription;
	}

	/**
	 * @return
	 * @uml.property  name="ticketChanges"
	 */
	public ArrayList<TicketChange> getTicketChanges() {
		return ticketChanges;
	}


	/**
	 * @param  ticketChanges
	 * @uml.property  name="ticketChanges"
	 */
	public void setTicketChanges(ArrayList<TicketChange> ticketChanges) {
		this.ticketChanges = ticketChanges;
	}


	/**
	 * @return
	 * @uml.property  name="relatedUsers"
	 */
	public ArrayList<User> getRelatedUsers() {
		return relatedUsers;
	}


	/**
	 * @return
	 * @uml.property  name="dateOpened"
	 */
	public Timestamp getDateOpened() {
		return dateOpened;
	}


	/**
	 * @return
	 * @uml.property  name="reporter"
	 */
	public User getReporter() {
		return reporter;
	}


	/**
	 * @param  reporter
	 * @uml.property  name="reporter"
	 */
	public void setReporter(User reporter) {
		this.reporter = reporter;
	}
	
	public String getLastPriorityBeforeDate(Timestamp timestamp) {
		String p = "";
		for(TicketChange change : this.ticketChanges) {
			if(change.getTimestamp().before(timestamp) && change.getFieldChanged().equals("priority")) {
				p = change.getNewValue();
			}
		}
		if(p.equals("")) {
			p = priority;
		}
		return p;
	}
	
	public int getNumberOfChangesBeforeDate(Timestamp timestamp) {
		int count = 0;
		for(TicketChange change : ticketChanges) {
			if(change.getTimestamp().before(timestamp)) {
				count++;
			}
		}
		return count;
	}
	
	public void setLastEditorAndTimeLastModifiedBeforeDate(Timestamp timestamp) {
		lastModified = created;
		lastEditor = owner;
		for(TicketChange change : ticketChanges) {
			if(change.getTimestamp().before(timestamp) && change.getTimestamp().after(lastModified)) {
				lastModified = change.getTimestamp();
				lastEditor = change.getExternalAuthor();
			}
		}
	}
	
	public void setTimeCreate(Timestamp timestamp) {
		created = dateOpened;
	}

	public HashMap<User, Double> getUserContributionBeforeDate(Timestamp timestamp, ArrayList<User> users) {
		HashMap<User, Double> userChanges = new HashMap<User, Double>();
		for(User user : users) {
			if(user.equals(owner) || user.equals(reporter)) {
				userChanges.put(user, 1.0);
			}
			else {
				userChanges.put(user, 0.0);
			}
		}
		for(TicketChange change : ticketChanges) {
			if(change.getTimestamp().before(timestamp)) {
				userChanges.put(change.getExternalAuthor(), userChanges.get(change.getExternalAuthor())+1.0);
			}
		}
		if (getNumberOfChangesBeforeDate(timestamp) != 0) {
			for (User user : userChanges.keySet()) {
				userChanges.put(user, userChanges.get(user)
						/ ((double) getNumberOfChangesBeforeDate(timestamp)+1.0));
			}
		}
		return userChanges;
	}

	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		users.add(owner);
		users.add(reporter);
		for(TicketChange changes : ticketChanges) {
			users.add(changes.getExternalAuthor());
		}
		return users;
	}
	
	
	public String toString() {
		return "Ticket"+ project_ticket_id+" "+super.toString();
	}


	public int compareTo(IArtifact o) {
		if(o instanceof Ticket) {
			if(this.equals((Ticket)o)) {
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
