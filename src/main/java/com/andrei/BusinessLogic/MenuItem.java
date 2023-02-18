package com.andrei.BusinessLogic;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 *Base class for the composite design pattern of menu item. Has abstract methods for calculating the fields
 * for the base product and composite product. Overrides hash code and equals on title (title is a unique key).
 * The abstract methods will be implemented by the Composite Product by calling the methods for each of its elements.
 * The base product will just return the fields.
 */

@SuppressWarnings("FieldMayBeFinal")
public abstract class MenuItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private String title;

    public MenuItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MenuItem)
        {
            return getTitle().equals(((MenuItem) obj).getTitle());
        }
        else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
        //return super.hashCode();
    }

    public abstract double computeRating();
    public abstract int computeCalories();
    public abstract int computeProtein();
    public abstract int computeFat();
    public abstract int computeSodium();
    public abstract double computePrice();

    //GETTERS FOR JAVAFX
    public double getRating(){
        return computeRating();
    }
    public int getCalories(){
        return computeCalories();
    }
    public int getProtein(){
        return computeProtein();
    }
    public int getFat(){
        return computeFat();
    }
    public int getSodium(){
        return computeSodium();
    }
    public double getPrice(){
        return computePrice();
    }
    public abstract String getContains();
}
