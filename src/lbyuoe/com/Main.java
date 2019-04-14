package lbyuoe.com;

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

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("FishGame");
        // set background
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-image: url('/image/bg.jpg')");
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        // set Canvas
        Canvas canvas = new Canvas(1180, 640);
        pane.getChildren().add(canvas);
        // set keyboard input
        ArrayList<String> input = new ArrayList<String>();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                if (!input.contains(code))
                    input.add(code);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove(code);
            }
        });
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // set score
        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 30);
        gc.setFont(theFont);
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLACK);
        // set master
        Fish master = new Fish();
        master.setImage("file:src/image/master.png");
        master.setPosition(590, 320);
        // set enemy
        ArrayList<Fish> enemyList = new ArrayList<Fish>();
        for (int i = 0; i < 30; i++) {
            enemyList.add(addEnemy());
        }
        final LongValue lastNanoTime = new LongValue(System.nanoTime());
        final IntValue score = new IntValue(0);
        new AnimationTimer() {
            @Override
            public void handle(long t) {
                LongValue currentNanoTime = new LongValue(t);
                double elapsedTime = (currentNanoTime.minus(lastNanoTime)) / 1000000000.0;
                lastNanoTime.value = t;
                master.setChange(0, 0);
                if (input.contains("LEFT"))
                    master.addChange(-500, 0);
                if (input.contains("RIGHT"))
                    master.addChange(500, 0);
                if (input.contains("UP"))
                    master.addChange(0, -500);
                if (input.contains("DOWN"))
                    master.addChange(0, 500);
                master.update(elapsedTime);
                Iterator<Fish> fishIter = enemyList.iterator();
                while (fishIter.hasNext()) {
                    Fish fish = fishIter.next();
                    if (master.intersects(fish)) {
                        fish.setPosition(1180 + 500 * Math.random(), 600 * Math.random());
                        ++score.value;
                    }
                }
                // render
                gc.clearRect(0, 0, 1180, 640);
                master.render(gc);
                for (Fish fish : enemyList) {
                    fish.setChange(-50, 0);
                    fish.addChange(0, 0);
                    if (fish.getPositionX() < -150) {
                        fish.setPosition(1180 + 500 * Math.random(), 600 * Math.random());
                    }
                    fish.update(elapsedTime);
                    fish.render(gc);
                }
                String pointsText = "Eaten fish: " + (score.value);
                gc.fillText(pointsText, 360, 36);
                gc.strokeText(pointsText, 360, 40);
            }
        }.start();
        stage.show();
    }

    public Fish addEnemy() {
        Integer a = (int) (Math.random() * 39 + 1);
        String name = "enemy (" + a.toString() + ").png";
        System.out.println(a);
        Fish enemy = new Fish();
        enemy.setImage("file:src/image/" + name);
        double px = 1180 + 500 * Math.random();
        double py = 600 * Math.random();
        enemy.setPosition(px, py);
        return enemy;
    }
}
