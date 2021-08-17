package com.example.springboot_security_bookedition;

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

    private String dough; // thin, normal, cheese

    private Set<String> set;


    public Pizza() {
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
}
