package com.lu.controller;

import com.lu.entity.ConsumerKeyEnum;
import com.lu.entity.ConsumerStartEnum;
import com.lu.entity.ConsumerUntilEnum;
import com.lu.entity.ConsumerValueEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ConsumerController {
    @FXML
    GridPane listGridPane;
    @FXML
    RowConstraints whichOneRow;
    @FXML
    GridPane whichOneGridPane;
    @FXML
    RowConstraints manyRow;
    @FXML
    GridPane manyGridPane;
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
    @FXML
    Button startButton;
    private volatile boolean starting = false;

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
        keyList = FXCollections.observableArrayList(Arrays.stream(ConsumerKeyEnum.values()).map(ConsumerKeyEnum::getKey).collect(Collectors.toList()));
        valueList = FXCollections.observableArrayList(Arrays.stream(ConsumerValueEnum.values()).map(ConsumerValueEnum::getKey).collect(Collectors.toList()));
        startList = FXCollections.observableArrayList(Arrays.stream(ConsumerStartEnum.values()).map(ConsumerStartEnum::getKey).collect(Collectors.toList()));
        utilList = FXCollections.observableArrayList(Arrays.stream(ConsumerUntilEnum.values()).map(ConsumerUntilEnum::getKey).collect(Collectors.toList()));
        topicList.addAll(topics);
        topicChoiceBox.setItems(topicList);
        topicChoiceBox.getSelectionModel().selectFirst();
        keyChoiceBox.setItems(keyList);
        keyChoiceBox.getSelectionModel().selectFirst();
        valueChoiceBox.setItems(valueList);
        valueChoiceBox.getSelectionModel().selectFirst();

        startChoiceBox.setItems(startList);
        startChoiceBox.getSelectionModel().selectFirst();
        startChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldString, newString) -> {
            if (ConsumerStartEnum.OFFSET.equals(ConsumerStartEnum.from(newString))) {
                whichOneRow.setMaxHeight(40);
                whichOneRow.setMinHeight(40);
                whichOneRow.setPrefHeight(40);
                whichOneGridPane.setVisible(true);
            } else {
                whichOneRow.setMaxHeight(0);
                whichOneRow.setMinHeight(0);
                whichOneRow.setPrefHeight(0);
                whichOneGridPane.setVisible(false);
            }
        });

        untilChoiceBox.setItems(utilList);
        untilChoiceBox.getSelectionModel().selectFirst();
        untilChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldString, newString) -> {
            if (ConsumerUntilEnum.NUMBER.equals(ConsumerUntilEnum.from(newString))) {
                manyRow.setMaxHeight(40);
                manyRow.setMinHeight(40);
                manyRow.setPrefHeight(40);
                manyGridPane.setVisible(true);
            } else {
                manyRow.setMaxHeight(0);
                manyRow.setMinHeight(0);
                manyRow.setPrefHeight(0);
                manyGridPane.setVisible(false);
            }
        });

        recordListView.setItems(recordList);
    }

    /**
     * 点击消费
     *
     * @param mouseEvent
     */
    public void clickStart(MouseEvent mouseEvent) {
        if (starting) {
            starting = false;
            startButton.setText("START");
        } else {
            startButton.setText("STOP");
            starting = true;
            asyncConsume();
        }
    }

    /**
     * 异步消费kafka消息
     */
    public void asyncConsume() {
        CompletableFuture.runAsync(() -> {
            String topic = topicChoiceBox.getSelectionModel().getSelectedItem();
            int partition = Integer.parseInt(partitionTextField.getText());
            String key = keyChoiceBox.getSelectionModel().getSelectedItem();
            String value = valueChoiceBox.getSelectionModel().getSelectedItem();
            ConsumerStartEnum startEnum = ConsumerStartEnum.from(startChoiceBox.getSelectionModel().getSelectedItem());
            ConsumerUntilEnum untilEnum = ConsumerUntilEnum.from(untilChoiceBox.getSelectionModel().getSelectedItem());

            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-client-" + System.currentTimeMillis());
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

            if (ConsumerStartEnum.LATEST.equals(startEnum) && ConsumerUntilEnum.FOREVER.equals(untilEnum)) {
                consumeMessage(startEnum, properties, topic, partition);
            } else if (ConsumerStartEnum.LATEST.equals(startEnum) && ConsumerUntilEnum.NUMBER.equals(untilEnum)) {
                int number = Integer.parseInt(numberTextField.getText());
                consumeMessage(startEnum, properties, topic, partition, number);
            } else if (ConsumerStartEnum.EARLIEST.equals(startEnum) && ConsumerUntilEnum.FOREVER.equals(untilEnum)) {
                consumeMessage(startEnum, properties, topic, partition);
            } else if (ConsumerStartEnum.EARLIEST.equals(startEnum) && ConsumerUntilEnum.NUMBER.equals(untilEnum)) {
                int number = Integer.parseInt(numberTextField.getText());
                consumeMessage(startEnum, properties, topic, partition, number);
            } else if (ConsumerStartEnum.OFFSET.equals(startEnum) && ConsumerUntilEnum.FOREVER.equals(untilEnum)) {
                long offset = Long.parseLong(offsetTextField.getText());
                consumeMessage(properties, topic, partition, offset);
            } else if (ConsumerStartEnum.OFFSET.equals(startEnum) && ConsumerUntilEnum.NUMBER.equals(untilEnum)) {
                long offset = Long.parseLong(offsetTextField.getText());
                int number = Integer.parseInt(numberTextField.getText());
                consumeMessage(properties, topic, partition, offset, number);
            }
        });
    }

    public void consumeMessage(Properties properties, String topic, int partition, long offset, int number) {
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, number);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seek(topicPartition, offset);
        refreshRecordList(kafkaConsumer);
    }

    public void consumeMessage(ConsumerStartEnum startEnum, Properties properties, String topic, int partition, int number) {
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, number);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        if (ConsumerStartEnum.LATEST.equals(startEnum)) {
            kafkaConsumer.seekToEnd(Collections.singletonList(topicPartition));
        } else {
            kafkaConsumer.seekToBeginning(Collections.singletonList(topicPartition));
        }
        refreshRecordList(kafkaConsumer);
    }

    public void consumeMessage(Properties properties, String topic, int partition, long offset) {
        int number = 100;
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, number);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seek(topicPartition, offset);
        alwaysRefreshRecordList(kafkaConsumer, number);
    }

    public void consumeMessage(ConsumerStartEnum startEnum, Properties properties, String topic, int partition) {
        int number = 100;
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, number);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        if (ConsumerStartEnum.LATEST.equals(startEnum)) {
            kafkaConsumer.seekToEnd(Collections.singletonList(topicPartition));
        } else {
            kafkaConsumer.seekToBeginning(Collections.singletonList(topicPartition));
        }
        alwaysRefreshRecordList(kafkaConsumer, number);
    }

    /**
     * 刷新消息列表
     *
     * @param kafkaConsumer
     */
    public void refreshRecordList(KafkaConsumer<String, String> kafkaConsumer) {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(10));
        List<ConsumerRecord<String, String>> records = new ArrayList<>();
        consumerRecords.iterator().forEachRemaining(records::add);
        recordList.clear();
        recordList.addAll(records);
    }

    /**
     * 刷新消息列表
     *
     * @param kafkaConsumer
     * @param number
     */
    public void alwaysRefreshRecordList(KafkaConsumer<String, String> kafkaConsumer, int number) {
        ConsumerRecords<String, String> consumerRecords;
        List<ConsumerRecord<String, String>> records = new ArrayList<>();
        while (starting) {
            consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            if (!consumerRecords.isEmpty()) {
                consumerRecords.iterator().forEachRemaining(records::add);
                ConsumerRecord<String, String> lastRecord = records.get(records.size() - 1);
                TopicPartition topicPartition = new TopicPartition(lastRecord.topic(), lastRecord.partition());
                kafkaConsumer.seek(topicPartition, lastRecord.offset() + number);
                recordList.addAll(records);
                records.clear();
            }
            System.out.println(LocalDateTime.now());
        }
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
}
