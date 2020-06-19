package com.lu.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lu.model.Cluster;
import com.lu.util.JsonUtil;
import com.lu.view.ClusterListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends RootController implements Initializable {
    @FXML
    public ListView<Cluster> clusterListView;
    @FXML
    private Button addNewClusterButton;
    public ObservableList<Cluster> observableList;

    public MainController() {
        observableList = FXCollections.observableArrayList();
        try {
            observableList.addAll(JsonUtil.objectMapper.readValue(this.getClass().getResource("/data.json"),
                    new TypeReference<List<Cluster>>() {}));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clusterListView.setItems(observableList);
        clusterListView.setOnMouseClicked(this::openClusterMain);
        clusterListView.setCellFactory(clusterList -> {
            ClusterListCell clusterListCell = new ClusterListCell();
            clusterListCell.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                clusterList.getSelectionModel().clearSelection();
            });
            return clusterListCell;
        });
    }

    /**
     * 显示添加新集群界面
     *
     * @param event
     */
    public void drawAddNewCluster(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/AddNewClusterFxml.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向主界面集群列表添加新集群
     *
     * @param newCluster
     */
    public void saveNewCluster2List(Cluster newCluster) {
        observableList.add(newCluster);
    }

    /**
     * 打开集群界面
     *
     * @param event
     */
    public void openClusterMain(MouseEvent event) {
        try {
            Cluster cluster = clusterListView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ClusterMainFxml.fxml"));
            Parent root = fxmlLoader.load();
            ClusterMainController clusterMainController = fxmlLoader.getController();
            clusterMainController.setCluster(cluster);
            clusterMainController.getClusterNameLabel().setText(cluster.getClusterName());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
