package ca.ualberta.cs.serl.wikidev.artifacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.User;

public class ChangeSetDetail extends IArtifact{
	
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	/**
	 * @uml.property  name="externalsystem_id"
	 */
	private int externalsystem_id;
	/**
	 * @uml.property  name="filename"
	 */
	private String filename;
	/**
	 * @uml.property  name="rev"
	 */
	private int rev;
	/**
	 * @uml.property  name="kind"
	 */
	private String kind;
	/**
	 * @uml.property  name="props"
	 */
	private String props;
	/**
	 * @uml.property  name="changetype"
	 */
	private String changetype;
	/**
	 * @uml.property  name="linesadded"
	 */
	private int linesadded;
	/**
	 * @uml.property  name="linesremoved"
	 */
	private int linesremoved;
	/**
	 * @uml.property  name="lineschanged"
	 */
	private int lineschanged;
	/**
	 * @uml.property  name="file"
	 */
	private String file;
	
	public ChangeSetDetail(String changetype, int externalsystem_id,
			String filename, int id, String kind, int linesadded,
			int lineschanged, int linesremoved, String props, int rev) {
		this.changetype = changetype;
		this.externalsystem_id = externalsystem_id;
		this.filename = filename;
		this.id = id;
		this.kind = kind;
		this.linesadded = linesadded;
		this.lineschanged = lineschanged;
		this.linesremoved = linesremoved;
		this.props = props;
		this.rev = rev;
		this.file = setFile(filename);
		this.directlyRelatedArtifacts = new ArrayList<RelatedArtifact>();
		//this.indirectlyRelatedArtifacts = new HashMap<IArtifact, HashMap<IArtifact, Integer>>();
	}

	/**
	 * @return
	 * @uml.property  name="file"
	 */
	public String getFile() {
		return file;
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
	 * @uml.property  name="externalsystem_id"
	 */
	public int getExternalsystem_id() {
		return externalsystem_id;
	}

	/**
	 * @param externalsystem_id
	 * @uml.property  name="externalsystem_id"
	 */
	public void setExternalsystem_id(int externalsystem_id) {
		this.externalsystem_id = externalsystem_id;
	}

	/**
	 * @return
	 * @uml.property  name="filename"
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 * @uml.property  name="filename"
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return
	 * @uml.property  name="rev"
	 */
	public int getRev() {
		return rev;
	}

	/**
	 * @param rev
	 * @uml.property  name="rev"
	 */
	public void setRev(int rev) {
		this.rev = rev;
	}

	/**
	 * @return
	 * @uml.property  name="kind"
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind
	 * @uml.property  name="kind"
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @return
	 * @uml.property  name="props"
	 */
	public String getProps() {
		return props;
	}

	/**
	 * @param props
	 * @uml.property  name="props"
	 */
	public void setProps(String props) {
		this.props = props;
	}

	/**
	 * @return
	 * @uml.property  name="changetype"
	 */
	public String getChangetype() {
		return changetype;
	}

	/**
	 * @param changetype
	 * @uml.property  name="changetype"
	 */
	public void setChangetype(String changetype) {
		this.changetype = changetype;
	}

	/**
	 * @return
	 * @uml.property  name="linesadded"
	 */
	public int getLinesadded() {
		return linesadded;
	}

	/**
	 * @param linesadded
	 * @uml.property  name="linesadded"
	 */
	public void setLinesadded(int linesadded) {
		this.linesadded = linesadded;
	}

	/**
	 * @return
	 * @uml.property  name="linesremoved"
	 */
	public int getLinesremoved() {
		return linesremoved;
	}

	/**
	 * @param linesremoved
	 * @uml.property  name="linesremoved"
	 */
	public void setLinesremoved(int linesremoved) {
		this.linesremoved = linesremoved;
	}

	/**
	 * @return
	 * @uml.property  name="lineschanged"
	 */
	public int getLineschanged() {
		return lineschanged;
	}

	/**
	 * @param lineschanged
	 * @uml.property  name="lineschanged"
	 */
	public void setLineschanged(int lineschanged) {
		this.lineschanged = lineschanged;
	}

	@Override
	public Set<User> getAssociatedUsers() {
		Set<User> users = new TreeSet<User>();
		users.add(owner);
		return users;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "changesetdetail"+id+" "+super.toString();
	}

	public int compareTo(IArtifact o) {
		if(o instanceof ChangeSetDetail) {
			if(this.equals((ChangeSetDetail)o)) {
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

	private String setFile(String filename) {
		int index = filename.lastIndexOf('/');
		return filename.substring(index+1);
	}
	
	

}
