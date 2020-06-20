package com.lu.controller;

import com.lu.util.KafkaUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.List;

public class ConsumersController extends RootController {
    @FXML
    TableView<String> tableView;
    @FXML
    TableColumn<String, String> consumerGroup;
    AdminClient adminClient;
    ObservableList<String> consumersList;

    public void init() {
        consumersList = FXCollections.observableArrayList();
        List<String> consumers = KafkaUtil.getConsumers(adminClient);
        buildTableView(consumers);
    }

    public void buildTableView(List<String> consumers) {
        consumerGroup.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        consumersList.addAll(consumers);
        tableView.setItems(consumersList);
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }
}
