package main.towers;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import main.Enemy;
import main.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static main.Tile.TILE_SIZE;

public abstract class Tower {

    private static final double SIZE = 0.8 * TILE_SIZE;

    protected double x, y;
    protected Level level;
    private Region visual;
    private Shape rangeVisual;

    protected Tower(double x, double y, double range, Level level) {
        this.x = x;
        this.y = y;
        this.level = level;
        rangeVisual = buildRangeVisual(range);
        visual = buildVisual();
        visual.setLayoutX(x - SIZE / 2 - 1);
        visual.setLayoutY(y - SIZE / 2 - 1);
        visual.setOnMouseEntered(event -> level.renderTop(rangeVisual));
        visual.setOnMouseExited(event -> level.unrender(rangeVisual));
    }

    public abstract void update();
    protected abstract Region buildVisual();

    public Region getVisual() {
        return visual;
    }

    protected Enemy getClosestEnemy(double range) {
        List<Enemy> enemies = level.getEnemiesByDistance(x, y);
        if (enemies.get(0).distanceTo(x, y) <= range)
            return enemies.get(0);
        return null;
    }

    protected Enemy getFarthestEnemy(double range) {
        List<Enemy> enemies = new ArrayList<>(level.getEnemies());
        enemies.sort(Comparator.comparingDouble(enemy -> -enemy.getProgress()));
        for (Enemy e : enemies) {
            if (e.distanceTo(x, y) < range) {
                return e;
            }
        }
        return null;
    }

    protected Shape buildVisualBase() {
        Rectangle square = new Rectangle(SIZE, SIZE);
        square.getStyleClass().add("tower-base");
        return square;
    }

    protected Shape buildRangeVisual(double range) {
        Circle circle = new Circle(x, y, range, Color.TRANSPARENT);
        circle.setStroke(Color.GRAY);
        circle.setStrokeWidth(2);
        circle.setMouseTransparent(true);
        return circle;
    }

    protected static double getRotation(double x, double y) {
        return 90 + Math.toDegrees(Math.atan2(y, x));
    }


}
