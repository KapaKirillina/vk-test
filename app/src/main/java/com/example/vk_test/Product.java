package com.example.vk_test;

import java.util.List;
public class Product {
    private int id;
    private String title;
    private String description;
    private String thumbnail;
    private int price;

    public Product(int id, String title, String description, String thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Product(String title, String description, String thumbnail) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Product(String title, String description, String thumbnail, int price) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
