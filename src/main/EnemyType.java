package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public enum EnemyType {

    GREEN(12, 0, 1.2, 12) {
        public Shape buildVisual() {
            return new Rectangle(SIZE, SIZE, Color.MEDIUMSEAGREEN);
        }
    }, BLUE(25, 0, 1, 15) {
        public Shape buildVisual() {
            return new Rectangle(SIZE, SIZE, Color.MEDIUMBLUE);
        }
    }, RED(55, 0, 0.8, 18) {
        public Shape buildVisual() {
            return new Rectangle(SIZE, SIZE, Color.INDIANRED);
        }
    }, GRAY(70, 1, 0.6, 20) {
        public Shape buildVisual() {
            return new Rectangle(SIZE, SIZE, Color.GRAY);
        }
    }, ORANGE(5, 0, 2, 10) {
        public Shape buildVisual() {
            return new Circle(SIZE / 2, Color.ORANGERED);
        }
    }, PURPLE(100, 3, 0.6, 15) {
        public Shape buildVisual() {
            return new Circle(SIZE / 2, Color.FUCHSIA);
        }
    };

    public final int HEALTH, ABSORPTION;
    public final double SPEED, SIZE;

    EnemyType(int health, int absorption, double speed, double size) {
        this.HEALTH = health;
        this.ABSORPTION = absorption;
        this.SPEED = speed;
        this.SIZE = size;
    }

    public Shape buildVisual() {
        throw new UnsupportedOperationException();
    }

    public Rectangle buildHealthBar() {
        return new Rectangle(SIZE, 3, Color.LIMEGREEN);
    }

}
