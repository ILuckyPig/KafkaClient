package com.lu.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/MainFxml.fxml"));
        primaryStage.setTitle("Kafka Client");
        primaryStage.setScene(new Scene(root, 700, 375));
        primaryStage.show();
    }
}
