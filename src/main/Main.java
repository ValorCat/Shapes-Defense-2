package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

import static main.EnemyType.*;

public class Main extends Application {

    public void start(Stage primaryStage) {
        InputStream mapData = Main.class.getResourceAsStream("../map.txt");
        Level level = new Level(mapData);

        AnimationTimer clock = new AnimationTimer() {
            public void handle(long now) {
                level.update();
            }
        };

        Scene scene = new Scene(level.getGameArea());
        scene.getStylesheets().add("stylesheet.css");
        scene.setOnKeyTyped(event -> {
            switch (event.getCharacter()) {
                case " ": clock.stop(); break;
                case "`": clock.start(); break;
                case "1": level.spawnEnemy(GREEN); break;
                case "2": level.spawnEnemy(BLUE); break;
                case "3": level.spawnEnemy(RED); break;
                case "4": level.spawnEnemy(GRAY); break;
                case "5": level.spawnEnemy(PURPLE); break;
                case "6": level.spawnEnemy(ORANGE); break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
        clock.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
