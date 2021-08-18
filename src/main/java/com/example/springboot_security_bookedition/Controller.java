
package com.example.springboot_security_bookedition;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
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
    OrderToppingRepository orderToppingRepository;

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
        return "redirect:/login";
    }

    @RequestMapping("/home")
    public String listActors(){
        return "index";
    }

    @RequestMapping("/orderhistory")
    public String orderHistory(Model model){
        model.addAttribute("pizzas", pizzaRepository.findAll());
        model.addAttribute("toppings", toppingRepo.findAll());

        double fullSales = 0;
        for( Pizza p: pizzaRepository.findAll()){
            fullSales += p.getPrice();
        }

        Pizza pizza = new Pizza();
        model.addAttribute("pizza", pizza);
        pizza.setAllPizzaSales(fullSales);
        return "orderhistory";
    }

    @RequestMapping("/userhistory/{username}")
    public String teamDetails(@PathVariable("username") String username, Model model){
        model.addAttribute("userpizzas", pizzaRepository.findAllByUserUsername(username));
        model.addAttribute("toppings", toppingRepo.findAll());
        return "userhistory";
    }


    @RequestMapping("/customerlist")
    public String customerList(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "customerlist";
    }

    @GetMapping("/createpizza")
    public String createPizza(Model model, Principal principal){
        Pizza pizza = new Pizza();
        pizza.setUser(userRepo.findByUsername(principal.getName()));
        pizzaRepository.save(pizza);
        model.addAttribute("pizza", pizza);
        model.addAttribute("toppings", toppingRepo.findAll());

        return "createpizza";
    }

    @PostMapping("/processpizza")
    public String processPizza(@ModelAttribute Pizza pizza, @RequestParam(name = "toppingList", required = false) String toppingList){
        Set<OrderTopping> toppings = new HashSet<>();
        if(toppingList != null){
            String[] pickedToppings = toppingList.split(",");

            for(String s: pickedToppings){
                OrderTopping pickedTopping = new OrderTopping();
                pickedTopping.setTopping(toppingRepo.findById(Long.parseLong(s)).get());
                pickedTopping.setPizza(pizza);
                pickedTopping.setName(toppingRepo.findById(Long.parseLong(s)).get().getName());
                pickedTopping.setPrice(toppingRepo.findById(Long.parseLong(s)).get().getPrice());
                toppings.add(pickedTopping);
                orderToppingRepository.save(pickedTopping);
            }
        }
        pizza.setLocalDate(LocalDate.now());
        pizza.setLocalTime(LocalTime.now());
        pizza.setToppings(toppings);
        pizza.setPrice();
        pizzaRepository.save(pizza);

        return "receipt";
    }

    @RequestMapping("/receipt")
    public String receipt( Model model){
        model.addAttribute("pizza", new Pizza());
        return "receipt";
    }

    @GetMapping("/createTopping")
    public String createTopping(Model model){
        model.addAttribute("topping", new Topping());
        return "createtopping";
    }

    @PostMapping("/processTopping")
    public String processTopping(@ModelAttribute Topping topping){
        toppingRepo.save(topping);
        return "redirect:/createpizza";
    }



    @RequestMapping("/about")
    public String aboutUs(){
        return "about";
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping("/admin")
    public String admin(){return "admin";}

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/login?logout=true";
    }

}