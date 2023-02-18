package com.andrei.Controller;

import com.andrei.BusinessLogic.Client;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
import com.andrei.BusinessLogic.Order;
import com.andrei.DataAccess.ReportBillWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.andrei.Controller.MainController.goToMainMenu;
import static com.andrei.Controller.MainController.showAlert;

/**
 * Controller for the employee. Is an observer of delivery service and gets notified each time a new order is
 * made and then added to the table. Has a class named FullOrder which has the order contents as a string in
 * order to utilise in the table to display on screen.
 */
@SuppressWarnings({"deprecation","unused"})
public class EmployeeController implements Observer {
    /**
     * A helper class that is used for the table details.
     * The order object does not contain enough useful information for the employee,
     * so this class is the one that is actually represented in the table.
     */
    public static class FullOrder
    {
        private final String clientName;
        private final Date date;
        private final double price;
        private final String contents;
        public FullOrder(Order order)
        {
            clientName = order.getClient().getUsername();
            date = order.getDate();
            price = order.getPrice();
            ArrayList<MenuItem> menuItems = DeliveryService.getInstance().getOrderDetails().get(order);
            StringBuilder s = new StringBuilder();
            for (MenuItem menuItem: menuItems)
            {
                s.append(menuItem.getTitle()).append("\n");
            }
            contents = s.toString();
        }

        public String getClientName() {
            return clientName;
        }

        public Date getDate() {
            return date;
        }

        public double getPrice() {
            return price;
        }

        public String getContents() {
            return contents;
        }
    }
    @FXML
    private TableView<FullOrder> orderTable;

    @FXML
    protected void onMainMenu(ActionEvent event) throws IOException {
        goToMainMenu(event);
    }

    /**
     * Receives an order from the delivery service class and creates a full order
     * item. The table is added to the orders already in the table
     * @param o   the observable object, delivery service in this case
     * @param arg an argument passed to the {@code notifyObservers}
     *            method. In this, is an object of type order.
     */
    @Override
    public void update(Observable o, Object arg)
    {
        Order order = (Order) arg;
        FullOrder fullOrder = new FullOrder(order);
        orderTable.getItems().add(fullOrder);
    }

    /**
     * Removes the order that is currently selected in the table. The corresponding order
     * is deleted from the delivery service class and its corresponding bill is also deleted.
     */
    @FXML
    protected void onRemove(){
        FullOrder entry = orderTable.getSelectionModel().getSelectedItem();
        if(entry == null)
        {
            showAlert("Must select order to delete!");
        }
        else
        {
            Client client = new Client(entry.clientName,null,null);
            Order order = new Order(client,entry.date,0);
            DeliveryService.getInstance().getOrderDetails().remove(order);
            new ReportBillWriter().deleteBill(order);
            orderTable.getItems().remove(entry);
        }
    }

    /**
     * Generates the header for the table. Full orders are generated from the current orders
     * in the delivery service class and loaded into the table. Default sorting is made on
     * order date.
     */
    public void initialize()
    {
        Field[] fields = FullOrder.class.getDeclaredFields();
        for(Field field: fields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
            String name = field.getName();
            TableColumn<FullOrder,Integer> column = new TableColumn<>(name);
            column.setCellValueFactory(new PropertyValueFactory<>(name));
            orderTable.getColumns().add(column);
        }
        List<FullOrder> tableOrders = new ArrayList<>();
        for(Order order: DeliveryService.getInstance().getAllOrders())
        {
            tableOrders.add(new FullOrder(order));
        }
        orderTable.getItems().setAll(tableOrders);
        orderTable.getSortOrder().add(orderTable.getColumns().get(1));
    }
    public void removeAllOrders()
    {
        orderTable.getItems().clear();
    }
}
