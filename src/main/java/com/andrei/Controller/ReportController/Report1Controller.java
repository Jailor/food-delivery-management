package com.andrei.Controller.ReportController;

import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.Order;
import com.andrei.Controller.EmployeeController;
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
import java.util.List;

import static com.andrei.Controller.ClientController.configureTextFieldToAcceptOnlyDecimalValues;
import static com.andrei.Controller.ClientController.getIntFromTextField;

/**
 * Controller for the operations implemented by report 1.
 * The admin enters values for filters for hours, minutes and seconds.
 * Empty filters means that the filter is not applied.
 */
public class Report1Controller {
    @FXML
    private TableView<EmployeeController.FullOrder> orderTable;
    @FXML
    private TextField minSecond;
    @FXML
    private TextField maxSecond;
    @FXML
    private TextField minMinute;
    @FXML
    private TextField maxMinute;
    @FXML
    private TextField minHour;
    @FXML
    private TextField maxHour;
    @FXML
    protected void onBack(ActionEvent event) throws IOException {
        MainController.toAdmin(event);
    }

    /**
     * When the admin presses the "generate report" button,
     * The fields are parsed and the information sent to
     * delivery service. A list of orders that fit the filters
     * is generated. A bill is written to a file for permanency.
     */
    @FXML
    protected void onGenerateReport()
    {
        int minSecondFilter  = getIntFromTextField(minSecond);
        int maxSecondFilter = getIntFromTextField(maxSecond);
        int minMinuteFilter = getIntFromTextField(minMinute);
        int maxMinuteFilter = getIntFromTextField(maxMinute);
        int minHourFilter = getIntFromTextField(minHour);
        int maxHourFilter = getIntFromTextField(maxHour);
        List<Order> orders = DeliveryService.getInstance().filterReport1(
                minSecondFilter,maxSecondFilter,minMinuteFilter,maxMinuteFilter,minHourFilter,maxHourFilter
        );
        List<EmployeeController.FullOrder> updatedList = new ArrayList<>();
        for(Order order:orders)
        {
            updatedList.add(new EmployeeController.FullOrder(order));
        }
        orderTable.getItems().setAll(updatedList);
        new ReportBillWriter().writeReport1(updatedList, minSecondFilter,maxSecondFilter,minMinuteFilter,maxMinuteFilter,
                minHourFilter, maxHourFilter);
    }

    /**
     * Generates the header table using the full order objects from the employee class.
     * Orders are sorted by default and the text fields are configured to only accept
     * decimal values.
     */
    public void initialize()
    {
        Field[] fields = EmployeeController.FullOrder.class.getDeclaredFields();
        for(Field field: fields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
            String name = field.getName();
            TableColumn<EmployeeController.FullOrder,Integer> column = new TableColumn<>(name);
            column.setCellValueFactory(new PropertyValueFactory<>(name));
            orderTable.getColumns().add(column);
        }
        orderTable.getSortOrder().add(orderTable.getColumns().get(1));
        configureTextFieldToAcceptOnlyDecimalValues(minSecond);
        configureTextFieldToAcceptOnlyDecimalValues(maxSecond);
        configureTextFieldToAcceptOnlyDecimalValues(minMinute);
        configureTextFieldToAcceptOnlyDecimalValues(maxMinute);
        configureTextFieldToAcceptOnlyDecimalValues(minHour);
        configureTextFieldToAcceptOnlyDecimalValues(maxHour);
    }

}
