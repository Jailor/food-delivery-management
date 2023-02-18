package com.andrei.Controller.ReportController;

import com.andrei.BusinessLogic.BaseProduct;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
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
import java.util.List;

import static com.andrei.Controller.ClientController.configureTextFieldToAcceptOnlyDecimalValues;
import static com.andrei.Controller.ClientController.getIntFromTextField;

/**
 * Controller for the operations implemented by report 2.
 * The admin enters values for the minimum number of times the
 * menu item has been ordered. If the filter is left empty, all
 * menu items are shown.
 */
public class Report2Controller {
    @FXML
    private TableView<MenuItem> productTable;

    @FXML
    private TextField filter;

    @FXML
    protected void onBack(ActionEvent event) throws IOException {
        MainController.toAdmin(event);
    }

    /**
     * Trigger for generating the report. Filter is parsed, table updated and information permeated to a file.
     */
    @FXML
    protected void onGenerateReport()
    {
        int minFilter = getIntFromTextField(filter);
        List<MenuItem> menuItems = DeliveryService.getInstance().filterReport2(minFilter);
        productTable.getItems().setAll(menuItems);
        new ReportBillWriter().writeReport2(menuItems, minFilter);
    }

    public void addTableColumns() {
        Field[] fields = MenuItem.class.getDeclaredFields();
        addColFromClass(fields);
        fields = BaseProduct.class.getDeclaredFields();
        addColFromClass(fields);
        String name = "contains";
        TableColumn<MenuItem,Integer> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        productTable.getColumns().add(column);
    }

    private void addColFromClass(Field[] fields) {
        for(Field field: fields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
            String name = field.getName();
            TableColumn<MenuItem,Integer> column = new TableColumn<>(name);
            column.setCellValueFactory(new PropertyValueFactory<>(name));
            productTable.getColumns().add(column);
        }
    }



    public void initialize()
    {
        addTableColumns();
        configureTextFieldToAcceptOnlyDecimalValues(filter);
    }
}
