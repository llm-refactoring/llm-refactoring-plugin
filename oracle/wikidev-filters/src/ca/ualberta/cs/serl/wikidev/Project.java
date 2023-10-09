package ca.ualberta.cs.serl.wikidev;

import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;

/**
 * @author   User
 */
public class Project implements Comparable<Project>{
	
	/**
	 * @uml.property  name="projectID"
	 */
	private int projectID;
	/**
	 * @uml.property  name="projectName"
	 */
	private String projectName;
	/**
	 * @uml.property  name="developers"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.User"
	 */
	private Set<User> developers;
	/**
	 * @uml.property  name="managers"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.User"
	 */
	private Set<User> managers;
	/**
	 * @uml.property  name="users"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.User"
	 */
	private Set<User> users;
	/**
	 * @uml.property  name="relatedArtifacts"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="project:ca.ualberta.cs.serl.wikidev.artifacts.IArtifact"
	 */
	private Set<IArtifact> relatedArtifacts;
	/**
	 * @uml.property  name="classNames"
	 */
	private Set<String> classNames;
	/**
	 * @uml.property  name="wikis"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="project:ca.ualberta.cs.serl.wikidev.artifacts.Wiki"
	 */
	private Set<Wiki> wikis;
	/**
	 * @uml.property  name="tickets"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.artifacts.Ticket"
	 */
	private Set<Ticket> tickets;
	/**
	 * @uml.property  name="messages"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.artifacts.Message"
	 */
	private Set<Message> messages;
	/**
	 * @uml.property  name="changesets"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet"
	 */
	private Set<ChangeSet> changesets;
	/**
	 * @uml.property  name="system"
	 */
	private int system;
	
	
	public Project(int projectID, String projectName) {
		this.projectID = projectID;
		this.projectName = projectName;
		this.classNames = new HashSet<String>();
		this.developers = new HashSet<User>();
		this.managers = new HashSet<User>();
		this.relatedArtifacts = new HashSet<IArtifact>();
		this.wikis = new HashSet<Wiki>();
		this.tickets = new HashSet<Ticket>();
		this.messages = new HashSet<Message>();
		this.changesets = new HashSet<ChangeSet>();
		this.users = new HashSet<User>();
	}

	/**
	 * @return
	 * @uml.property  name="system"
	 */
	public int getSystem() {
		return system;
	}

	/**
	 * @param system
	 * @uml.property  name="system"
	 */
	public void setSystem(int system) {
		this.system = system;
	}

	public void addUser(User user) {
		this.users.add(user);
	}
	
	public Set<Wiki> getWikis() {
		return wikis;
	}

	public void setWikis(Set<Wiki> wikis) {
		this.wikis = wikis;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public Set<ChangeSet> getChangesets() {
		return changesets;
	}

	public void setChangesets(Set<ChangeSet> changesets) {
		this.changesets = changesets;
	}

	public void addWiki(Wiki wiki) {
		this.wikis.add(wiki);
	}
	
	public void addTicket(Ticket ticket) {
		this.tickets.add(ticket);
	}
	
	public void addMessage(Message message) {
		this.messages.add(message);
	}
	
	public void addChangeset(ChangeSet changeset) {
		this.changesets.add(changeset);
	}

	/**
	 * @return
	 * @uml.property  name="projectID"
	 */
	public int getProjectID() {
		return projectID;
	}


	/**
	 * @return
	 * @uml.property  name="projectName"
	 */
	public String getProjectName() {
		return projectName;
	}
	
	public void addRelatedArtifact(IArtifact artifact) {
		relatedArtifacts.add(artifact);
	}


	/**
	 * @return
	 * @uml.property  name="classNames"
	 */
	public Set<String> getClassNames() {
		return classNames;
	}


	/**
	 * @param  classNames
	 * @uml.property  name="classNames"
	 */
	public void setClassNames(Set<String> classNames) {
		this.classNames = classNames;
	}

	public void addDeveloper(User developer) {
		this.developers.add(developer);
	}

	/**
	 * @return
	 * @uml.property  name="developers"
	 */
	public Set<User> getDevelopers() {
		return developers;
	}

	public void addManager(User manager) {
		this.managers.add(manager);
	}

	/**
	 * @return
	 * @uml.property  name="managers"
	 */
	public Set<User> getManagers() {
		return managers;
	}
	
	/**
	 * @return
	 * @uml.property  name="users"
	 */
	public Set<User> getUsers() {
		return users;
	}


	/**
	 * @param  users
	 * @uml.property  name="users"
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}


	@Override
	/*public int compareTo(Project o) {
		if(this.equals(o)) {
			return 0;
		}
		else {
			return 1;
		}
	}*/
	
	public String toString() {
		return projectName;
	}


	public int compareTo(Project o) {
		if(this.equals(o)) {
			return 0;
		}
		else {
			return 1;
		}
	}

}
