package com.andrei.Controller;

import com.andrei.BusinessLogic.*;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static com.andrei.Controller.MainController.goToMainMenu;
import static com.andrei.Controller.MainController.showAlert;


/**
 * The admin controller class contains methods for the admin interface: loading products from a CSV,
 * creating and editing the menu items and generating reports based on some criteria.
 */
public class AdminController {
    @FXML
    private TableView<MenuItem> itemTable;
    @FXML
    private TextField compositeNameTextField;
    @FXML
    protected void onMainMenu(ActionEvent event) throws IOException {
        goToMainMenu(event);
    }

    /**
     * Adding a base product is delegated to a new window. Window modal means that the user cannot interact
     * with the main window until he is done with the other one. Created menu items are inserted at index 0.
     */
    @FXML
    protected void onAddBaseItem(ActionEvent e) throws IOException {
        Stage primaryStage =(Stage)((Node)e.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage.getScene().getWindow());
        URL fxmlLocation = Main.class.getResource("base-edit-create.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setScene(scene);
        ((CreateEditBaseController)fxmlLoader.getController()).setAdminController(this);
        stage.show();
    }

    /**
     * Similar to adding a base product, but the selection from the table is checked
     * and the corresponding entry is loaded into the editor using the loadData method.
     */
    @FXML
    protected void onEditBaseItem(ActionEvent e) throws IOException {
        MenuItem entry = itemTable.getSelectionModel().getSelectedItem();
        ObservableList<MenuItem> entries = itemTable.getSelectionModel().getSelectedItems();
        if(entries.size() > 1)
        {
            showAlert("Cannot edit multiple items at the same time!");
        }
        else if(entry==null)
        {
            showAlert("Must select item to edit");
        } else if (entry instanceof CompositeProduct) {
            showAlert("Editing of composite products not available at the moment!");
        } else
        {
            Stage primaryStage =(Stage)((Node)e.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage.getScene().getWindow());
            URL fxmlLocation = Main.class.getResource("base-edit-create.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load(), 700, 550);
            stage.setScene(scene);
            ((CreateEditBaseController)fxmlLoader.getController()).setAdminController(this);
            ((CreateEditBaseController)fxmlLoader.getController()).loadData((BaseProduct) entry);
            stage.show();
        }
    }

    /**
     * Removes an item or multiple items that are currently selected in the table.
     * The selected items are checked if they are part of a composite product. In case
     * that is the case, the user is alerted about this and the operation is aborted.
     *
     * The user will be alerted if they are sure they want to delete the selected item.
     * On accept, the corresponding method will be called in the delivery service class
     * which shall delete the menu item from the corresponding list and then refresh the
     * table.
     */
    @FXML
    protected void onRemoveItem() {
        MenuItem entry = itemTable.getSelectionModel().getSelectedItem();
        ObservableList<MenuItem> entries = itemTable.getSelectionModel().getSelectedItems();
        if(entries.size() > 1)
        {
            List<CompositeProduct> compositeProducts = DeliveryService.getInstance().getComposite();
            for(MenuItem menuItem: entries)
            {
                for(CompositeProduct compositeProduct: compositeProducts)
                {
                    if(compositeProduct.getItems().contains(menuItem))
                    {
                        showAlert("Cannot delete product that is part of composite product!");
                        return;
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Entries Confirmation Alert");
            StringBuilder s = new StringBuilder("Are you sure that you want to delete these entries?\n");
            for(MenuItem menuItem: entries)
            {
                s.append(menuItem.getTitle()).append("\n");
            }
            alert.setContentText(s.toString());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    for (MenuItem menuItem: entries)
                    {
                        DeliveryService.getInstance().removeMenuItem(menuItem);
                    }
                    refreshTable();
                }
            });
        }
        else if(entry==null) {
            showAlert("Must select item to remove");
        }
        else
        {
            List<CompositeProduct> compositeProducts = DeliveryService.getInstance().getComposite();

            for(CompositeProduct compositeProduct: compositeProducts)
            {
                if(compositeProduct.getItems().contains(entry))
                {
                    showAlert("Cannot delete product that is part of composite product!");
                    return;
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Entry Confirmation Alert");
            alert.setContentText("Are you sure that you want to delete this entry?\n" + entry.getTitle() + "\n");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    DeliveryService.getInstance().removeMenuItem(entry);
                    refreshTable();
                }
            });
        }
    }

    /**
     * When the create composite button is pressed, the selection from the table is taken and checked for size.
     * The selection will be rejected if not formed of at least 2 products. The title will also be rejected if it
     * is empty. If no other product already exists with the same name, a composite product will be generated by adding the
     * base products to the composite product list and the corresponding delivery service method for adding a menu item is called.
     * The product table is then refreshed. Created menu items are inserted at index 0.
     */
    @FXML
    protected void onCreateComposite()
    {
        if(compositeNameTextField.getText().equals(""))
        {
            showAlert("Name of product cannot be empty");
            return;
        }
        BaseProduct entry = new BaseProduct(compositeNameTextField.getText(),0,0,0,0,0,0);
        if(DeliveryService.getInstance().getMenuItems().contains(entry))
        {
            showAlert("Menu item already exists with this name!");
            return;
        }
        ObservableList<MenuItem> entries = itemTable.getSelectionModel().getSelectedItems();
        if(entries.size() <= 1)
        {
            showAlert("Cannot create composite item from less than 2 base items");
        }
        else
        {
            CompositeProduct compositeProduct = new CompositeProduct(compositeNameTextField.getText());
            for(MenuItem menuItem: entries)
            {
                compositeProduct.addItem(menuItem);
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create composite product confirmation");
            StringBuilder s = new StringBuilder("Are you sure that you want to use these items for composite?\n");
            for(MenuItem menuItem: entries)
            {
                s.append(menuItem.getTitle()).append("\n");
            }
            alert.setContentText(s.toString());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    DeliveryService.getInstance().addMenuItem(compositeProduct);
                    refreshTable();
                }
            });
        }
    }

    /**
     * Loads products into the table from the CSV file. Confirmation is asked from the user.
     */
    @FXML
    protected void onLoad() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Load confirmation alert");
        alert.setContentText("Are you sure that you want to load products?\n ORDERS will" +
                "be deleted and\n any current composite products shall be LOST.");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                DeliveryService.getInstance().populateProducts("products.csv");
                DeliveryService.getInstance().setOrderDetails(new HashMap<>());
                MainController.employeeController.removeAllOrders();
                refreshTable();
            }
        });
    }
    //report functions load the appropriate controller
    @FXML
    protected void onGenerateReport1(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("report1-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Report 1 Window");
        stage.setScene(scene);
    }

    @FXML
    protected void onGenerateReport2(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("report2-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Report 2 Window");
        stage.setScene(scene);
    }

    @FXML
    protected void onGenerateReport3(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("report3-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Report 3 Window");
        stage.setScene(scene);
    }

    @FXML
    protected void onGenerateReport4(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("report4-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Report 4 Window");
        stage.setScene(scene);
    }

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

    /**
     * Sets selection model to multiple.
     */
    public void initialize()
    {
        itemTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        addTableColumns();
        refreshTable();
    }
}
