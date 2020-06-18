package com.lu.controller;

import com.lu.model.Topic;
import com.lu.util.KafkaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.kafka.clients.admin.AdminClient;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    List<String> bootstrapServerList;
    AdminClient adminClient;
    ObservableList<Topic> topicList;
    // TODO 如何初始化内部controller的数据
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topicList = FXCollections.observableArrayList();
        String bootstrapServers = String.join(",", bootstrapServerList);
        adminClient = KafkaUtil.getAdminClient(bootstrapServers);

        try {
            Set<String> topics = adminClient.listTopics().names().get();
            List<Topic> topicList = KafkaUtil.getTopics(adminClient, topics);
            buildTableView(topicList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void buildTableView(List<Topic> topics) {
        topicNameColumn.setCellValueFactory(new PropertyValueFactory<>("topicName"));
        rfColumn.setCellValueFactory(new PropertyValueFactory<>("replicationFactor"));
        partitionsColumn.setCellValueFactory(new PropertyValueFactory<>("partitions"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("messageCount"));
        consumerColumn.setCellValueFactory(new PropertyValueFactory<>("consumerCount"));
        topicList.addAll(topics);
        tableView.setItems(topicList);
    }

    public List<String> getBootstrapServerList() {
        return bootstrapServerList;
    }

    public void setBootstrapServerList(List<String> bootstrapServerList) {
        this.bootstrapServerList = bootstrapServerList;
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }
}
