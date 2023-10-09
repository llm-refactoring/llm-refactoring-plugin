package classes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "User")
@org.hibernate.annotations.Entity(mutable=false)
public class User implements Serializable {
	private String username;
	private String password;

	public User() {
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Id
	@Column(name = "USERNAME", length = 10, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	@Column(name = "PASSWORD", length = 10, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

}
