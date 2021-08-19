package com.example.springboot_security_bookedition;

import org.springframework.data.repository.CrudRepository;

public interface OrderToppingRepository extends CrudRepository<OrderTopping, Long> {
    long countByName(String toppingName);
}
