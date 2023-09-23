package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.UserOrder;
import com.udacity.ecommerce.model.persistence.repositories.OrderRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final static Logger log = LoggerFactory.getLogger(OrderController.class);

	private final UserRepository userRepository;

	private final OrderRepository orderRepository;

	public OrderController (UserRepository userRepository, OrderRepository orderRepository){
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.warn("OrderController | submit | Missing username.");
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		log.info("OrderController | submit | Success from username : {}" ,username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.warn("OrderController | getOrdersForUser | Missing username.");
			return ResponseEntity.notFound().build();
		}

		log.info("OrderController | getOrdersForUser | Order listed from username : {}", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
