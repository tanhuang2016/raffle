package ui;

import dev.cat.RaffleApplication;
import dev.cat.config.FxmlView;
import dev.cat.config.StageManager;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class StartSceneTest {

    private Stage stage;
    private StageManager stageManager;
    List<String> names = List.of("Alice", "Mike", "Linda");

    @Start
    public void start(Stage s) throws Exception {
        this.stage = s;
        new RaffleApplication().start(s);
        stageManager = StageManager.getInstance();
    }

    @BeforeEach
    void reset(FxRobot robot) throws TimeoutException {
        robot.interact(() -> {
            stageManager.switchScene(FxmlView.START);
        });
    }

    @Test
    void shouldContainButton(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#startButton")
                        .queryAs(Button.class))
                .hasText("Start raffle!");

    }

    @Test
    void shouldContainLabel(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#durationLabel")
                        .queryAs(Label.class))
                .hasText("Choose duration (s):");
    }

    @Test
    void shouldContainComboBox(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#durationBox")
                        .queryComboBox())
                .isNotNull();

    }

    @Test
    void shouldSetRaffleDurationInStageManager(FxRobot fxRobot) throws TimeoutException {
        ComboBox<Integer> comboBox = fxRobot.lookup("#durationBox")
                .queryComboBox();

        fxRobot.interact(() -> {
            comboBox.getSelectionModel().select(Integer.valueOf(5));
        });

        assertThat(stageManager.getRaffleDurationMs()).isEqualTo(5000);

    }

    @Test
    void shouldKeepDefaultDurationInStageManagerIfNotChosen(FxRobot fxRobot) throws TimeoutException {
        Parent oldRoot = stage.getScene().getRoot();
        stageManager.collectUserData(names, false);

        Button btn = fxRobot.lookup("#startButton").queryButton();
        fxRobot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        assertThat(stageManager.getRaffleDurationMs()).isEqualTo(15000);

    }

    @Test
    void shouldGoToRaffleScene(FxRobot fxRobot) throws TimeoutException {
        Parent oldRoot = stage.getScene().getRoot();
        stageManager.collectUserData(names, false);

        Button btn = fxRobot.lookup("#startButton").queryButton();
        fxRobot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        assertThat(stage.getScene().getRoot()).isNotSameAs(oldRoot);
        assertThat(
                fxRobot.lookup("#repeatButton")
                        .queryAs(Button.class)).isNotNull();
    }


}
