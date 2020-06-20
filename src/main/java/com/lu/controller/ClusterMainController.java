package com.lu.controller;

import com.lu.model.Cluster;
import com.lu.util.KafkaUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private ConsumersController consumersController;
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
        if (null == topicsController) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TopicsFxml.fxml"));
                Parent root = fxmlLoader.load();
                topicsController = fxmlLoader.getController();
                topicsController.setAdminClient(adminClient);
                topicsController.init();
                tableGridPane.add(root,1,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public void clickConsumers(MouseEvent mouseEvent) {
        if (null == consumersController) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ConsumersFxml.fxml"));
                Parent root = fxmlLoader.load();
                consumersController = fxmlLoader.getController();
                tableGridPane.add(root, 1, 0);
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
