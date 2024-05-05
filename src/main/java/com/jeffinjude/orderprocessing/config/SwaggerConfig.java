package com.jeffinjude.orderprocessing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	    info = @Info(
	        title = "Order Processing Service API",
	        description = "This api manages the order details of products.",
	        summary = "This api can add an order, update order status, cancel an order, list all orders, list all order by status and get details of an order.",
	        contact = @Contact(
	        			name = "Jeffin Pulickal",
	        			email = "test@test.com"
	        		),
	        version = "v1"
	    )
	)
public class SwaggerConfig {
	//Access swagger at http://localhost:8092/swagger-ui/index.html
}
