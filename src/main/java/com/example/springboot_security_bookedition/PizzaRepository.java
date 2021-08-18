package com.example.springboot_security_bookedition;

import org.springframework.data.repository.CrudRepository;

public interface PizzaRepository extends CrudRepository<Pizza, Long> {
    Iterable<Pizza> findAllByUserId(long id);
}
