package classes;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="PURCHASE_ORDER")
public class PurchaseOrder implements Serializable{
	private Integer id;
	private Customer customer;
	private Date orderDate;
	private Double discount;
	private Collection<PurchaseOrderItem> purchaseOrderItems;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")	
	@Column(name="ID_PURCHASE_ORDER", nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_CUSTOMER",nullable=false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer( Customer customer ) {
		this.customer = customer;
	}

	@Column(name="ORDER_DATE", nullable=false)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate( Date orderDate ) {
		this.orderDate = orderDate;
	}

	@Column(name="DISCOUNT", nullable=false)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount( Double discount ) {
		this.discount = discount;
	}

	@OneToMany(mappedBy="purchaseOrder",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	public Collection<PurchaseOrderItem> getPurchaseOrderItems() {
		return purchaseOrderItems;
	}
	
	public void setPurchaseOrderItems(
			Collection<PurchaseOrderItem> purchaseOrderItems ) {
		this.purchaseOrderItems = purchaseOrderItems;
	}
	
	
	@Override
	public String toString() {
		return this.id + "\t" + this.customer.getName() + "\t" + this.orderDate + "\t" + this.discount;
	}
	
	
}
