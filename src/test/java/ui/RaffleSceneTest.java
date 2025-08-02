package ui;

import dev.cat.RaffleApplication;
import dev.cat.config.FxmlView;
import dev.cat.config.StageManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
public class RaffleSceneTest {

    private StageManager stageManager;
    private Stage stage;
    List<String> names = List.of("Alice", "Mike", "Linda");

    @Start
    public void start(Stage s) throws Exception {
        this.stage = s;
        new RaffleApplication().start(s);
        stageManager = StageManager.getInstance();
    }

    @BeforeEach
    public void clearData(FxRobot fxRobot) {
        fxRobot.interact(() -> {
            stageManager.clearUserData();
            stageManager.collectUserData(names, false);
            stageManager.setRaffleDurationMs(5);
            stageManager.switchScene(FxmlView.RAFFLE);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void shouldContainLabel(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#dataLabel")
                        .queryAs(Label.class))
                .isNotNull();

    }

    @Test
    void shouldContainPresentButton(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#presentButton")
                        .queryAs(Button.class))
                .hasText("Present");

    }

    @Test
    void shouldContainRepeatButton(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#repeatButton")
                        .queryAs(Button.class))
                .hasText("Start again");

    }

    @Test
    void shouldContainImages(FxRobot fxRobot) {
        assertThat(
                fxRobot.lookup("#bottomImage")
                        .queryAs(ImageView.class))
                .isNotNull();
        assertThat(
                fxRobot.lookup("#topImage")
                        .queryAs(ImageView.class))
                .isNotNull();

    }

    @Test
    void shouldHideAndDisableButtonsWhenRaffling(FxRobot fxRobot) {

        Button present = fxRobot.lookup("#presentButton").queryButton();
        Button repeat = fxRobot.lookup("#repeatButton").queryButton();

        assertThat(present.isVisible()).isFalse();
        assertThat(present.isDisabled()).isTrue();
        assertThat(repeat.isVisible()).isFalse();
        assertThat(repeat.isDisabled()).isTrue();

    }

    @Test
    void shouldShowAndEnableButtonsWhenResultShows(FxRobot fxRobot) throws TimeoutException {
        Button present = fxRobot.lookup("#presentButton").queryButton();
        Button repeat = fxRobot.lookup("#repeatButton").queryButton();


        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        assertThat(present.isVisible()).isTrue();
        assertThat(present.isDisabled()).isFalse();
        assertThat(repeat.isVisible()).isTrue();
        assertThat(repeat.isDisabled()).isFalse();

    }

    @Test
    void shouldHideAndDisableButtonsWhenCongratulating(FxRobot fxRobot) throws TimeoutException {

        Button present = fxRobot.lookup("#presentButton").queryButton();
        Button repeat = fxRobot.lookup("#repeatButton").queryButton();


        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        fxRobot.interact(present::fire);
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(present.isVisible()).isFalse();
        assertThat(present.isDisabled()).isTrue();
        assertThat(repeat.isVisible()).isFalse();
        assertThat(repeat.isDisabled()).isTrue();

    }

    @Test
    void shouldPlayAnimationWhenCongratulating(FxRobot fxRobot) throws TimeoutException {

        Button present = fxRobot.lookup("#presentButton").queryButton();
        ImageView topImage = fxRobot.lookup("#topImage").queryAs(ImageView.class);
        ImageView bottomImage = fxRobot.lookup("#bottomImage").queryAs(ImageView.class);


        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        fxRobot.interact(present::fire);
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(topImage.getFitHeight()).isEqualTo(500.0);
        assertThat(bottomImage.getFitHeight()).isEqualTo(500.0);

    }


    @Test
    void shouldChangeLabelStyleWhenCongratulating(FxRobot fxRobot) throws TimeoutException {

        Label label = fxRobot.lookup("#dataLabel").queryAs(Label.class);
        Button present = fxRobot.lookup("#presentButton").queryButton();

        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        fxRobot.interact(present::fire);
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(label.getText())
                .startsWith("Congratulations, ");

    }



}
