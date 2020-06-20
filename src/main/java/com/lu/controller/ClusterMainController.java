package com.lu.controller;

import com.lu.model.Cluster;
import com.lu.util.KafkaUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.kafka.clients.admin.AdminClient;

import java.io.IOException;

public class ClusterMainController extends RootController {
    @FXML
    private Label clusterNameLabel;
    @FXML
    GridPane tableGridPane;
    private TopicsController topicsController;
    private GridPane topicsGridPane;
    private ConsumersController consumersController;
    private GridPane consumersGridPane;
    private Cluster cluster;
    private AdminClient adminClient;

    /**
     * 创建admin client
     */
    public void build() {
        String bootstrapServers = String.join(",", cluster.getBootstrapServer());
        adminClient = KafkaUtil.getAdminClient(bootstrapServers);
    }

    /**
     * 显示Topics界面
     *
     * @param mouseEvent
     */
    public void clickTopics(MouseEvent mouseEvent) {
        if (null == topicsGridPane) {
            if (null != consumersGridPane) {
                consumersGridPane.setVisible(false);
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TopicsFxml.fxml"));
                topicsGridPane = fxmlLoader.load();
                topicsController = fxmlLoader.getController();
                topicsController.setAdminClient(adminClient);
                topicsController.init();
                tableGridPane.add(topicsGridPane,1,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            topicsGridPane.setVisible(true);
            if (null != consumersGridPane) {
                consumersGridPane.setVisible(false);
            }
        }
    }

    /**
     * 显示Consumers界面
     *
     * @param mouseEvent
     */
    public void clickConsumers(MouseEvent mouseEvent) {
        if (null == consumersGridPane) {
            if (null != topicsGridPane) {
                topicsGridPane.setVisible(false);
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ConsumersFxml.fxml"));
                consumersGridPane = fxmlLoader.load();
                consumersController = fxmlLoader.getController();
                consumersController.setAdminClient(adminClient);
                consumersController.init();
                tableGridPane.add(consumersGridPane, 1, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            consumersGridPane.setVisible(true);
            if (null != topicsGridPane) {
                topicsGridPane.setVisible(false);
            }
        }
    }

    public Label getClusterNameLabel() {
        return clusterNameLabel;
    }

    public void setClusterNameLabel(Label clusterNameLabel) {
        this.clusterNameLabel = clusterNameLabel;
    }

    public TopicsController getTopicController() {
        return topicsController;
    }

    public void setTopicController(TopicsController topicsController) {
        this.topicsController = topicsController;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
}
