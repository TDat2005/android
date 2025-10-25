package com.example.myapplication10;

import java.io.Serializable;

// Quan trọng!
public class Product implements Serializable {
    private int id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private double rating;


    // Constructor
    public Product(int id, String title, double price, String description, String category, String image, double rating) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.rating = rating;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }
}