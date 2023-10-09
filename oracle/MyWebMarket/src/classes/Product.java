package classes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="Product")
public class Product implements Serializable{
	private Integer id;
	private String name;
	private Double price;
	private Integer supply;
	
	@Id
	@Column(name="ID_PRODUCT", nullable=false)
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

	@Column(name="PRICE", nullable=false)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice( Double preco ) {
		this.price = preco;
	}

	@Column(name="SUPPLY", nullable=false)
	public Integer getSupply() {
		return this.supply;
	}

	public void setSupply( Integer estoque ) {
		this.supply = estoque;
	}

	@Override
	public String toString() {
		return this.id + "\t" + this.name + "\t" + this.price + "\t" + this.supply;
	}
	
	
}
