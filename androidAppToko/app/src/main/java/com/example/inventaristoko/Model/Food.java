package com.example.inventaristoko.Model;

import java.math.BigDecimal;

public class Food {
    private int id;
    private String name;
    private String category;
    private BigDecimal price;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
