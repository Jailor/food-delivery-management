package com.andrei.Controller;

import com.andrei.BusinessLogic.Client;
import com.andrei.BusinessLogic.DeliveryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import static com.andrei.Controller.MainController.showAlert;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private PasswordField passwordField2;

    @FXML
    protected void onCancel(ActionEvent event)
    {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * On accept, the fields are parsed. Empty fields are not allowed.
     * Username already in database is also checked and rejected if a match is found.
     * If the passwords do not match, the registration is cancelled again.
     */
    @FXML
    protected void onAccept(ActionEvent event)
    {
        if(usernameField.getText().equals("") ||
                phoneNumberField.getText().equals("") ||
                passwordField1.getText().equals("")||
                passwordField2.getText().equals(""))
        {
            showAlert("Empty fields are not allowed!");
            return;
        }
        String username = usernameField.getText();
        DeliveryService deliveryService = DeliveryService.getInstance();
        for (Client client: deliveryService.getClients())
        {
            if(client.getUsername().equals(username))
            {
                showAlert("Username already in the database!");
                return;
            }
        }
        String phoneNumber = phoneNumberField.getText();
        String pass1 = passwordField1.getText();
        String pass2 = passwordField2.getText();
        if(!pass1.equals(pass2))
        {
            showAlert("Passwords do not match!");
            return;
        }
        Client client = new Client(username,pass1,phoneNumber);
        System.out.println("Created client:" + client);
        //update list of clients
        deliveryService.getClients().add(client);
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

}
