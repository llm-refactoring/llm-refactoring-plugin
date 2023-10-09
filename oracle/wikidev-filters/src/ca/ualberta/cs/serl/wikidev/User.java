package ca.ualberta.cs.serl.wikidev;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;

/**
 * @author   User
 */
public class User implements Comparable<User> {
	
	public static final String ROLE_DEVELOPER = "Developer";
	public static final String ROLE_MANAGER = "Manager";
	
	/**
	 * @uml.property  name="userID"
	 */
	private int userID;
	/**
	 * @uml.property  name="userName"
	 */
	private String userName;
	/**
	 * @uml.property  name="userRealName"
	 */
	private String userRealName;
	/**
	 * @uml.property  name="projects"
	 */
	private ArrayList<Project> projects;
	/**
	 * @uml.property  name="relatedArtifacts"
	 * @uml.associationEnd  qualifier="artifact:ca.ualberta.cs.serl.wikidev.artifacts.IArtifact java.lang.Integer"
	 */
	private HashMap<IArtifact, Integer> relatedArtifacts;
	/**
	 * @uml.property  name="rolePerProject"
	 */
	private HashMap<Project, String> rolePerProject;
	/**
	 * @uml.property  name="aliases"
	 */
	private ArrayList<String> aliases;
	
	/**
	 * @uml.property  name="index"
	 */
	private int index;
	private Color color;
	
	public User(int userID, String userName, String userRealName) {
		this.userID = userID;
		this.userName = userName;
		this.userRealName = userRealName;
		this.relatedArtifacts = new HashMap<IArtifact, Integer>();
	}
	
	public User(String userName) {
		this.userID = 0;
		this.userName = userName;
		this.userRealName = "";
		this.relatedArtifacts = new HashMap<IArtifact, Integer>();
	}
	
	

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}



	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
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
	 * @uml.property  name="userID"
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * @return
	 * @uml.property  name="userName"
	 */
	public String getUserName() {
		return userName;
	}
	
	public void addRelatedArtifacts(IArtifact artifact, int weight) {
		relatedArtifacts.put(artifact, weight);
	}

	/**
	 * @return
	 * @uml.property  name="relatedArtifacts"
	 */
	public HashMap<IArtifact, Integer> getRelatedArtifacts() {
		return relatedArtifacts;
	}

	/**
	 * @return
	 * @uml.property  name="aliases"
	 */
	public ArrayList<String> getAliases() {
		aliases = new ArrayList<String>();
		aliases.add(userName);
		aliases.add(userRealName);
		String[] name = userRealName.split(" ");
		for (int i = 0; i < name.length; i++) {
			aliases.add(name[i]);
		}
		/*try {
			aliases.addAll(DataManager.getUserAliases(userID));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return aliases;
	}

	/**
	 * @param userRealName
	 * @uml.property  name="userRealName"
	 */
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	/**
	 * @return
	 * @uml.property  name="rolePerProject"
	 */
	public HashMap<Project, String> getRolePerProject() {
		return rolePerProject;
	}

	/**
	 * @param  rolePerProject
	 * @uml.property  name="rolePerProject"
	 */
	public void setRolePerProject(HashMap<Project, String> rolePerProject) {
		this.rolePerProject = rolePerProject;
	}
	
	
	public String toString() {
		return userName;
	}

	public int compareTo(User arg0) {
		if(this.equals(arg0)) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	
}
