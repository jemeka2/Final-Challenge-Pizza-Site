package com.example.springboot_security_bookedition;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String size;   // s m l

    private String sauce;  // none, normal, extra

    private String cheese; // none, normal, extra

    private String dough; // thin, normal, cheesy

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Topping> toppings;

    private double price;


    public Pizza() {

    }

    public Pizza(User user, String size, String sauce, String cheese, String dough, Set<Topping> toppings, double price) {
        this.user = user;
        this.size = size;
        this.sauce = sauce;
        this.cheese = cheese;
        this.dough = dough;
        this.toppings = toppings;
        this.price = price;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getSauce() {
        return sauce;
    }
    public void setSauce(String sauce) {
        this.sauce = sauce;
    }

    public String getCheese() {
        return cheese;
    }
    public void setCheese(String cheese) {
        this.cheese = cheese;
    }

    public String getDough() {
        return dough;
    }
    public void setDough(String dough) {
        this.dough = dough;
    }

    public Set<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(Set<Topping> toppings) {
        this.toppings = toppings;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice() {
        double subtotal = 7.00;
        if(this.size.equals("m")){
            subtotal += 2.00;
        } else if(this.size.equals("l")){
            subtotal += 4.00;
        }
        if(this.cheese.equals("extra")){
            subtotal += 1.00;
        }
        if(this.dough.equals("cheesy")){
            subtotal += 1.00;
        }
        if(this.sauce.equals("extra")){
            subtotal += 1.00;
        }
        for (Topping topping : this.toppings) {
            subtotal += topping.getPrice();
        }
        this.price = subtotal;
    }
}
