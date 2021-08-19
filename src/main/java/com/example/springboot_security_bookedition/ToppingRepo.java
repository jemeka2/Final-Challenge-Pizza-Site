package com.example.springboot_security_bookedition;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ToppingRepo extends CrudRepository<Topping, Long> {
    Iterable<Topping> findAllByInStockTrue();
}
