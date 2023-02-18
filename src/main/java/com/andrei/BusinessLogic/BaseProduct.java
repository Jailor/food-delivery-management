package com.andrei.BusinessLogic;

import java.io.Serial;

/**
 * A base products, extends menu item. The calculate methods are implemented as simple getters.
 * Has fields to store information about the product that match those in the description. Calls
 * the super constructor for the title.
 */
@SuppressWarnings("FieldMayBeFinal")
public class BaseProduct extends MenuItem {
    @Serial
    private static final long serialVersionUID = 1L;
    private double rating;
    private int calories;
    private int protein;
    private int fat;
    private int sodium;
    private double price;

    public BaseProduct(String title,double rating,int calories,int protein,int fat, int sodium, double price) {
        super(title);
        this.price = price;
        this.rating = rating;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
    }

    @Override
    public double computePrice() {
        return price;
    }

    @Override
    public double computeRating() {
        return rating;
    }
    @Override
    public int computeProtein() {
        return protein;
    }
    @Override
    public int computeCalories() {
        return calories;
    }
    @Override
    public int computeFat() {
        return fat;
    }
    @Override
    public int computeSodium() {
        return sodium;
    }
    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public String getContains() {
        return "-";
    }
}
