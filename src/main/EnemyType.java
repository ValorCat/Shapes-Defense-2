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
    }, GRAY(60, 3, 0.7, 20) {
        public Shape buildVisual() {
            return new Rectangle(SIZE, SIZE, Color.GRAY);
        }
    }, RED(5, 0, 1.8, 10) {
        public Shape buildVisual() {
            return new Circle(SIZE / 2, Color.ORANGERED);
        }
    }, PURPLE(80, 8, 0.7, 15) {
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
