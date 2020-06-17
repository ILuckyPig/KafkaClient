package com.lu.controller;

import com.lu.model.Topic;
import com.lu.util.KafkaUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.kafka.clients.admin.AdminClient;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ClusterMainController extends RootController implements Initializable {
    @FXML
    Label clusterNameLabel;
    TopicController topicController;
    List<String> bootstrapServerList;
    AdminClient adminClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String bootstrapServers = String.join(",", bootstrapServerList);
        adminClient = KafkaUtil.getAdminClient(bootstrapServers);

        try {
            // TODO build topic
            List<Topic> topicList = adminClient.listTopics().names().get()
                    .stream()
                    .map(name -> {
                        Topic topic = new Topic();
                        topic.setTopicName(name);
                        return topic;
                    })
                    .collect(Collectors.toList());


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public Label getClusterNameLabel() {
        return clusterNameLabel;
    }

    public void setClusterNameLabel(Label clusterNameLabel) {
        this.clusterNameLabel = clusterNameLabel;
    }

    public TopicController getTopicController() {
        return topicController;
    }

    public void setTopicController(TopicController topicController) {
        this.topicController = topicController;
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
