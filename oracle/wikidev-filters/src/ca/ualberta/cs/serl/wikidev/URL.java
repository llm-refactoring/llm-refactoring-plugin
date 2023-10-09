package ca.ualberta.cs.serl.wikidev;


/**
 * @author   User
 */
public class URL {
	
	/**
	 * @uml.property  name="url"
	 */
	private String url;
	/**
	 * @uml.property  name="host"
	 */
	private String host;
	/**
	 * @uml.property  name="entityType"
	 */
	private String entityType;
	/**
	 * @uml.property  name="projectName"
	 */
	private String projectName;

	
	/**
	 * @return
	 * @uml.property  name="host"
	 */
	public String getHost() {
		return host;
	}


	/**
	 * @param  host
	 * @uml.property  name="host"
	 */
	public void setHost(String host) {
		this.host = host;
	}


	/**
	 * @return
	 * @uml.property  name="url"
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param  url
	 * @uml.property  name="url"
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return
	 * @uml.property  name="entityType"
	 */
	public String getEntityType() {
		return entityType;
	}


	/**
	 * @param  entityType
	 * @uml.property  name="entityType"
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}


	/**
	 * @return
	 * @uml.property  name="projectName"
	 */
	public String getProjectName() {
		return projectName;
	}


	/**
	 * @param  projectName
	 * @uml.property  name="projectName"
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	

}
