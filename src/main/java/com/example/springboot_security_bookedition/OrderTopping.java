package com.example.springboot_security_bookedition;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class OrderTopping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Min(0)
    @Max(20)
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topping_id")
    private Topping topping;

    public OrderTopping() {
    }

    public OrderTopping(String name, double price, Pizza pizza, Topping topping) {
        this.name = name;
        this.price = price;
        this.pizza = pizza;
        this.topping = topping;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Topping getTopping() {
        return topping;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
    }


}
