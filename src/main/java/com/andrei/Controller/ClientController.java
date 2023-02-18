package com.andrei.Controller;

import com.andrei.BusinessLogic.BaseProduct;
import com.andrei.BusinessLogic.Client;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
import com.andrei.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import static com.andrei.Controller.MainController.showAlert;

/**
 * Controller operations for the client view. Supports searching, filtering and selecting from the table in order
 * to make a new order.
 */
public class ClientController {
    @FXML
    private TableView<MenuItem> itemTable;

    @FXML
    private TextField searchBar;

    @FXML
    private TextField minRating;
    @FXML
    private TextField maxRating;
    @FXML
    private TextField minCalories;
    @FXML
    private TextField maxCalories;
    @FXML
    private TextField minProtein;
    @FXML
    private TextField maxProtein;
    @FXML
    private TextField minFat;
    @FXML
    private TextField maxFat;
    @FXML
    private TextField minSodium;
    @FXML
    private TextField maxSodium;
    @FXML
    private TextField minPrice;
    @FXML
    private TextField maxPrice;

    private Client client;

    /**
     * Back to client login.
     */
    @FXML
    protected void onBack(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("client-login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setScene(scene);
    }

    /**
     * Perhaps unfortunately named onKeyTyped, this method is called whenever a key is typed in the search bar
     * and whenever ENTER is pressed in the filter fields. The keyword is parsed from the search bar and the filters are
     * parsed from their corresponding filter text fields. We know these parses succeed because each text field only accepts
     * decimal values. Doubles have to be entered with a "," instead of ".", but the user is alerted of that in the GUI.
     *
     * The corresponding filtering method is called in the delivery service and the updated list is returned and set into the
     * table.
     */
    @FXML
    protected void onKeyTyped()
    {
        String keyword = searchBar.getText().toLowerCase();
        final double minRatingFilter = getDoubleFromTextField(minRating);
        final double maxRatingFilter = getDoubleFromTextField(maxRating);
        final int minCaloriesFilter = getIntFromTextField(minCalories);
        final int maxCaloriesFilter = getIntFromTextField(maxCalories);
        final int minProteinFilter = getIntFromTextField(minProtein);
        final int maxProteinFilter = getIntFromTextField(maxProtein);
        final int minFatFilter = getIntFromTextField(minFat);
        final int maxFatFilter = getIntFromTextField(maxFat);
        final int minSodiumFilter = getIntFromTextField(minSodium);
        final int maxSodiumFilter = getIntFromTextField(maxSodium);
        final double minPriceFilter = getDoubleFromTextField(minPrice);
        final double maxPriceFilter = getDoubleFromTextField(maxPrice);
        List<MenuItem> updatedList = DeliveryService.getInstance().clientFilter(
                keyword,minRatingFilter,maxRatingFilter,minCaloriesFilter,maxCaloriesFilter,
                minProteinFilter,maxProteinFilter,minFatFilter,maxFatFilter,minSodiumFilter,maxSodiumFilter,
                minPriceFilter,maxPriceFilter
        );
        refreshTable(updatedList);
    }

    /**
     * Creates an order. Selection from table is acquired and checked for size. If at least 1 product has been selected from
     * the table, the order is generated and an alert is shown containing information about the ordered products.
     */
    @FXML
    protected void onCreateOrder()
    {
        ObservableList<MenuItem> entries = itemTable.getSelectionModel().getSelectedItems();
        if(entries.size() < 1)
        {
            showAlert("Must select at least one menu item for order!");
        }
        else
        {
            ArrayList<MenuItem> arrayList = entries.stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            DeliveryService.getInstance().addOrder(client,arrayList);
            StringBuilder s = new StringBuilder("Order successfully created!\n\nOrdered products are:\n");
            entries.forEach(menuItem -> s.append(menuItem.getTitle()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION,s.toString());
            alert.setHeaderText("Order confirmed!");
            alert.show();
        }
    }

    /**
     * @param textField -> text field to parse
     * @return parsed double from text fields
     */
    public static double getDoubleFromTextField(TextField textField)
    {
        if(textField.getText().equals("")) return -1;
        String text;
        if(textField.getText().contains(","))
        {
            text = textField.getText().replace(',','.');
        }
        else text = textField.getText();
        return Double.parseDouble(text);
    }

    /**
     * @param textField -> text field to parse
     * @return parsed int from text field
     */
    public static int getIntFromTextField(TextField textField)
    {
        if(textField.getText().equals("")) return -1;
        String text;
        if(textField.getText().contains(","))
        {
            text = textField.getText().replace(',','.');
        }
        else text = textField.getText();
        return (int)Math.floor(Double.parseDouble(text));
    }

    //table initialisation
    public void addTableColumns() {
        Field[] fields = MenuItem.class.getDeclaredFields();
        addColFromClass(fields);
        fields = BaseProduct.class.getDeclaredFields();
        addColFromClass(fields);
        String name = "contains";
        TableColumn<MenuItem,Integer> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        itemTable.getColumns().add(column);
    }

    private void addColFromClass(Field[] fields) {
        for(Field field: fields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
            String name = field.getName();
            TableColumn<MenuItem,Integer> column = new TableColumn<>(name);
            column.setCellValueFactory(new PropertyValueFactory<>(name));
            itemTable.getColumns().add(column);
        }
    }
    public void refreshTable(){
        DeliveryService deliveryService = DeliveryService.getInstance();
        List<MenuItem> lst =  deliveryService.getMenuItems();
        itemTable.getItems().setAll(lst);
    }
    public void refreshTable(List<MenuItem> lst){
        itemTable.getItems().setAll(lst);
    }

    /**
     * Sets selection mode to multiple and configures text fields to only accept numbers of type XXX...X,YYY...
     * The table is also loaded with all the menu items initially and the table header is generated similarly to the
     * one in the admin controller.
     */
    public void initialize()
    {
        itemTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        addTableColumns();
        refreshTable();
        configureTextFieldToAcceptOnlyDecimalValues(minRating);
        configureTextFieldToAcceptOnlyDecimalValues(maxRating);
        configureTextFieldToAcceptOnlyDecimalValues(minCalories);
        configureTextFieldToAcceptOnlyDecimalValues(maxCalories);
        configureTextFieldToAcceptOnlyDecimalValues(minProtein);
        configureTextFieldToAcceptOnlyDecimalValues(maxProtein);
        configureTextFieldToAcceptOnlyDecimalValues(minFat);
        configureTextFieldToAcceptOnlyDecimalValues(maxFat);
        configureTextFieldToAcceptOnlyDecimalValues(minSodium);
        configureTextFieldToAcceptOnlyDecimalValues(maxSodium);
        configureTextFieldToAcceptOnlyDecimalValues(minPrice);
        configureTextFieldToAcceptOnlyDecimalValues(maxPrice);
    }

    /**
     * Name is suggestive. Static method because it is called by other classes.
     * @param textField -> text field to be modified
     */
    public static void configureTextFieldToAcceptOnlyDecimalValues(TextField textField) {

        DecimalFormat format = new DecimalFormat("#");

        final TextFormatter<Object> decimalTextFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(change.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                return null;
            } else {
                return change;
            }
        });
        textField.setTextFormatter(decimalTextFormatter);
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
