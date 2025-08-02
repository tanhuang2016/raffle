package ui;

import dev.cat.RaffleApplication;
import dev.cat.config.StageManager;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
public class HomeSceneTest {

    private StageManager stageManager;
    private Stage stage;
    List<String> names = List.of("Alice", "Mike", "Linda");
    List<String> emails = List.of("alice@mexample.com", "mike@corp.net", "plaintext");


    @Start
    public void start(Stage s) throws Exception {
        this.stage = s;
        new RaffleApplication().start(s);
        stageManager = StageManager.getInstance();
    }

    @BeforeEach
    public void clearData() {
        stageManager.clearUserData();
    }

    @Test
    void shouldContainLabel(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup(".label")
                        .queryAs(Label.class))
                .hasText("Put participants here:");

    }

    @Test
    void shouldContainCheckBox(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#checkBox")
                        .queryAs(CheckBox.class))
                .hasText("Mask emails");

    }

    @Test
    void shouldContainButton(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#continueButton")
                        .queryAs(Button.class))
                .hasText("Continue");

    }

    @Test
    void shouldContainTextArea(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#text")
                        .queryTextInputControl()).isNotNull();

    }

    @Test
    void shouldChangeSceneWhenContinueButtonIsClicked(FxRobot fxRobot) throws TimeoutException {
        Parent oldRoot = stage.getScene().getRoot();

        TextArea area = fxRobot.lookup("#text")
                .queryAs(TextArea.class);

        fxRobot.interact(() ->
                area.setText(String.join(System.lineSeparator(), names)));

        Button btn = fxRobot.lookup("#continueButton").queryButton();
        fxRobot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        assertThat(stage.getScene().getRoot()).isNotSameAs(oldRoot);
        assertThat(
                fxRobot.lookup("#startButton")
                        .queryAs(Button.class)).isNotNull();

    }

    @Test
    void shouldPersistDataFromTextAreaInStageManager(FxRobot fxRobot) throws TimeoutException {
        Parent oldRoot = stage.getScene().getRoot();

        TextArea area = fxRobot.lookup("#text")
                .queryAs(TextArea.class);

        fxRobot.interact(() ->
                area.setText(String.join(System.lineSeparator(), names)));

        Button btn = fxRobot.lookup("#continueButton").queryButton();
        fxRobot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        assertThat(stageManager.getUserData())
                .containsExactlyInAnyOrderElementsOf(names);
    }

    @Test
    void shouldMaskEmails(FxRobot fxRobot) throws TimeoutException {

        Parent oldRoot = stage.getScene().getRoot();

        TextArea area = fxRobot.lookup("#text")
                .queryAs(TextArea.class);
        CheckBox checkBox = fxRobot.lookup("#checkBox")
                .queryAs(CheckBox.class);
        Button btn = fxRobot.lookup("#continueButton").queryButton();

        fxRobot.interact(() ->
                area.setText(String.join(System.lineSeparator(), emails)));
        checkBox.setSelected(true);

        fxRobot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        var expected = List.of("alice*****", "mike*****", "plaintext");
        assertThat(stageManager.getUserData())
                .containsExactlyInAnyOrderElementsOf(expected);


    }

}