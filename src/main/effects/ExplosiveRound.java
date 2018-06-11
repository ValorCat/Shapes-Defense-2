package main.effects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import main.Enemy;
import main.Level;

import java.util.*;
import java.util.Map.Entry;

import static main.Level.HEIGHT;
import static main.Level.WIDTH;

public class ExplosiveRound implements Effect {

    private static final double SPEED = 0.03;
    private static final double RANGE = 60;
    private static final double RADIUS = 5;
    private static final int SPIN_SPEED = 30;

    private double x, y, xTarget, yTarget, xVelocity, yVelocity;
    private int damage;
    private Circle visual;
    private Level level;

    public ExplosiveRound(double x, double y, double xTarget, double yTarget, int damage, Level level) {
        this.x = x;
        this.y = y;
        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.xVelocity = (xTarget - x) * SPEED;
        this.yVelocity = (yTarget - y) * SPEED;
        this.damage = damage;
        this.level = level;
        visual = buildVisual();
    }

    public void update() {
        move();
        visual.setRotate(visual.getRotate() + SPIN_SPEED);
        if (atTarget() || outOfBounds()) {
            double damageLeft = damage;
            List<Entry<Enemy, Double>> targets = getEnemiesInRange();
            for (int i = 0; damageLeft > 0 && i < targets.size(); i++) {
                Enemy enemy = targets.get(i).getKey();
                double distance = targets.get(i).getValue();
                damageLeft = enemy.damage(computeDamage(damageLeft, distance));
            }
            level.clearEffect(this);
        }
    }

    public Circle getVisual() {
        return visual;
    }

    private double computeDamage(double maxDamage, double targetDistance) {
        return maxDamage * (1 - targetDistance / RANGE);
    }

    private List<Entry<Enemy, Double>> getEnemiesInRange() {
        Map<Enemy, Double> enemiesInRange = new HashMap<>();
        for (Enemy enemy : level.getEnemies()) {
            double distance = Math.hypot(x - enemy.getX(), y - enemy.getY());
            if (distance < RANGE) {
                enemiesInRange.put(enemy, distance);
            }
        }
        List<Entry<Enemy, Double>> list = new ArrayList<>(enemiesInRange.entrySet());
        list.sort(Entry.comparingByValue());
        return list;
    }

    private boolean atTarget() {
        return Math.hypot(x - xTarget, y - yTarget) <= 5;
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

    private static Circle buildVisual() {
        Circle visual = new Circle(RADIUS, Color.DIMGRAY);
        visual.setStroke(Color.RED);
        visual.setStrokeType(StrokeType.INSIDE);
        visual.getStrokeDashArray().addAll(3d, 6d);
        return visual;
    }

}
