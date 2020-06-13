package com.lu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClusterController extends RootController implements Initializable {
    @FXML
    public ListView<String> menuListView;
    public ObservableList<String> observableList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableList = FXCollections.observableArrayList();
        observableList.addAll("Topics", "Consumer");
        menuListView.setItems(observableList);
    }
}
