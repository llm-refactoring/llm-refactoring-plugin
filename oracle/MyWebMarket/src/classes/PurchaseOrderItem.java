package classes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

@Entity(name="PURCHASE_ORDER_ITEM")
public class PurchaseOrderItem implements Serializable{
	private PurchaseOrder purchaseOrder;
	private Integer id;
	private Product product;
	private Double price;
	private Integer quantity;
	
	@NaturalId
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PURCHASE_ORDER",nullable=false)
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public void setPurchaseOrder( PurchaseOrder purchaseOrder ) {
		this.purchaseOrder = purchaseOrder;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name="ID_PURCHASE_ORDER_ITEM", nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId( Integer id ) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PRODUCT",nullable=false)
	public Product getProduct() {
		return product;
	}
	public void setProduct( Product product ) {
		this.product = product;
	}
	
	@Column(name="PRICE", nullable=false)
	public Double getPrice() {
		return price;
	}
	public void setPrice( Double price ) {
		this.price = price;
	}
	
	@Column(name="QUANTITY", nullable=false)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity( Integer quantity ) {
		this.quantity = quantity;
	}
	
}
