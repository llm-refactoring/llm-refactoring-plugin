package ca.ualberta.cs.serl.wikidev.artifacts;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.User;

public class Documentation extends IArtifact {

	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		users.add(owner);
		return users;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public int compareTo(IArtifact o) {
		if(o instanceof Documentation) {
			if(this.equals((Documentation)o)) {
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
