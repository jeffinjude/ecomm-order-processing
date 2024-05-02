package com.jeffinjude.orderprocessing.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffinjude.orderprocessing.entities.OrderDetails;
import com.jeffinjude.orderprocessing.entities.OrderDetailsInfo;
import com.jeffinjude.orderprocessing.services.OrderService;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
	
	@Value("${ecomm.order.teststatus}")
	private String testProp;
	
	@Autowired
	OrderService orderService;

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@GetMapping("test")
	public ResponseEntity<String> test() {	
		return ResponseEntity.ok("Order Processing Test status: " + testProp);
	}
	
	@PostMapping("add")
	public ResponseEntity<OrderDetails> addOrderDetails(@RequestBody OrderDetails orderDetails) {
		ResponseEntity<OrderDetails> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		try {
			OrderDetails createdOrder = orderService.addOrderService(orderDetails);
			log.info("Inside addOrderDetails. Created order: " + createdOrder.toString());
			responseEntity = new ResponseEntity<>(createdOrder, HttpStatus.OK);
		}
		catch(Exception e) {
			log.debug("Inside addOrderDetails. Exception: " + e.getMessage());
			orderDetails.setOrderStatus(e.getMessage());
			responseEntity = new ResponseEntity<>(orderDetails, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@PostMapping("update/status/{orderId}")
	public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") int orderId, @RequestBody String orderStatus) {
		ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		try {
			orderService.updateOrderStatusService(orderStatus, orderId);
			responseEntity = new ResponseEntity<>("Order status updated.", HttpStatus.OK);
		}
		catch(Exception e) {
			log.debug("Inside updateOrderStatus. Exception: " + e.getMessage());
			responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@PostMapping("cancel/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable("orderId") int orderId) {
		ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		try {
			orderService.cancelOrderStatusService(orderId);
			responseEntity = new ResponseEntity<>("Order status cancelled.", HttpStatus.OK);
		}
		catch(Exception e) {
			log.debug("Inside cancelOrder. Exception: " + e.getMessage());
			responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@GetMapping("list")
	public ResponseEntity<List<OrderDetailsInfo>> getAllOrders() {
		ResponseEntity<List<OrderDetailsInfo>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		try {
			List<OrderDetailsInfo> fetchedOrders = orderService.getAllOrdersService();
			log.info("Inside getAllOrders. Fetched orders: " + fetchedOrders.toString());
			responseEntity = new ResponseEntity<>(fetchedOrders, HttpStatus.OK);
		}
		catch(Exception e) {
			log.debug("Inside getAllOrders. Exception: " + e.getMessage());
		}
		
		return responseEntity;
	}
	
	@GetMapping("list/{status}")
	public ResponseEntity<List<OrderDetailsInfo>> getAllOrdersByStatus(@PathVariable("status") String status) {
		ResponseEntity<List<OrderDetailsInfo>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		try {
			List<OrderDetailsInfo> fetchedOrders = orderService.getOrderDetailsByStatusService(status);
			log.info("Inside getAllOrdersByStatus. Fetched orders: " + fetchedOrders.toString());
			responseEntity = new ResponseEntity<>(fetchedOrders, HttpStatus.OK);
		}
		catch(Exception e) {
			log.debug("Inside getAllOrdersByStatus. Exception: " + e.getMessage());
		}
		
		return responseEntity;
	}
	
	@GetMapping("details/{id}")
	public ResponseEntity<OrderDetailsInfo> getOrderDetails(@PathVariable("id") int id) {
		ResponseEntity<OrderDetailsInfo> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		try {
			Optional<OrderDetailsInfo> fetchedOrderDetails = orderService.getOrderDetailsService(id);
			log.info("Inside getOrderDetails. Fetched order details: " + fetchedOrderDetails.toString());
			if(fetchedOrderDetails.isPresent()) {
				responseEntity = new ResponseEntity<>(fetchedOrderDetails.get(), HttpStatus.OK);
			}
			else {
				responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
		}
		catch(Exception e) {
			log.debug("Inside getOrderDetails. Exception: " + e.getMessage());
		}
		
		return responseEntity;
	}
}