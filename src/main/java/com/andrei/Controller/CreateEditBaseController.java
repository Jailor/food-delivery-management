package com.andrei.Controller;

import com.andrei.BusinessLogic.BaseProduct;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import static com.andrei.Controller.MainController.showAlert;


/**
 * Controller for editing and creating base products. The same GUI is used, the only difference being the data
 * that is loaded into the fields or left empty.
 */
public class CreateEditBaseController {

    @FXML
    private TextField titleTextField;
    @FXML
    private TextField ratingTextField;
    @FXML
    private TextField caloriesTextField;
    @FXML
    private TextField proteinTextField;
    @FXML
    private TextField fatTextField;
    @FXML
    private TextField sodiumTextField;
    @FXML
    private TextField priceTextField;

    AdminController adminController;

    BaseProduct baseProduct = null;

    /**
     * Adds/Edits the corresponding base product. If the base product instance is null, means that we are creating.
     * The input is validated and optionally an alert is shown if there are problems with the input. If no error is
     * generated, the product is added using the corresponding method in the delivery service.
     *
     * Similarly, if editing, the input is validated and the editing delegated to the method in the delivery service class.
     */
    @FXML
    protected void onAccept(ActionEvent e) {
        if(baseProduct==null)
        {
            String msg = validateInput(true);
            if(msg != null)
            {
                showAlert(msg);
                return;
            }
            DeliveryService.getInstance().addMenuItem(getProduct());
        }
        else
        {
            String msg = validateInput(false);
            if(msg != null)
            {
                showAlert(msg);
                return;
            }
            BaseProduct updatedProduct = getProduct();
            DeliveryService.getInstance().editBaseProduct(updatedProduct,baseProduct);
        }
        Stage stage =(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.close();
        adminController.refreshTable();
    }
    @FXML
    protected void onCancel(ActionEvent e) {
        Stage stage =(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * When editing, this method is called to load the appropriate data into the text fields.
     * @param toEdit -> product details to load into text fields
     */
    public void loadData(BaseProduct toEdit)
    {
        baseProduct = toEdit;
        titleTextField.setText(toEdit.getTitle());
        ratingTextField.setText(String.valueOf(toEdit.getRating()));
        caloriesTextField.setText(String.valueOf(toEdit.getCalories()));
        proteinTextField.setText(String.valueOf(toEdit.getProtein()));
        sodiumTextField.setText(String.valueOf(toEdit.getSodium()));
        fatTextField.setText(String.valueOf(toEdit.getFat()));
        priceTextField.setText(String.valueOf(toEdit.getPrice()));
    }

    public void setAdminController(AdminController adminController)
    {
        this.adminController = adminController;
    }

    /**
     * Checks if the fields all have good values, and that the title is non-empty. Then checks if all the values read
     * are all positive. If the flag isCreating is specified, the menu item list will be checked for possible duplicates
     * on title. If it is false, the same check is made but the initial value is ignored because it would trigger a false positive.
     * @param isCreating -> boolean value that tells the method whether additional checks are needed when editing/creating
     * @return -> string with error text.
     */
    private String validateInput(boolean isCreating)
    {
        String title = titleTextField.getText();
        if(title.equals("")) return "Title cannot be empty";
        double rating;
        int calories;
        int protein;
        int sodium;
        int fat;
        double price;
        try {
            rating = Double.parseDouble(ratingTextField.getText());
            calories = Integer.parseInt(caloriesTextField.getText());
            protein = Integer.parseInt(proteinTextField.getText());
            sodium = Integer.parseInt(sodiumTextField.getText());
            fat = Integer.parseInt(fatTextField.getText());
            price = Double.parseDouble(priceTextField.getText());
        }catch (NumberFormatException ex)
        {
            return "Cannot parse numbers from text fields!";
        }
        if (rating < 0 || calories < 0|| protein < 0 || sodium < 0|| fat < 0 || price <0)
        {
            return "Negative values are not allowed on any of the text fields!";
        }
        if (price == 0)
        {
            return "Price cannot be 0!";
        }

        BaseProduct entry = new BaseProduct(title,rating,calories,protein,fat,sodium,price);
        if(isCreating)
            if(DeliveryService.getInstance().getMenuItems().contains(entry)) return "Menu item already exists with this name!";
        if(!isCreating)
        {
           for(MenuItem menuItem: DeliveryService.getInstance().getMenuItems())
           {
               if(menuItem.equals(baseProduct)) continue;
               if(menuItem.equals(entry)) return "Menu item already exists with this name";
           }
        }
        return null;
    }

    /**
     * @return a base product with the fields initialised to the corresponding text fields values.
     */
    private BaseProduct getProduct()
    {
        String title = titleTextField.getText();
        double rating = 0;
        int calories = 0;
        int protein = 0;
        int sodium = 0;
        int fat = 0;
        double price = 0;
        try {
            rating = Double.parseDouble(ratingTextField.getText());
            calories = Integer.parseInt(caloriesTextField.getText());
            protein = Integer.parseInt(proteinTextField.getText());
            sodium = Integer.parseInt(sodiumTextField.getText());
            fat = Integer.parseInt(fatTextField.getText());
            price = Double.parseDouble(priceTextField.getText());
        }catch (NumberFormatException ignored)
        {
        }
        return new BaseProduct(title,rating,calories,protein,fat,sodium,price);
    }

}
