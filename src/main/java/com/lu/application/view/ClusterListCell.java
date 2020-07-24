package com.lu.application.view;

import com.lu.application.model.Cluster;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ClusterListCell extends ListCell<Cluster> {
    @FXML
    private Label clusterNameLabel;
    @FXML
    private Label bootstrapServerLabel;
    @FXML
    private Button configButton;
    @FXML
    private GridPane clusterListGridPane;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Cluster cluster, boolean empty) {
        super.updateItem(cluster, empty);
        if (empty || null == cluster) {
            setText(null);
            setGraphic(null);
        } else {
            if (null == fxmlLoader) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/ClusterListCellFxml.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            clusterNameLabel.setText(cluster.getClusterName());
            bootstrapServerLabel.setText(String.join(",", cluster.getBootstrapServer()));

            setText(null);
            setGraphic(clusterListGridPane);
        }
    }
}
