package com.andrei.Controller.ReportController;

import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
import com.andrei.Controller.MainController;
import com.andrei.DataAccess.ReportBillWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controller for the operations implemented by report 3.
 * The admin chooses a date. If not date is chosen,
 * orders from all dates are considered.
 */
@SuppressWarnings("unused")
public class Report4Controller {

    public static class ReportProduct
    {
        private final String title;
        private final double rating;
        private final int calories;
        private final int protein;
        private final int fat;
        private final int sodium;
        private final double price;
        private final String contains;
        private final int noOfTimesOrdered;

        public ReportProduct(String title, double rating, int calories, int protein, int fat, int sodium, double price, String contains, int noOfTimesOrdered) {
            this.title = title;
            this.rating = rating;
            this.calories = calories;
            this.protein = protein;
            this.fat = fat;
            this.sodium = sodium;
            this.price = price;
            this.contains = contains;
            this.noOfTimesOrdered = noOfTimesOrdered;
        }

        public String getTitle() {
            return title;
        }

        public double getRating() {
            return rating;
        }

        public int getCalories() {
            return calories;
        }

        public int getProtein() {
            return protein;
        }

        public int getFat() {
            return fat;
        }

        public int getSodium() {
            return sodium;
        }

        public double getPrice() {
            return price;
        }

        public String getContains() {
            return contains;
        }

        public int getNoOfTimesOrdered() {
            return noOfTimesOrdered;
        }
    }
    @FXML
    private TableView<ReportProduct> productTable;

    @FXML
    private DatePicker datePicker;

    @FXML
    protected void onBack(ActionEvent event) throws IOException {
        MainController.toAdmin(event);
    }

    /**
     * Trigger for generating the report. Filters are parsed and a map is returned of menu item and integer.
     * This map is used to generate the report products with product info + nr of times they have ordered on
     * that specific date.
     */
    @FXML
    protected void onGenerateReport() {
        List<ReportProduct> reportProducts = new ArrayList<>();
        LocalDate localDate = datePicker.getValue();
        HashMap<MenuItem,Integer> map = DeliveryService.getInstance().filterReport4(localDate);
        map.keySet().forEach( menuItem -> reportProducts.add(new ReportProduct(menuItem.getTitle(),
                menuItem.computeRating(), menuItem.computeCalories(), menuItem.computeProtein(),
                menuItem.computeFat(), menuItem.computeSodium(), menuItem.computePrice(),
                menuItem.getContains(),map.get(menuItem))));
        productTable.getItems().setAll(reportProducts);
        new ReportBillWriter().writeReport4(reportProducts, localDate);
    }

    public void initialize()
    {
        Field[] fields = ReportProduct.class.getDeclaredFields();
        for(Field field: fields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
            String name = field.getName();
            TableColumn<ReportProduct,Integer> column = new TableColumn<>(name);
            column.setCellValueFactory(new PropertyValueFactory<>(name));
            productTable.getColumns().add(column);
        }
    }

}
