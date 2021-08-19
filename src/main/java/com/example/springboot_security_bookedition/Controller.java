
package com.example.springboot_security_bookedition;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
import java.util.*;

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
        return "redirect:/login";
    }
    @PostMapping("/processregister")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, Model model){

        model.addAttribute("user", user);
        model.addAttribute("message", "New user account created");
        user.setEnabled(true);
        userRepo.save(user);

        Role role = new Role(user.getUsername(), "ROLE_USER");
        roleRepo.save(role);

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
    public String createPizza(Model model){
        Pizza pizza = new Pizza();

        model.addAttribute("pizza", pizza);
        model.addAttribute("toppings", toppingRepo.findAllByInStockTrue());

        return "createpizza";
    }

    @PostMapping("/processpizza")
    public String processPizza(@ModelAttribute Pizza pizza, @RequestParam(name = "toppingList", required = false) String toppingList,
                               Principal principal){
        Set<OrderTopping> toppings = new HashSet<>();

        pizza.setUser(userRepo.findByUsername(principal.getName()));
        pizzaRepository.save(pizza);

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

        String name = userRepo.findByUsername(principal.getName()).getFirstName() + " " + userRepo.findByUsername(principal.getName()).getLastName();
        String pizzaOrder = pizza.getDough() + " dough " + pizza.getCheese() + " cheese " + pizza.getSauce() + " sauce ";
        String allToppings = "";
        String size = "";
        for(OrderTopping s: pizza.getToppings()){
            allToppings =  s.getName() + " " + allToppings;
        }

        if(pizza.getSize().equals("s")){
            size = "small";
        }
        else if(pizza.getSize().equals("m")){
            size = "medium";
        }
        else{
            size = "large";
        }

        sendSimpleMessage(userRepo.findByUsername(principal.getName()).getEmail(),"DJN Pizza Order Confirmation",
                "Hello " + name + ",\nyou recently ordered a " + size + " pizza with\n" + pizzaOrder + "\n"
                        + allToppings + "\nThank you for eating at DJN Pizza") ;

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

    @RequestMapping("/deleteTopping/{id}")
    public String deleteTopping(@PathVariable("id") long id){
        toppingRepo.findById(id).get().setInStock(false);
        toppingRepo.save(toppingRepo.findById(id).get());
        return "redirect:/createpizza";
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


    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("djnpizza@gmail.com");
        mailSender.setPassword("JavaBootcamp");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("djnpizza@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

}