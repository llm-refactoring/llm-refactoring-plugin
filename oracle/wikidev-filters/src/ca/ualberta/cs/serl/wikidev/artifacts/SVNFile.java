package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.User;

public class SVNFile extends IArtifact{
	
	private String name;
	private boolean deleted;
	private int changes;
	private String type;
	private TreeSet<User> users;
	
	public SVNFile(String name) {
		this.name = name;
		this.deleted = false;
		this.changes = 1;
		this.users = new TreeSet<User>();
	}
	
	public TreeSet<User> getUsers() {
		return users;
	}

	public void addUser(User user) {
		users.add(user);
	}
	
	public void increaseChanges() {
		changes++;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getName() {
		return name;
	}

	public int getChanges() {
		return changes;
	}
	
	public boolean equals(Object o) {
		return this.name.equals(((SVNFile) o).name);
	}
	
	public String toString() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public void setType() {
		if(name.endsWith("txt") || name.endsWith("doc") || name.endsWith("docx") || name.endsWith("rtf") || name.endsWith("pdf")) {
			type = "Text";
		}
		else if(name.endsWith("jpeg") || name.endsWith("png") || name.endsWith("mpeg") || name.endsWith("avi") || name.endsWith("ppt")) {
			type = "Media";
		}
		else if(name.endsWith(".java") || name.endsWith(".php") || name.endsWith(".py") || name.endsWith(".c") || name.endsWith(".cpp") || name.endsWith(".as") || name.endsWith(".rb") || name.endsWith(".vb") || name.endsWith(".cgi") || name.endsWith(".js")) {
			type = "Source";
		}
		else if(name.endsWith("html")) {
			type = "Html";
		}
		else {
			type = "Misc";
		}
	}

	@Override
	public Set<User> getAssociatedUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public int compareTo(IArtifact arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLastModified(Timestamp timestamp) {
		lastModified = timestamp;
		
	}

	public void setLastEditor(User editor) {
		lastEditor = editor;
		
	}

}
