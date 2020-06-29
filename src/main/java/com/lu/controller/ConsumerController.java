package com.lu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class ConsumerController {
    @FXML
    TextField partitionTextField;
    @FXML
    TextField offsetTextField;
    @FXML
    TextField numberTextField;
    @FXML
    ChoiceBox<String> topicChoiceBox;
    @FXML
    ChoiceBox<String> keyChoiceBox;
    @FXML
    ChoiceBox<String> valueChoiceBox;
    @FXML
    ChoiceBox<String> startChoiceBox;
    @FXML
    ChoiceBox<String> untilChoiceBox;
    @FXML
    ListView<ConsumerRecord> recordListView;
    private ObservableList<String> topicList;
    private ObservableList<String> keyList;
    private ObservableList<String> valueList;
    private ObservableList<String> startList;
    private ObservableList<String> utilList;
    private ObservableList<ConsumerRecord> recordList = FXCollections.observableArrayList();
    private Set<String> topics;
    private KafkaConsumer consumer;
    private String bootstrapServers;

    public void build() {
        topicList = FXCollections.observableArrayList();
        keyList = FXCollections.observableArrayList("string", "JSON");
        valueList = FXCollections.observableArrayList("string", "JSON");
        startList = FXCollections.observableArrayList("latest", "earliest", "an offset");
        utilList = FXCollections.observableArrayList("forever", "number of messages", "an offset");
        topicList.addAll(topics);
        topicChoiceBox.setItems(topicList);
        topicChoiceBox.getSelectionModel().selectFirst();
        keyChoiceBox.setItems(keyList);
        keyChoiceBox.getSelectionModel().selectFirst();
        valueChoiceBox.setItems(valueList);
        valueChoiceBox.getSelectionModel().selectFirst();
        startChoiceBox.setItems(startList);
        startChoiceBox.getSelectionModel().selectFirst();
        untilChoiceBox.setItems(utilList);
        untilChoiceBox.getSelectionModel().selectFirst();
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

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void clickStart(MouseEvent mouseEvent) {
        String topic = topicChoiceBox.getSelectionModel().getSelectedItem();
        int partition = Integer.parseInt(partitionTextField.getText());
        String key = keyChoiceBox.getSelectionModel().getSelectedItem();
        String value = valueChoiceBox.getSelectionModel().getSelectedItem();
        String start = startChoiceBox.getSelectionModel().getSelectedItem();
        long offset = Long.parseLong(offsetTextField.getText());
        String until = untilChoiceBox.getSelectionModel().getSelectedItem();
        int number = Integer.parseInt(numberTextField.getText());
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-client");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, number);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.seek(topicPartition, offset);
        // TODO IllegalStateException: No current assignment for partition
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            recordList.add(consumerRecord);
        }
    }
}
