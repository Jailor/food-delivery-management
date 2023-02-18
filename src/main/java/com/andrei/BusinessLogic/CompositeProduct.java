package com.andrei.BusinessLogic;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * The composite product. Extends menu item and has a list of menu item
 * but no other fields. Because of this, field values are calculated dynamically
 * by calling the corresponding method for each menu item in the list. These menu items
 * can be base products or other composite products. Thus, a tree of recursion is generated
 * with leaves being the base products and internal nodes being composite products.
 */
public class CompositeProduct extends MenuItem{
    @Serial
    private static final long serialVersionUID = 1L;

    private List<MenuItem> items;

    public CompositeProduct(String title) {
        super(title);
        items = new ArrayList<>();
    }

    public CompositeProduct(String title, List<MenuItem> items) {
        super(title);
        this.items = items;
    }


    public void addItem(MenuItem item)
    {
        items.add(item);
    }

    public void removeItem(MenuItem item)
    {
        items.remove(item);
    }
    //equivalent of getChildren() method
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(MenuItem item: items)
        {
            s.append(item.toString());
            s.append(", ");
        }
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

    @Override
    public double computeRating() {
        double rating = 0;
        for(MenuItem item: items)
        {
            rating += item.computeRating();
        }
        rating = rating/items.size();
        return rating;
    }

    @Override
    public int computeCalories() {
        int calories = 0;
        for (MenuItem item: items)
        {
            calories += item.computeCalories();
        }
        return calories;
    }

    @Override
    public int computeProtein() {
        int protein = 0;
        for (MenuItem item: items)
        {
            protein += item.computeProtein();
        }
        return protein;
    }

    @Override
    public int computeFat() {
        int fat = 0;
        for(MenuItem item: items)
        {
            fat += item.computeFat();
        }
        return fat;
    }

    @Override
    public int computeSodium() {
        int sodium = 0;
        for(MenuItem item: items)
        {
            sodium += item.computeSodium();
        }
        return sodium;
    }

    @Override
    public double computePrice() {
        double price = 0;
        for(MenuItem item: items)
        {
            price += item.computePrice();
        }
        return price;
    }

    @Override
    public String getContains() {
        StringBuilder s = new StringBuilder();
        for(MenuItem menuItem: items)
        {
            s.append(menuItem.getTitle() + "; ");
        }
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

    public List<MenuItem> getItems() {
        return items;
    }
}
