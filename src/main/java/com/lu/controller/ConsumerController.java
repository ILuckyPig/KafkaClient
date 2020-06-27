package com.lu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Set;

public class ConsumerController {
    @FXML
    ChoiceBox<String> topicChoiceBox;
    @FXML
    ChoiceBox<String> keyChoiceBox;
    @FXML
    ChoiceBox<String> valueChoiceBox;
    @FXML
    ChoiceBox<String> startChoiceBox;
    @FXML
    ChoiceBox<String> utilChoiceBox;
    private ObservableList<String> topicList;
    private ObservableList<String> keyList;
    private ObservableList<String> valueList;
    private ObservableList<String> startList;
    private ObservableList<String> utilList;
    private Set<String> topics;
    private KafkaConsumer consumer;

    public void build() {
        topicList = FXCollections.observableArrayList();
        keyList = FXCollections.observableArrayList("string", "JSON");
        valueList = FXCollections.observableArrayList("string", "JSON");
        startList = FXCollections.observableArrayList("latest", "earliest", "an offset");
        utilList = FXCollections.observableArrayList("forever", "number of messages", "an offset");
        topicList.addAll(topics);
        topicChoiceBox.setItems(topicList);
        topicChoiceBox.setValue(topicList.get(0));
        keyChoiceBox.setItems(keyList);
        keyChoiceBox.setValue(keyList.get(0));
        valueChoiceBox.setItems(valueList);
        valueChoiceBox.setValue(valueList.get(0));
        startChoiceBox.setItems(startList);
        startChoiceBox.setValue(startList.get(0));
        utilChoiceBox.setItems(utilList);
        utilChoiceBox.setValue(utilList.get(0));
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public KafkaConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    public void clickStart(MouseEvent mouseEvent) {
        // TODO consumer data
    }
}
