package com.lu.application.controller;

import com.lu.application.model.Topic;
import com.lu.application.util.KafkaUtil;
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
    private TableView<Topic> tableView;
    @FXML
    private TableColumn<Topic, String> topicNameColumn;
    @FXML
    private TableColumn<Topic, Integer> rfColumn;
    @FXML
    private TableColumn<Topic, Integer> partitionsColumn;
    @FXML
    private TableColumn<Topic, Integer> countColumn;
    @FXML
    private TableColumn<Topic, Integer> consumerColumn;
    private AdminClient adminClient;
    private ObservableList<Topic> topicList;
    private Set<String> topics;

    /**
     * 初始化表格数据
     *
     */
    public void init() {
        topicList = FXCollections.observableArrayList();
        try {
            topics = adminClient.listTopics().names().get();
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

    /**
     * 刷新topic列表
     */
    public void refresh() {
        try {
            topics = adminClient.listTopics().names().get();
            List<Topic> topicList = KafkaUtil.getTopicRfAndPartitions(adminClient, topics);
            topicList.clear();
            topicList.addAll(topicList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public ObservableList<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(ObservableList<Topic> topicList) {
        this.topicList = topicList;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }
}
