package com.lu.controller;

import com.lu.Context;
import com.lu.model.Cluster;
import com.lu.util.JsonUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class AddNewClusterController extends RootController {
    @FXML
    TextField clusterNameTextField;
    @FXML
    TextField bootstrapServerTextField;
    @FXML
    Button saveButton;

    /**
     * 保存新集群
     *
     * @param event
     */
    public void saveNewCluster(ActionEvent event) {
        String clusterName = clusterNameTextField.getText();
        String bootstrapServer = bootstrapServerTextField.getText();
        List<String> split = Arrays.asList(bootstrapServer.split(","));
        Cluster newCluster = new Cluster(clusterName, split);

        MainController mainController = Context.getController(MainController.class);
        mainController.saveNewCluster2List(newCluster);

        try {
            Writer writer = new FileWriter(new File(this.getClass().getResource("/data.json").getFile()));
            JsonUtil.objectMapper.writeValue(writer, mainController.observableList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((Stage) saveButton.getScene().getWindow()).close();
    }
}
