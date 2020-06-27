package com.lu.controller;

import com.lu.model.Cluster;
import com.lu.util.KafkaUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class ClusterMainController extends RootController {
    @FXML
    private Label clusterNameLabel;
    @FXML
    GridPane tableGridPane;
    private TopicsController topicsController;
    private GridPane topicsGridPane;
    private ConsumerListController consumerListController;
    private GridPane consumerListGridPane;
    private final Map<String, GridPane> consumerLagGridPaneMap = new HashMap<>();
    private final Map<String, ConsumerLagController> consumerControllerMap = new HashMap<>();
    private Cluster cluster;
    private AdminClient adminClient;
    private String bootstrapServers;

    /**
     * 创建admin client
     */
    public void build() {
        bootstrapServers = String.join(",", cluster.getBootstrapServer());
        adminClient = KafkaUtil.getAdminClient(bootstrapServers);
        clickTopics(null);
    }

    /**
     * 显示Topics界面
     *
     * @param mouseEvent
     */
    public void clickTopics(MouseEvent mouseEvent) {
        if (null != consumerListGridPane) {
            consumerListGridPane.setVisible(false);
        }
        if (!consumerLagGridPaneMap.isEmpty()) {
            consumerLagGridPaneMap.forEach((key, value) -> value.setVisible(false));
        }
        if (null == topicsGridPane) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TopicsFxml.fxml"));
                topicsGridPane = fxmlLoader.load();
                topicsController = fxmlLoader.getController();
                topicsController.setAdminClient(adminClient);
                topicsController.init();
                tableGridPane.add(topicsGridPane, 1, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            topicsGridPane.setVisible(true);
        }
    }

    /**
     * 显示ConsumerList界面
     *
     * @param mouseEvent
     */
    public void clickConsumerList(MouseEvent mouseEvent) {
        if (null != topicsGridPane) {
            topicsGridPane.setVisible(false);
        }
        if (!consumerLagGridPaneMap.isEmpty()) {
            consumerLagGridPaneMap.forEach((key, value) -> value.setVisible(false));
        }
        if (null == consumerListGridPane) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ConsumerListFxml.fxml"));
                consumerListGridPane = fxmlLoader.load();
                consumerListController = fxmlLoader.getController();
                consumerListController.setAdminClient(adminClient);
                consumerListController.init();
                tableGridPane.add(consumerListGridPane, 1, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            consumerListGridPane.setVisible(true);
        }
    }

    /**
     * 显示ConsumerLag界面
     *
     * @param mouseEvent
     */
    public void clickConsumerLag(MouseEvent mouseEvent) {
        if (null != topicsGridPane) {
            topicsGridPane.setVisible(false);
        }
        if (null != consumerListGridPane) {
            consumerListGridPane.setVisible(false);
        }
        TableCell<String, String> cell = (TableCell<String, String>) mouseEvent.getSource();
        String groupId = cell.getText();
        GridPane consumerGridPane;
        try {
            if (!consumerLagGridPaneMap.containsKey(groupId)) {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ConsumerLagFxml.fxml"));
                consumerGridPane = fxmlLoader.load();
                ConsumerLagController consumerLagController = fxmlLoader.getController();
                consumerLagController.setAdminClient(adminClient);
                consumerLagController.setBootstrapServer(bootstrapServers);
                consumerLagController.setGroupId(groupId);
                consumerLagController.init();
                tableGridPane.add(consumerGridPane, 1, 0);
                consumerLagGridPaneMap.put(groupId, consumerGridPane);
                consumerControllerMap.put(groupId, consumerLagController);
            } else {
                consumerGridPane = consumerLagGridPaneMap.get(groupId);
                consumerGridPane.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新topic、consumer、consumer lag界面
     *
     * @param mouseEvent
     */
    public void clickRefresh(MouseEvent mouseEvent) {
        if (null != topicsGridPane && topicsGridPane.visibleProperty().get()) {
            topicsController.refresh();
            return;
        }

        if (null != consumerListGridPane && consumerListGridPane.visibleProperty().get()) {
            consumerListController.refresh();
            return;
        }

        consumerLagGridPaneMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().visibleProperty().get())
                .forEach(entry -> {
                    ConsumerLagController consumerLagController = consumerControllerMap.get(entry.getKey());
                    consumerLagController.refresh();
                });
    }

    public void clickConsumer(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ConsumerFxml.fxml"));
            Parent root = fxmlLoader.load();
            ConsumerController consumerController = fxmlLoader.getController();
            Set<String> topics = adminClient.listTopics().names().get();
            KafkaConsumer consumer = KafkaUtil.getConsumer("kafka-client-" + System.currentTimeMillis(), bootstrapServers,
                    StringDeserializer.class.getName(), StringDeserializer.class.getName());
            consumerController.setTopics(topics);
            consumerController.setConsumer(consumer);
            consumerController.build();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
