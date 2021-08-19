package com.example.springboot_security_bookedition;


import org.springframework.data.repository.CrudRepository;

//CRUD - Create, read, update, delete
public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Iterable<User> findAllByUsernameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCaseOrEmailContainsIgnoreCase(String username, String firstName, String lastName, String Email);
}
