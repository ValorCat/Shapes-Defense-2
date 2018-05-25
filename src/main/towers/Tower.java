package main.towers;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import main.Enemy;
import main.Level;

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
        visual.setOnMouseEntered(event -> level.render(rangeVisual));
        visual.setOnMouseExited(event -> level.unrender(rangeVisual));
    }

    public abstract void update();
    protected abstract Region buildVisual();

    public Region getVisual() {
        return visual;
    }

    protected double distanceTo(Enemy target) {
        return Math.hypot(target.getX() - x, target.getY() - y);
    }

    protected Enemy getClosestEnemy(double range) {
        Enemy bestTarget = null;
        double bestDistance = range;
        for (Enemy enemy : level.getEnemies()) {
            double distance = distanceTo(enemy);
            if (distance < bestDistance) {
                bestTarget = enemy;
                bestDistance = distance;
            }
        }
        return bestTarget;
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
