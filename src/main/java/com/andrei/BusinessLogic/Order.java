package com.andrei.BusinessLogic;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * The order class. Models the order for a customer. Has but 3 fields: client, date and price.
 * The date is the key for order, as java dates are precise to the millisecond. This class must implement
 * equals and hash code because it is used as a key by the map in delivery service to access the menu items
 * that the order contains.
 */
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Client client;

    private Date date;

    private double price;

    public Order(Client client, double price) {
        this.client = client;
        this.price = price;
        this.date = new Date();
    }

    public Order(Client client, Date date, double price) {
        this.client = client;
        this.date = date;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return date.equals(order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        return "Order{" +
                "client=" + client.getUsername() +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

    //GETTERS AND SETTERS


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
