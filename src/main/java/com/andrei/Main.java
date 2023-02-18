package com.andrei;

import com.andrei.BusinessLogic.Client;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
import com.andrei.Controller.MainController;
import com.andrei.DataAccess.Serializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.Hashtable;


public class Main extends Application {
    /**
     * The init() method that is called before the start() method in a JavaFX application.
     * Generates an instance of the class delivery service and adds the observable employee scene.
     * The observable employee scene is kept in memory because the fxmlLoader will create a new
     * instance of a controller every time the .load() is called, so the scene is actually
     * permeated in memory at the start of the application, so it can start observing any changes
     * to the delivery class.
     */
    @Override
    public void init() throws Exception {
        DeliveryService.getInstance();
        URL fxmlLocation = Main.class.getResource("employee-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        DeliveryService.getInstance().addObserver(fxmlLoader.getController());
        MainController.employeeScene = scene;
        MainController.employeeController = fxmlLoader.getController();
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Main Menu");
        stage.getIcons().add(new Image(getClass().getResource("images/icon.png").toExternalForm())); //"file:" + Paths.get("icon.png")
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The stop method called by a javaFX application automatically. Will serialize any and all application data that
     * was changed during the runtime. The serialized data includes client data, menu item data and the hashmap that holds
     * the order details.
     */
    @Override
    public void stop() throws Exception {
        System.out.println("Application exit");
        Serializer<Client> clientSerializer = new Serializer<>(Client.class);
        clientSerializer.writeToFile(DeliveryService.getInstance().getClients());
        Serializer<MenuItem> menuItemSerializer = new Serializer<>(MenuItem.class);
        menuItemSerializer.writeToFile(DeliveryService.getInstance().getMenuItems());
        Serializer.writeHashMap(DeliveryService.getInstance().getOrderDetails());
        super.stop();
    }

    public static void main(String[] args) {
/*        HashMap<String,String> stringMap = new HashMap<>();
        String s = "pula";
        stringMap.put("1",s);
        stringMap.put("2",s);
        stringMap.put("3",s);
        stringMap.values().forEach(System.out::println);
        System.out.println();
        Hashtable<String,String> hashtable = new Hashtable<>();
        hashtable.put("1",s);
        hashtable.put("2",s);
        hashtable.put("3",s);
        hashtable.values().forEach(System.out::println);
        System.out.println();*/
        launch();
    }
}