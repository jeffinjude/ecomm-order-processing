package com.jeffinjude.orderprocessing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetails {
	
	@Id
    @Column(name = "order_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;
	
	@Column(name = "product_id", nullable = false)
	private int productId;
	
	@Column(name = "customer_id", nullable = false)
	private int customerId;
	
	@Column(name = "order_status", nullable = false)
	private String orderStatus;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "OrderDetails [orderId=" + orderId + ", productId=" + productId + ", customerId=" + customerId
				+ ", orderStatus=" + orderStatus + "]";
	}
	
}
