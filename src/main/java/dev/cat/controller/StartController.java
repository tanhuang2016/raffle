package dev.cat.controller;

import dev.cat.config.FxmlView;
import dev.cat.config.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public class StartController implements Initializable {

    @FXML
    private Button startButton;

    private final StageManager stageManager = StageManager.getInstance();



    @FXML
    void startRaffle() throws Exception {
        stageManager.switchToNextScene(FxmlView.RAFFLE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
