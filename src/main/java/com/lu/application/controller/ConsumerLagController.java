package com.lu.application.controller;

import com.lu.application.model.PartitionOffsetAndLag;
import com.lu.application.util.KafkaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ConsumerLagController {
    @FXML
    TableView<PartitionOffsetAndLag> tableView;
    @FXML
    TableColumn<PartitionOffsetAndLag, String> partitionColumn;
    @FXML
    TableColumn<PartitionOffsetAndLag, Long> lagColumn;
    @FXML
    TableColumn<PartitionOffsetAndLag, String> endCurrentColumn;
    @FXML
    Button changeOffsetButton;
    private AdminClient adminClient;
    private KafkaConsumer consumer;
    private String groupId;
    private String bootstrapServer;
    private ObservableList<PartitionOffsetAndLag> offsetAndLagList;

    public void init() {
        offsetAndLagList = FXCollections.observableArrayList();
        consumer = KafkaUtil.getConsumer(groupId, bootstrapServer, StringDeserializer.class.getName(), StringDeserializer.class.getName());
        try {
            List<PartitionOffsetAndLag> consumerLag = KafkaUtil.getConsumerLag(adminClient, consumer, groupId);
            boolean alive = KafkaUtil.consumerAlive(adminClient, groupId);
            buildView(consumerLag);
            changeOffsetButton.setDisable(alive);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void buildView(List<PartitionOffsetAndLag> consumerLag) {
        partitionColumn.setCellValueFactory(new PropertyValueFactory<>("partition"));
        lagColumn.setCellValueFactory(new PropertyValueFactory<>("lag"));
        endCurrentColumn.setCellValueFactory(new PropertyValueFactory<>("endCurrent"));
        offsetAndLagList.addAll(consumerLag);
        tableView.setItems(offsetAndLagList);
    }

    public void refresh() {
        try {
            List<PartitionOffsetAndLag> consumerLag = KafkaUtil.getConsumerLag(adminClient, consumer, groupId);
            boolean alive = KafkaUtil.consumerAlive(adminClient, groupId);
            offsetAndLagList.clear();
            offsetAndLagList.addAll(consumerLag);
            changeOffsetButton.setDisable(alive);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置offset
     *
     * @param mouseEvent
     */
    public void changeOffsets(MouseEvent mouseEvent) {
        try {
            boolean alive = KafkaUtil.consumerAlive(adminClient, groupId);
            changeOffsetButton.setDisable(alive);

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ChangeOffsetsFxml.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public KafkaConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }
}
