package main;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

public class Enemy {

    private double x, y;
    private Level level;
    private Tile target;
    private int pathStep;
    private Shape visual;
    private Rectangle healthBar;

    private EnemyType type;
    private int health;
    private double speed;

    public Enemy(EnemyType type, Level level) {
        this.type = type;
        health = type.HEALTH;
        speed = type.SPEED;
        visual = type.buildVisual();
        healthBar = type.buildHealthBar();

        this.level = level;
        this.pathStep = 1;
        target = level.getPath().get(1);
        Tile start = level.getPath().get(0);
        moveTo(start.getX(), start.getY());
    }

    public void update() {
        if (target != null) {
            moveForward(speed);
            visual.setRotate(visual.getRotate() + speed * 2);
        }
    }

    public boolean isAlive() {
        return health > 0 || target == null;
    }

    public int damage(int amount) {
        health -= amount;
        healthBar.setWidth(type.SIZE * health / type.HEALTH);
        if (health <= 0) {
            level.clearEnemy(this);
            return amount;
        }
        return Math.max(0, amount - type.ABSORPTION);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Shape getVisual() {
        return visual;
    }

    public Shape getHealthBar() {
        return healthBar;
    }

    private void moveForward(double amount) {
        double distToTarget = getTargetDistance();
        if (distToTarget <= speed) {
            moveTo(target.getX(), target.getY());
            advanceTarget();
            if (target != null) {
                moveForward(amount - distToTarget);
            }
        } else {
            if (target.getX() < x)      moveTo(x - amount, y);
            else if (target.getX() > x) moveTo(x + amount, y);
            else if (target.getY() < y) moveTo(x, y - amount);
            else                        moveTo(x, y + amount);
        }
    }

    private void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
        healthBar.setLayoutX(x - healthBar.getWidth() / 2);
        healthBar.setLayoutY(y - type.SIZE / 2 - 6);
        if (visual instanceof Rectangle) {
            visual.setLayoutX(x - type.SIZE / 2);
            visual.setLayoutY(y - type.SIZE / 2);
        } else {
            visual.setLayoutX(x);
            visual.setLayoutY(y);
        }
    }

    private double getTargetDistance() {
        return Math.hypot(x - target.getX(), y - target.getY());
    }

    private void advanceTarget() {
        List<Tile> path = level.getPath();
        if (pathStep < path.size() - 1) {
            pathStep++;
            target = path.get(pathStep);
        } else {
            level.clearEnemy(this);
            target = null;
        }
    }

}
