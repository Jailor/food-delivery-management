package com.andrei.Controller;

import com.andrei.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The Main Controller class. This is first loaded when the application starts. Here the user can choose which view
 * they want to use. Has a show alert method for showing an alert with a message.
 */
public class MainController {

    public static Scene employeeScene = null;
    public static EmployeeController employeeController = null;
    @FXML
    protected void onAdminClick(ActionEvent event) throws IOException {
        toAdmin(event);
    }

    public static void toAdmin(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("admin-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Admin Window");
        stage.setScene(scene);
    }

    @FXML
    protected void onClientClick(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("client-login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Client Login");
        stage.setScene(scene);
    }
    @FXML
    protected void onEmployeeClick(ActionEvent event){
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Employee window");
        stage.setScene(employeeScene);
    }
    public static void showAlert(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR,msg);
        alert.show();
    }

    static void goToMainMenu(ActionEvent event) throws IOException {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        URL fxmlLocation = Main.class.getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
    }


}