package com.lu.application.controller;

import com.lu.application.entity.OffsetEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ChangeOffsetsController {
    @FXML
    ChoiceBox<String> modeChoiceBox;

    private ObservableList<String> modeList;

    public void build() {
        modeList = FXCollections.observableArrayList(Arrays.stream(OffsetEnum.values()).map(OffsetEnum::getKey).collect(Collectors.toList()));
    }
}
