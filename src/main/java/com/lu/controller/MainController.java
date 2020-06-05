package com.lu.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.model.Cluster;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ListView<Cluster> clusterListView;
    @FXML
    private Button addNewClusterButton;
    private ObservableList<Cluster> observableList;

    public MainController() {
        observableList = FXCollections.observableArrayList();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Cluster> clusterList = null;
        try {
            clusterList = objectMapper.readValue(this.getClass().getResource("/data.json"),
                    new TypeReference<List<Cluster>>() {});
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        observableList.addAll(clusterList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clusterListView.setItems(observableList);
        clusterListView.setCellFactory(clusterList -> new ClusterListCell());
    }

    public void addNewCluster(ActionEvent event) {
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
}
