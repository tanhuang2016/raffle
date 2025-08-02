package unit;

import dev.cat.RaffleApplication;
import dev.cat.config.FxmlView;
import dev.cat.config.StageManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class RaffleControllerTest {

    private StageManager stageManager;
    private Stage stage;
    List<String> names = List.of("Alice", "Mike", "Linda", "Jimmy");

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
    void shouldShowNamesFromSetOnly(FxRobot fxRobot) throws TimeoutException {

        Button present = fxRobot.lookup("#presentButton").queryButton();
        Label label   = fxRobot.lookup("#dataLabel").queryAs(Label.class);

        List<String> displayedNames = Collections.synchronizedList(new ArrayList<>());

        fxRobot.interact(() -> label.textProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && !displayedNames.contains(newValue)) {
                        displayedNames.add(newValue);
                    }
                }));

        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        assertThat(displayedNames).allSatisfy(name ->
                assertThat(stageManager.getUserData()).contains(name));

    }

    @Test
    void shouldShuffleNames(FxRobot fxRobot) throws TimeoutException {

        Button present = fxRobot.lookup("#presentButton").queryButton();
        Label label   = fxRobot.lookup("#dataLabel").queryAs(Label.class);

        List<String> displayedNames = Collections.synchronizedList(new ArrayList<>());

        fxRobot.interact(() -> label.textProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && !displayedNames.contains(newValue)) {
                        displayedNames.add(newValue);
                    }
                }));

        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        boolean different = false;
        List<String> initialNames = new ArrayList<>(stageManager.getUserData());
        for(int i = 0; i < initialNames.size(); i++) {
            if(!displayedNames.get(i).equals(initialNames.get(i))) {
                different = true;
                break;
            }
        }
        assertThat(different).isTrue();

    }

    @Test
    void shouldRemoveNameFromSetIfNotPresent(FxRobot fxRobot) throws TimeoutException {

        Button present = fxRobot.lookup("#presentButton").queryButton();
        Button repeat = fxRobot.lookup("#repeatButton").queryButton();

        Label label   = fxRobot.lookup("#dataLabel").queryAs(Label.class);

        List<String> displayedNames = Collections.synchronizedList(new ArrayList<>());

        fxRobot.interact(() -> label.textProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && !displayedNames.contains(newValue)) {
                        displayedNames.add(newValue);
                    }
                }));

        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        String currentName = label.getText();
        fxRobot.interact(repeat::fire);

        List<String> newDisplayedNames = Collections.synchronizedList(new ArrayList<>());

        fxRobot.interact(() -> label.textProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && !displayedNames.contains(newValue)) {
                        newDisplayedNames.add(newValue);
                    }
                }));

        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        assertThat(newDisplayedNames).doesNotContain(currentName);

    }

    @Test
    void shouldShowMax300NamesEvenIfSetContainsMore(FxRobot fxRobot) throws TimeoutException {

        stageManager.clearUserData();
        stageManager.collectUserData(get400Names(), false);

        Button present = fxRobot.lookup("#presentButton").queryButton();
        Label label   = fxRobot.lookup("#dataLabel").queryAs(Label.class);

        List<String> displayedNames = Collections.synchronizedList(new ArrayList<>());

        fxRobot.interact(() -> label.textProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && !displayedNames.contains(newValue)) {
                        displayedNames.add(newValue);
                    }
                }));

        WaitForAsyncUtils.waitFor(7, TimeUnit.SECONDS,
                present::isVisible);

        assertThat(displayedNames.size()).isNotEqualTo(stageManager.getUserData().size());
        assertThat(displayedNames.size()).isLessThanOrEqualTo(300);
    }

    private List<String> get400Names() {

        List<String> names = new ArrayList<>();

        String name = "name";
        for(int i = 0; i <=400; i++) {
          names.add(name + i);
        }
        return names;
    }

}
