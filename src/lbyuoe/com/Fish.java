package lbyuoe.com;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fish {
    private Image image;
    private double positionX;
    private double positionY;
    private double changeX;
    private double changeY;
    private double width;
    private double height;

    public Fish() {
        positionX = 0;
        positionY = 0;
        changeX = 0;
        changeY = 0;
    }

    public void setImage(String fileName) {
        Image i = new Image(fileName);
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setChange(double x, double y) {
        changeX = x;
        changeY = y;
    }

    public void addChange(double x, double y) {
        changeX += x;
        changeY += y;
    }

    public void update(double time) {
        positionX += changeX * time;
        positionY += changeY * time;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Fish fish) {
        return fish.getBoundary().intersects(this.getBoundary());
    }

    public double getPositionX() {
        return positionX;
    }
}
