package main.effects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Enemy;
import main.Level;

import static main.Level.HEIGHT;
import static main.Level.WIDTH;

public class TurretRound implements Projectile {

    private static final double SPEED = 0.4;
    private static final double RADIUS = 4;

    private double x, y, xVelocity, yVelocity;
    private int damage;
    private Circle visual;
    private Level level;

    public TurretRound(double x, double y, double xVelocity, double yVelocity, int damage, Level level) {
        this.x = x;
        this.y = y;
        this.xVelocity = xVelocity * SPEED;
        this.yVelocity = yVelocity * SPEED;
        this.damage = damage;
        this.level = level;
        visual = buildVisual();
    }

    public void update() {
        move();
        Enemy target = getContactedEnemy();
        if (target != null || outOfBounds()) {
            if (target != null) {
                target.damage(damage);
            }
            level.clearProjectile(this);
        }
    }

    public Circle getVisual() {
        return visual;
    }

    private boolean outOfBounds() {
        return x < 0 || x > WIDTH || y < 0 || y > HEIGHT;
    }

    private void move() {
        x += xVelocity;
        y += yVelocity;
        visual.setCenterX(x);
        visual.setCenterY(y);
    }

    private Enemy getContactedEnemy() {
        for (Enemy enemy : level.getEnemies()) {
            if (visual.getBoundsInParent().intersects(enemy.getVisual().getBoundsInParent())) {
                return enemy;
            }
        }
        return null;
    }

    private static Circle buildVisual() {
        return new Circle(RADIUS, Color.DIMGRAY);
    }

}
