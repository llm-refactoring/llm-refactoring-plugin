package classes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="Customer")
public class Customer implements Serializable{
	private Integer id;
	private String name;
	private String address;
	private Long phone;
	private String email;
	
	@Id
	@Column(name="ID_CUSTOMER", nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	@Column(name="NAME", length=60, nullable=false)
	public String getName() {
		return this.name;
	}

	public void setName( String nome ) {
		this.name = nome;
	}

	@Column(name="ADDRESS", length=60, nullable=false)
	public String getAddress() {
		return address;
	}

	public void setAddress( String address ) {
		this.address = address;
	}

	@Column(name="PHONE", nullable=false)
	public Long getPhone() {
		return phone;
	}

	public void setPhone( Long phone ) {
		this.phone = phone;
	}

	@Column(name="EMAIL", length=60, nullable=false)
	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	@Override
	public String toString() {
		return this.id + "\t" + this.name + "\t" + this.address + "\t" + this.phone + "\t" + this.email;
	}
	
	
}
