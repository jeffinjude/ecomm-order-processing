package com.jeffinjude.orderprocessing.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.jeffinjude.orderprocessing.services.OrderService;
import com.jeffinjude.payment.entities.MessagePayload;

@Service
public class Consumer {
	private static final String TOPIC = "ecomm-order-status";
	
	private static final Logger log = LoggerFactory.getLogger(Consumer.class);
	
	@Autowired
	OrderService orderService;
	
	@KafkaListener(topics = TOPIC, groupId = "ecomm_order_status_group")
	public void consumeMessage(MessagePayload message) {
		log.info("Inside Consumer. Payload: " + message.toString());
		try {
			orderService.updateOrderStatusService(message.getOrderStatus(), message.getOrderId());
		} catch (Exception e) {
			log.error("Inside Consumer. Exception: " + e.getMessage());
		}
	}
}
