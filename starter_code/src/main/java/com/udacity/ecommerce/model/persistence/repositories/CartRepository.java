package com.udacity.ecommerce.model.persistence.repositories;

import com.udacity.ecommerce.model.persistence.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
