package com.test.controllers;

import com.test.models.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Objects;

public class WindowBuilder {
    private String viewPath;
    private int width = 640;
    private int height = 480;
    private String title = "Okno";
    private boolean resizable = false;
    private Object controllerData;

    public WindowBuilder setViewPath(String viewPath){
        this.viewPath = viewPath;
        return this;
    }

    public WindowBuilder setWidth(int width){
        this.width = width;
        return this;
    }

    public WindowBuilder setHeight(int height){
        this.height = height;
        return this;
    }

    public WindowBuilder setTitle(String title){
        this.title = title;
        return this;
    }

    public WindowBuilder setResizable(boolean resizable){
        this.resizable = resizable;
        return this;
    }

    public void setControllerData(Object data){
        this.controllerData = data;
    }

    @FXML
    public Stage build() throws Exception {
        if(Objects.equals(this.viewPath, "") || this.viewPath.isEmpty()){
            throw new Exception("Brak patha do view");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(viewPath));
        Parent employeeView = fxmlLoader.load();

        if(controllerData != null){
            EmployeeController controller = fxmlLoader.getController();
            controller.setData((Employee) controllerData);
        }

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(employeeView, width, height));
        stage.setTitle(title);
        stage.show();
        return stage;
    }
}
