package com.example.springboot_security_bookedition;

import com.example.springboot_security_bookedition.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//CRUD - Create, read, update, delete
public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);

}
