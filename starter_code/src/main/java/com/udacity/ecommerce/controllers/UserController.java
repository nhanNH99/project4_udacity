package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserRepository userRepository;

	private final CartRepository cartRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserController (UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	private static final int PASSWORD_MINIMUM_SIZE = 8;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			log.info("UserController | findById | User Not Found. id {}", id);
			return ResponseEntity.notFound().build();
		} else {
			log.info("UserController | findById | User Found. id {}", id);
			return ResponseEntity.ok(user.get());
		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.info("UserController | findByUsername | User Not Found. username : {}", username);
			return ResponseEntity.notFound().build();
		} else {
			log.info("UserController | findByUsername | User Found. username : {}", username);
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if (!validPassword(createUserRequest.getPassword()) ||
			!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.info("UserController | CreateUser | Password did not meet minimum requirements.");
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);

		user.setPassword(null);
		log.info("UserController | CreateUser | Success: | username: {}", user.getUsername());
		return ResponseEntity.ok(user);
	}

	private boolean validPassword(String password) {
		return password != null && password.length() >= PASSWORD_MINIMUM_SIZE;
	}

}
