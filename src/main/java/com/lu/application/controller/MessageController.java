package com.lu.application.controller;

import com.lu.application.entity.ConsumerValueEnum;
import com.lu.application.util.JsonUtil;
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
    private String value;

    public void build() {
        keyTextField.setText(consumerRecord.key());
        if (ConsumerValueEnum.JSON.equals(ConsumerValueEnum.from(value))) {
            valueTextArea.setWrapText(false);
            valueTextArea.setText(JsonUtil.convertString2Json(consumerRecord.value()).toPrettyString());
        } else {
            valueTextArea.setText(consumerRecord.value());
        }
    }


    public ConsumerRecord<String, String> getConsumerRecord() {
        return consumerRecord;
    }

    public void setConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
        this.consumerRecord = consumerRecord;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
