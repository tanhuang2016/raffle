package dev.cat.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StartController implements Initializable {

    @FXML
    private Button startButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
