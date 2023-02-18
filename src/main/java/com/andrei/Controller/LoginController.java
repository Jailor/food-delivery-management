package com.andrei.Controller;

import com.andrei.BusinessLogic.Client;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.andrei.Controller.MainController.goToMainMenu;
import static com.andrei.Controller.MainController.showAlert;

/**
 * This class offers action handlers for the login part of the application. A username and password fields are present.
 * Depending on the action chosen, the client logs in successfully or has an error while logging or is sent to registration.
 */
public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    protected void onMainMenu(ActionEvent event) throws IOException {
        goToMainMenu(event);
    }

    /**
     * Tries to log in the user. Parses the text from the Text fields then checks if the client exists in the
     * backend. If the client exists, the password is checked, otherwise it will tell the user that the username does
     * not exist. If the password matches, the user is logged in and the view is changed to the client view. Otherwise,
     * the user is informed about the password mismatch.
     */
    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        if(usernameField.getText().equals("")||
            passwordField.getText().equals(""))
        {
            showAlert("Username and password fields cannot be empty!");
            return;
        }
        String username = usernameField.getText();
        String password = passwordField.getText();
        for (Client client: DeliveryService.getInstance().getClients())
        {
            if(client.getUsername().equals(username))
            {
                if(client.getPassword().equals(password))
                {
                    Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
                    URL fxmlLocation = Main.class.getResource("client-view.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
                    ((ClientController)fxmlLoader.getController()).setClient(client);
                    stage.setScene(scene);
                }
                else showAlert("Incorrect password!");
                return;
            }
        }
        showAlert("Username not found in database!");
    }

    /**
     * Loads the view that allows the client to register a new account.
     */
    @FXML
    protected void onRegisterClick() throws IOException {
        Stage stage = new Stage();
        URL fxmlLocation = Main.class.getResource("client-register.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setScene(scene);
        stage.show();
    }

}
