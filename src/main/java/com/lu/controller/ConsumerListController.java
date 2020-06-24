package com.lu.controller;

import com.lu.Context;
import com.lu.util.KafkaUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ConsumerListController extends RootController {
    @FXML
    TableView<String> tableView;
    @FXML
    TableColumn<String, String> consumerGroup;
    AdminClient adminClient;
    ObservableList<String> consumersList;

    public void init() {
        consumersList = FXCollections.observableArrayList();
        try {
            List<String> consumers = KafkaUtil.getConsumerGroups(adminClient);
            buildTableView(consumers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void buildTableView(List<String> consumers) {
        consumerGroup.setCellFactory(data -> {
            TableCell<String, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    setText(b ? null : s);
                }
            };
            cell.setOnMouseClicked(mouseEvent -> {
                if (!cell.isEmpty()) {
                    ClusterMainController controller = Context.getController(ClusterMainController.class);
                    controller.clickConsumer(mouseEvent);
                }
            });
            return cell;
        });
        consumerGroup.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        consumersList.addAll(consumers);
        tableView.setItems(consumersList);
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void refresh() {
        try {
            List<String> consumers = KafkaUtil.getConsumerGroups(adminClient);
            consumersList.clear();
            consumersList.addAll(consumers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
