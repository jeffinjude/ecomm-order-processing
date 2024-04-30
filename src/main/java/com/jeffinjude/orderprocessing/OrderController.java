package com.jeffinjude.orderprocessing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
	
	@Value("${ecomm.order.teststatus}")
	private String testProp;

	
	@GetMapping("test")
	public ResponseEntity<String> test() {	
		return ResponseEntity.ok("Order Processing Test status: " + testProp);
	}
}
