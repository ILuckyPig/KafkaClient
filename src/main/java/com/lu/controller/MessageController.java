package com.lu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class MessageController {
    private ConsumerRecord<String, String> consumerRecord;
    @FXML
    private TextField keyTextField;
    @FXML
    private TextArea valueTextArea;

    public void build() {
        keyTextField.setText(consumerRecord.key());
        valueTextArea.setText(consumerRecord.value());
    }


    public ConsumerRecord<String, String> getConsumerRecord() {
        return consumerRecord;
    }

    public void setConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
        this.consumerRecord = consumerRecord;
    }
}
