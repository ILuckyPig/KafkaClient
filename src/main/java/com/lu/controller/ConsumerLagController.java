package com.lu.controller;

import com.lu.model.PartitionOffsetAndLag;
import com.lu.util.KafkaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.awt.event.MouseEvent;
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
            buildView(consumerLag);
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
            offsetAndLagList.clear();
            offsetAndLagList.addAll(consumerLag);
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
