package ui;

import dev.cat.RaffleApplication;
import dev.cat.config.FxmlView;
import dev.cat.config.StageManager;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
public class ToolbarTest {

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
            stageManager.switchScene(FxmlView.HOME);
            stageManager.switchToWindowedMode();
        });
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !stage.isFullScreen());
    }


    @Test
    public void shouldGoToHomeScreen(FxRobot robot) throws TimeoutException {
        Parent oldRoot = stage.getScene().getRoot();

        robot.interact(() -> stageManager.switchScene(FxmlView.START));
        Button btn = robot.lookup("#homeButton").queryButton();
        robot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        assertThat(
                robot.lookup("#checkBox")
                        .tryQuery())
                .isPresent();
    }

    @Test
    public void shouldEmptyStageManagerListWhenGoHome(FxRobot robot) throws TimeoutException {
        Parent oldRoot = stage.getScene().getRoot();

        robot.interact(() -> {
            stageManager.switchScene(FxmlView.START);
            stageManager.collectUserData(names, false);
        });
        Button btn = robot.lookup("#homeButton").queryButton();
        robot.interact(btn::fire);

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.getScene().getRoot() != oldRoot);

        assertThat(stageManager.getUserData())
                .isEmpty();
    }


    @Test
    public void shouldGoFullScreen(FxRobot robot) throws TimeoutException {
        Button btn = robot.lookup("#fullScreenButton").queryButton();
        robot.interact(() -> assertThat(stage.isFullScreen()).isFalse());

        robot.interact(btn::fire);
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> stage.isFullScreen());

        robot.interact(() -> {
            assertThat(stage.isFullScreen()).isTrue();
        });
    }

    @Test
    public void shouldGoWindowed(FxRobot robot) throws TimeoutException {
        robot.interact(() -> {
            stageManager.switchToFullScreenMode();
        });
        Button btn = robot.lookup("#fullScreenButton").queryButton();
        robot.interact(btn::fire);
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !stage.isFullScreen());

        robot.interact(() -> {
            assertThat(stage.isFullScreen()).isFalse();
        });
    }

    @Test
    public void shouldChangeActionOnClick(FxRobot robot) {
        Button btn = robot.lookup("#fullScreenButton").queryButton();

        // capture the EventHandler instance BEFORE click
        var handlerBefore = btn.getOnAction();

        robot.interact(btn::fire);
        WaitForAsyncUtils.waitForFxEvents();

        var handlerAfter = btn.getOnAction();
        assertThat(handlerAfter).isNotSameAs(handlerBefore);
    }

    @Test
    public void shouldExitApp(FxRobot robot) {
        Button btn = robot.lookup("#exitButton").queryButton();
        robot.interact(() -> {
            assertThat(stage.isShowing()).isTrue();
        });

        robot.interact(btn::fire);
        WaitForAsyncUtils.waitForFxEvents();

        robot.interact(() -> {
            assertThat(stage.isShowing()).isFalse();
        });
    }
}
