package com.lu.controller;

import com.lu.model.Topic;
import com.lu.util.KafkaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class TopicsController extends RootController {
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
    AdminClient adminClient;
    ObservableList<Topic> topicList;

    /**
     * 初始化表格数据
     *
     */
    public void init() {
        topicList = FXCollections.observableArrayList();
        try {
            Set<String> topics = adminClient.listTopics().names().get();
            List<Topic> topicList = KafkaUtil.getTopicRfAndPartitions(adminClient, topics);
            buildTableView(topicList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建表格数据
     *
     * @param topics
     */
    public void buildTableView(List<Topic> topics) {
        topicNameColumn.setCellValueFactory(new PropertyValueFactory<>("topicName"));
        rfColumn.setCellValueFactory(new PropertyValueFactory<>("replicationFactor"));
        partitionsColumn.setCellValueFactory(new PropertyValueFactory<>("partitions"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("messageCount"));
        consumerColumn.setCellValueFactory(new PropertyValueFactory<>("consumerCount"));
        topicList.addAll(topics);
        tableView.setItems(topicList);
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }
}
