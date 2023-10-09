package ca.ualberta.cs.serl.wikidev.artifacts;

import java.sql.Timestamp;

import ca.ualberta.cs.serl.wikidev.User;

public class WikiRevision {
	
	/**
	 * @uml.property  name="rev_id"
	 */
	private int rev_id;
	/**
	 * @uml.property  name="rev_page"
	 */
	private int rev_page;
	/**
	 * @uml.property  name="rev_text_id"
	 */
	private int rev_text_id;
	/**
	 * @uml.property  name="rev_text"
	 */
	private String rev_text;
	/**
	 * @uml.property  name="rev_user_text"
	 */
	private String rev_user_text;
	/**
	 * @uml.property  name="rev_user"
	 * @uml.associationEnd  
	 */
	private User rev_user;
	/**
	 * @uml.property  name="rev_timestamp"
	 */
	private Timestamp rev_timestamp;
	
	
	public WikiRevision(int rev_id, int rev_page, int rev_text_id,
			String rev_user_text, Timestamp rev_timestamp) {
		this.rev_id = rev_id;
		this.rev_page = rev_page;
		this.rev_text_id = rev_text_id;
		this.rev_user_text = rev_user_text;
		this.rev_timestamp = rev_timestamp;
	}


	/**
	 * @return
	 * @uml.property  name="rev_id"
	 */
	public int getRev_id() {
		return rev_id;
	}


	/**
	 * @param rev_id
	 * @uml.property  name="rev_id"
	 */
	public void setRev_id(int rev_id) {
		this.rev_id = rev_id;
	}


	/**
	 * @return
	 * @uml.property  name="rev_page"
	 */
	public int getRev_page() {
		return rev_page;
	}


	/**
	 * @param rev_page
	 * @uml.property  name="rev_page"
	 */
	public void setRev_page(int rev_page) {
		this.rev_page = rev_page;
	}


	/**
	 * @return
	 * @uml.property  name="rev_text_id"
	 */
	public int getRev_text_id() {
		return rev_text_id;
	}


	/**
	 * @param rev_text_id
	 * @uml.property  name="rev_text_id"
	 */
	public void setRev_text_id(int rev_text_id) {
		this.rev_text_id = rev_text_id;
	}


	/**
	 * @return
	 * @uml.property  name="rev_user_text"
	 */
	public String getRev_user_text() {
		return rev_user_text;
	}


	/**
	 * @param rev_user_text
	 * @uml.property  name="rev_user_text"
	 */
	public void setRev_user_text(String rev_user_text) {
		this.rev_user_text = rev_user_text;
	}


	/**
	 * @return
	 * @uml.property  name="rev_user"
	 */
	public User getRev_user() {
		return rev_user;
	}


	/**
	 * @param rev_user
	 * @uml.property  name="rev_user"
	 */
	public void setRev_user(User rev_user) {
		this.rev_user = rev_user;
	}


	/**
	 * @return
	 * @uml.property  name="rev_timestamp"
	 */
	public Timestamp getRev_timestamp() {
		return rev_timestamp;
	}


	/**
	 * @param rev_timestamp
	 * @uml.property  name="rev_timestamp"
	 */
	public void setRev_timestamp(Timestamp rev_timestamp) {
		this.rev_timestamp = rev_timestamp;
	}


	/**
	 * @return
	 * @uml.property  name="rev_text"
	 */
	public String getRev_text() {
		return rev_text;
	}


	/**
	 * @param rev_text
	 * @uml.property  name="rev_text"
	 */
	public void setRev_text(String rev_text) {
		this.rev_text = rev_text;
	}
	
	
	
	

}
