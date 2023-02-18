package com.andrei.BusinessLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IDeliveryServiceProcessing {
    /**
     * This method deletes all products from the table and replaces them with the products
     * from the CSV file located at absolute path.
     *
     * @pre absolutePath != null
     * @post isWellFormed() && list.size() > 0
     * @param absolutePath -> Absolute path of file that contains information
     */
     void populateProducts(String absolutePath);

    /**
     * This method removes a single menu item from the list of available ones
     *
     * @pre menuItems != null
     * @post isWellFormed() && list.size()@pre  == list.size()@post - 1
     * @param menuItem -> menuItem instance to be deleted
     */
     void removeMenuItem(MenuItem menuItem);

    /**
     * This method adds a single menu item at index 0.
     *
     * @pre menuItems != null
     * @post isWellFormed() && list.size()@pre  == list.size()@post + 1
     * @param menuItem -> menuItem instance to be added
     */
    void addMenuItem(MenuItem menuItem);

    /**
     * This method edits a single base product and updates the corresponding composites
     *
     * @pre menuItems != null
     * @post isWellFormed() && list.size()@pre  == list.size()@post
     * @param baseProduct -> base product details
     * @param updatedProduct -> updated product details
     */
    void editBaseProduct(BaseProduct updatedProduct,BaseProduct baseProduct);
    /**
     * This method adds an order at the current date and time using the specified orders.
     * The value of the order must be calculated and writes a bill for the specified order.
     * @pre isWellFormed() && client != null && menuItems.size() >= 1
     * @post orders.size()@post == orders.size()@pre && map(order).containsAll(menuItems)
     * @param client -> client that made the order
     * @param menuItems -> list of menu items to be added to order
     */
     void addOrder(Client client, ArrayList<MenuItem> menuItems);

    /**
     * Grabs the keySet of the HashMap and returns it as a list of orders
     * @pre orderDetails != null
     * @post nochange
     * @return list of all the orders
     */
     List<Order> getAllOrders();

    /**
     * Filters products based on the criteria that the client requested. If a filter is -1, it is not applied.
     * @pre isWellFormed()
     * @post updatedList.size() <= menuItems.size() && forEach menu item contains keyword
     * @param keyword -> search substring
     * @return list of updated products
     */
     List<MenuItem> clientFilter(String keyword, double minRatingFilter, double maxRatingFilter,
                                       int minCaloriesFilter, int maxCaloriesFilter,
                                       int minProteinFilter, int maxProteinFilter,
                                       int minFatFilter, int maxFatFilter,
                                       int minSodiumFilter, int maxSodiumFilter,
                                       double minPriceFilter, double maxPriceFilter);

    /**
     * Filters orders between a given start time of day and a given end time of day
     * @pre isWellFormed()
     * @post updatedList.size() <= orders.size() && orders conforms to filters
     * @return a list of orders conforming to the filters
     */
     List<Order> filterReport1(
            int minSecondFilter , int maxSecondFilter,
            int minMinuteFilter, int maxMinuteFilter,
            int minHourFilter, int maxHourFilter
    );

    /**
     * Filters to list that contains all the products ordered a minimum number of times so far.
     * @pre isWellFormed()
     * @post updatedList.size() <= menuItems.size() && menu items conforms to filter
     * @param minFilter -> minimum number of times an item has been ordered, by default 0.
     * @return -> list of updated menu items that have been ordered at least minFilter times
     */
    List<MenuItem> filterReport2(int minFilter);

    /**
     * Filters to a map that contains all the clients that have ordered a minimum number of times so far.
     * @pre isWellFormed()
     * @post map(client) >= minOrderFilter && order list conforms to filters
     * @param minOrderFilter -> minimum number of orders for each client
     * @param minPriceFilter -> minimum price for each order
     * @return map of filtered clients and number of orders
     */
    HashMap<Client,Integer> filterReport3(int minOrderFilter, double minPriceFilter);

    /**
     * Filters to a map that contains all the products ordered within a specific day and the number of times they have been ordered.
     * @pre isWellFormed()
     * @post each menu item must be found in at least one order
     * @param localDate -> date for report
     * @return -> map of menu items and the times they have been ordered on that day
     */
    HashMap<MenuItem,Integer> filterReport4(LocalDate localDate);
}
