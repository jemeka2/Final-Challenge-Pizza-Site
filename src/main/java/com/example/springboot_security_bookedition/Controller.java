
package com.example.springboot_security_bookedition;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@org.springframework.stereotype.Controller
public class Controller {


    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    ToppingRepo toppingRepo;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepo.findByUsername(username));
        return "secure";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/processregister")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        if(result.hasErrors()){
            user.clearPassword();
            model.addAttribute("user", user);
            return "register";
        }else{
            model.addAttribute("user", user);
            model.addAttribute("message", "New user account created");
            user.setEnabled(true);
            userRepo.save(user);

            Role role = new Role(user.getUsername(), "ROLE_USER");
            roleRepo.save(role);
        }
        return "index";
    }

    @RequestMapping("/home")
    public String listActors(){
        return "index";
    }

    @GetMapping("/createpizza")
    public String createPizza(Model model, Principal principal){
        Pizza pizza = new Pizza();

        pizza.setUser(userRepo.findByUsername(principal.getName()));
        model.addAttribute("pizza", pizza);
        model.addAttribute("toppings", toppingRepo.findAll());
        return "createpizza";
    }

    @PostMapping("/processpizza")
    public String processPizza(@ModelAttribute Pizza pizza, @RequestParam(name = "topping1")long topping1
            , @RequestParam(name = "topping2")long topping2
            , @RequestParam(name = "topping3") long topping3){
        Set<Topping> toppings = new HashSet<>();
        toppingRepo.findById(topping1).get().setPizza(pizza);
        toppingRepo.findById(topping2).get().setPizza(pizza);
        toppingRepo.findById(topping3).get().setPizza(pizza);
        toppings.add(toppingRepo.findById(topping1).get());
        toppings.add(toppingRepo.findById(topping2).get());
        toppings.add(toppingRepo.findById(topping3).get());
        pizza.setToppings(toppings);
        pizza.setPrice();
        pizzaRepository.save(pizza);
        return "redirect:/receipt";
    }

    @RequestMapping("/receipt")
    public String receipt(Model model){
        model.addAttribute("pizzas", pizzaRepository.findAll());
        return "receipt";
    }

    @RequestMapping("/about")
    public String aboutUs(){
        return "about";
    }

    @RequestMapping("/login")
    public String login(){return "login";}

    @RequestMapping("/admin")
    public String admin(){return "admin";}

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/login?logout=true";
    }
}