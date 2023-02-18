package com.andrei.Controller.ReportController;

import com.andrei.BusinessLogic.*;
import com.andrei.Controller.MainController;
import com.andrei.DataAccess.ReportBillWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.andrei.Controller.ClientController.*;

/**
 * Controller for the operations implemented by report 3.
 * The admin enters values for the minimum number of orders
 * and the price for these orders. Left empty, all clients will be shown.
 */
@SuppressWarnings("unused")
public class Report3Controller {
    public static class ReportClient
    {
        private final String username;
        private final String password;
        private final String phoneNumber;
        private final int noOfOrders;
        public ReportClient(Client client, int noOfOrders)
        {
            this.username = client.getUsername();
            this.password = client.getPassword();
            this.phoneNumber = client.getPhoneNumber();
            this.noOfOrders = noOfOrders;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public int getNoOfOrders() {
            return noOfOrders;
        }
    }
    @FXML
    private TableView<ReportClient> clientTable;
    @FXML
    private TextField minOrder;
    @FXML
    private TextField minPrice;

    @FXML
    protected void onBack(ActionEvent event) throws IOException {
        MainController.toAdmin(event);
    }

    /**
     * Trigger for generating the report. Filters are parsed and a map is returned of client and integer.
     * This map is used to generate the report clients with client info + nr of times they have ordered.
     */
    @FXML
    protected void onGenerateReport()
    {
        List<ReportClient> reportClients = new ArrayList<>();
        int minOrderFilter = getIntFromTextField(minOrder);
        double minPriceFilter = getDoubleFromTextField(minPrice);
        HashMap<Client,Integer> map = DeliveryService.getInstance().filterReport3(minOrderFilter,minPriceFilter);
        List<Client> clients = map.keySet().stream().toList();
        for (Client client: clients)
        {
            reportClients.add(new ReportClient(client,map.get(client)));
        }
        clientTable.getItems().setAll(reportClients);
        new ReportBillWriter().writeReport3(reportClients,minOrderFilter,minPriceFilter);
    }

    public void addTableColumns() {
        Field[] fields = ReportClient.class.getDeclaredFields();
        addColFromClass(fields);
    }

    private void addColFromClass(Field[] fields) {
        for(Field field: fields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
            String name = field.getName();
            //if(name.equals("password")) continue;
            TableColumn<ReportClient,Integer> column = new TableColumn<>(name);
            column.setCellValueFactory(new PropertyValueFactory<>(name));
            clientTable.getColumns().add(column);
        }
    }



    public void initialize()
    {
        addTableColumns();
        configureTextFieldToAcceptOnlyDecimalValues(minOrder);
        configureTextFieldToAcceptOnlyDecimalValues(minPrice);
    }
}
