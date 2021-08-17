package com.example.springboot_security_bookedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CommandLineRunnerBean implements CommandLineRunner {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    ToppingRepo toppingRepo;

    public void run(String...args){
        User user = new User("bart", "bart@domain.com", "bart", "Bart","Simpson", true, null);
        Role userRole = new Role("bart", "ROLE_USER");
        userRepo.save(user);
        roleRepo.save(userRole);

        User admin = new User("super", "super@domain.com", "super", "Super", "Hero", true, null);
        Role adminRole1 = new Role("super", "ROLE_ADMIN");
        Role adminRole2 = new Role("super", "ROLE_USER");

        userRepo.save(admin);
        roleRepo.save(adminRole1);
        roleRepo.save(adminRole2);

        User temp = new User("temp", "temp@domain.com", "temp", "temp", "user", true, null);
        Role tempRole3 = new Role("temp", "ROLE_USER");

        userRepo.save(temp);
        roleRepo.save(tempRole3);

        Topping pepperoni = new Topping("pepperoni", 0.5, true);
        toppingRepo.save(pepperoni);
        Topping onions = new Topping("onions", 0.5, false);
        toppingRepo.save(onions);
        Topping peppers = new Topping("pepper", 0.5, true);
        toppingRepo.save(peppers);

    }

}
