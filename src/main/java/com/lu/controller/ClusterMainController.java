package com.lu.controller;

import com.lu.model.Cluster;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ClusterMainController extends RootController {
    @FXML
    private Label clusterNameLabel;
    @FXML
    GridPane tableGridPane;
    private TopicController topicController;
    private Cluster cluster;

    public void clickTopics(MouseEvent mouseEvent) {
        if (null == topicController) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TopicsFxml.fxml"));
                Parent root = fxmlLoader.load();
                topicController = fxmlLoader.getController();
                topicController.init(cluster.getBootstrapServer());
                tableGridPane.add(root,1,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
}
