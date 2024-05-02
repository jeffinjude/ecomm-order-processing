package com.jeffinjude.orderprocessing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jeffinjude.orderprocessing.entities.OrderDetails;

public interface OrderRepository extends JpaRepository<OrderDetails, Integer> {

	List<OrderDetails> findByOrderStatus(String orderStatus);
	
	@Modifying
	@Query("update OrderDetails od set od.orderStatus = :orderStatus where od.orderId = :orderId")
	int updateOrderStatus(@Param("orderStatus") String orderStatus, @Param("orderId") int orderId);


}
