package com.lu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClusterMainController extends RootController {
    @FXML
    Label clusterNameLabel;
    @FXML
    private TopicController topicController;

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
}
