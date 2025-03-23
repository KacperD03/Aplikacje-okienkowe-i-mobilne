package com.test.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private Button addEmployeeButton;

    @FXML
    private Button addGroupButton;

    @FXML
    private Button showGroupsButton;

    @FXML
    private void initialize() {
        addEmployeeButton.setOnAction(e -> {
            try {
                openEmployeeForm();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        addGroupButton.setOnAction(e -> {
            try {
                openGroupForm();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        showGroupsButton.setOnAction(e -> {
            try {
                openGroupList();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void openEmployeeForm() throws Exception {
        WindowBuilder builder = new WindowBuilder();
        builder.setViewPath("/com/test/views/EmployeeView.fxml").
                setTitle("Dodaj pracownika").setWidth(640).setHeight(480);
        builder.build();
    }

    private void openGroupForm() throws Exception {
        WindowBuilder builder = new WindowBuilder();
        builder.setViewPath("/com/test/views/NewGroupView.fxml").
                setTitle("Dodaj grupę").setWidth(320).setHeight(240);
        builder.build();
    }

    private void openGroupList() throws Exception {
        WindowBuilder builder = new WindowBuilder();
        builder.setViewPath("/com/test/views/ShowGroupsView.fxml").
                setTitle("Przeglądaj grupy").setWidth(640).setHeight(640);
        builder.build();
    }
}
