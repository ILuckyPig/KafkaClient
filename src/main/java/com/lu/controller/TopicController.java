package com.lu.controller;

import com.lu.model.Topic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TopicController extends RootController implements Initializable {
    @FXML
    TableView tableView;
    @FXML
    TableColumn topicNameColumn;
    @FXML
    TableColumn rfColumn;
    @FXML
    TableColumn partitionsColumn;
    @FXML
    TableColumn countColumn;
    @FXML
    TableColumn consumerColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Topic> list = FXCollections.observableArrayList();
    }
}
