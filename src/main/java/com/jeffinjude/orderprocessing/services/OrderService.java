package com.jeffinjude.orderprocessing.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.jeffinjude.orderprocessing.controllers.OrderController;
import com.jeffinjude.orderprocessing.entities.OrderDetails;
import com.jeffinjude.orderprocessing.entities.OrderDetailsInfo;
import com.jeffinjude.orderprocessing.entities.OrderStatus;
import com.jeffinjude.orderprocessing.entities.Product;
import com.jeffinjude.orderprocessing.entities.Customer;
import com.jeffinjude.orderprocessing.entities.InventoryDetails;
import com.jeffinjude.orderprocessing.repositories.OrderRepository;

@Service
public class OrderService {

	private final WebClient webClient;

	
	public OrderService(WebClient webClient) {
		this.webClient = webClient;
	}
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	OrderRepository orderRepository;

	public OrderDetails addOrderService(OrderDetails orderDetails) throws Exception {

		Optional<Product> fetchedProduct = Optional.ofNullable(webClient.get().uri("/ecomm-product-catalogue/api/v1/product/details/"+orderDetails.getProductId()).retrieve()
	      .bodyToMono(Product.class).block());
		log.info("Inside addOrderService. fetchedProduct: " + fetchedProduct.toString());
		
		Optional<Customer> fetchedCustomer = Optional.ofNullable(webClient.get().uri("/ecomm-customer/api/v1/customer/details/"+orderDetails.getCustomerId()).retrieve()
			      .bodyToMono(Customer.class).block());
		log.info("Inside addOrderService. fetchedCustomer: " + fetchedCustomer.toString());
		
		Optional<InventoryDetails> fetchedInventoryDetails = Optional.ofNullable(webClient.get().uri("/ecomm-inventory/api/v1/inventory/details/"+orderDetails.getProductId()).retrieve()
			      .bodyToMono(InventoryDetails.class).block());
		log.info("Inside addOrderService. fetchedInventoryDetails: " + fetchedInventoryDetails.toString());
		
		orderDetails.setOrderStatus(OrderStatus.PENDING.name());
		
		if(fetchedProduct.isPresent() && fetchedCustomer.isPresent()) {
			if(fetchedInventoryDetails.isPresent() && fetchedInventoryDetails.get().getProductQuantity() > 0) {
				return orderRepository.save(orderDetails);
			}
			else {
				throw new Exception("Insufficient product inventory.");
			}
		}
		else {
			throw new Exception("Invalid Product or Customer id.");
		}
	}
	
	public List<OrderDetailsInfo> getAllOrdersService() {
		List<OrderDetails> orderDetailsList = orderRepository.findAll();
		List<OrderDetailsInfo> orderDetailsInfoList = new ArrayList<OrderDetailsInfo>();
		orderDetailsList.forEach(order -> {
			orderDetailsInfoList.add(getOrderDetailsInfo(order.getCustomerId(), order.getProductId(), order.getOrderStatus()));
		});
		return orderDetailsInfoList;
	}
	
	public Optional<OrderDetailsInfo> getOrderDetailsService(int orderId) {
		Optional<OrderDetails> orderDetails = orderRepository.findById(orderId);
		Optional<OrderDetailsInfo> orderDetailsInfo = Optional.ofNullable(null);
		if(orderDetails.isPresent()) {
			orderDetailsInfo = Optional.ofNullable(getOrderDetailsInfo(orderDetails.get().getCustomerId(), orderDetails.get().getProductId(), orderDetails.get().getOrderStatus()));
		}
		return orderDetailsInfo;
	}
	
	public List<OrderDetailsInfo> getOrderDetailsByStatusService(String orderStatus) {
		List<OrderDetails> orderDetailsList = orderRepository.findByOrderStatus(orderStatus);
		List<OrderDetailsInfo> orderDetailsInfoList = new ArrayList<OrderDetailsInfo>();
		orderDetailsList.forEach(order -> {
			orderDetailsInfoList.add(getOrderDetailsInfo(order.getCustomerId(), order.getProductId(), order.getOrderStatus()));
		});
		return orderDetailsInfoList;
	}
	
	@Transactional
	public void updateOrderStatusService(String orderStatus, int orderId) throws Exception {
		if(getOrderDetailsService(orderId).isPresent()) {
			if(Arrays.stream(OrderStatus.values()).anyMatch((status) -> status.name().equals(orderStatus.toUpperCase()))) {
				orderRepository.updateOrderStatus(orderStatus.toUpperCase(), orderId);
			}
			else {
				throw new Exception("Invalid order status.");
			}
		}
		else {
			throw new Exception("Invalid order id.");
		}
	}
	
	@Transactional
	public void cancelOrderStatusService(int orderId) throws Exception {
		if(getOrderDetailsService(orderId).isPresent()) {
			try {
				orderRepository.updateOrderStatus(OrderStatus.CANCELLED.name(), orderId);
				//TODO: Make a call to payment microservice to reverse payment.
				
			}
			catch(Exception e) {
				log.debug("Inside cancelOrderStatusService. Exception: " + e.getMessage());
				throw new Exception("Order cancellation failed.");
			}
		}
		else {
			throw new Exception("Invalid order id.");
		}
	}
	
	private OrderDetailsInfo getOrderDetailsInfo(int customerId, int productId, String orderStatus) {
		Optional<Product> fetchedProduct = Optional.ofNullable(webClient.get().uri("/ecomm-product-catalogue/api/v1/product/details/"+productId).retrieve()
	      .bodyToMono(Product.class).block());
		log.info("Inside getOrderDetailsInfo. fetchedProduct: " + fetchedProduct.toString());
		
		Optional<Customer> fetchedCustomer = Optional.ofNullable(webClient.get().uri("/ecomm-customer/api/v1/customer/details/"+customerId).retrieve()
			      .bodyToMono(Customer.class).block());
		log.info("Inside getOrderDetailsInfo. fetchedCustomer: " + fetchedCustomer.toString());
		
		OrderDetailsInfo orderDetailsInfo = new OrderDetailsInfo();
		
		if(fetchedProduct.isPresent()) {
			orderDetailsInfo.setProduct(fetchedProduct.get());
		}
		
		if(fetchedCustomer.isPresent()) {
			orderDetailsInfo.setCustomer(fetchedCustomer.get());
		}
		
		if(!orderStatus.isBlank()) {
			orderDetailsInfo.setOrderStatus(orderStatus);
		}
		
		return orderDetailsInfo;
	}
}
