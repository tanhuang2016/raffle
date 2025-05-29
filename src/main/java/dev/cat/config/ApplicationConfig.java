package dev.cat.config;

import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Configuration
public class ApplicationConfig {

    private final FxmlLoader fxmlLoader;

    public ApplicationConfig(FxmlLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    @Bean
    @Lazy
    public StageManager stageManager(Stage stage) throws IOException {
        return new StageManager(fxmlLoader, stage);
    }

}
