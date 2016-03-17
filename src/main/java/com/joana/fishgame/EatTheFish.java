package com.joana.fishgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Cardoso on 17-Mar-16.
 */
public class EatTheFish extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Eat the fish!");

        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-image: url('file:sea.png')");
        Scene theScene = new Scene(pane);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(512, 512);
        pane.getChildren().add(canvas);

        final ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        if (!input.contains(code))
                            input.add(code);
                    }
                });

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        input.remove(code);
                    }
                });

        final GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        final Sprite shark = new Sprite();
        shark.setImage("file:shark2.png");
        shark.setPosition(0, 0);

        final ArrayList<Sprite> fishList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Sprite fish = new Sprite();
            fish.setImage("file:fish.png");
            double px = 350 * Math.random() + 50;
            double py = 350 * Math.random() + 50;
            fish.setPosition(px, py);
            fishList.add(fish);
        }

        final LongValue lastNanoTime = new LongValue(System.nanoTime());

        final IntValue score = new IntValue(0);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                // game logic

                shark.setVelocity(0, 0);
                if (input.contains("LEFT"))
                    shark.addVelocity(-150, 0);
                if (input.contains("RIGHT"))
                    shark.addVelocity(150, 0);
                if (input.contains("UP"))
                    shark.addVelocity(0, -150);
                if (input.contains("DOWN"))
                    shark.addVelocity(0, 150);

                shark.update(elapsedTime);

                // collision detection

                Iterator<Sprite> fishIter = fishList.iterator();
                while (fishIter.hasNext()) {
                    Sprite fish = fishIter.next();
                    if (shark.intersects(fish)) {
                        fishIter.remove();
                        score.value++;
                    }
                }

                // render

                gc.clearRect(0, 0, 512, 512);
                shark.render(gc);

                for (Sprite fish : fishList)
                    fish.render(gc);

                String pointsText = "Eaten fish: " + (score.value);
                gc.fillText(pointsText, 360, 36);
                gc.strokeText(pointsText, 360, 36);

            }
        }.start();

        theStage.show();
    }
}