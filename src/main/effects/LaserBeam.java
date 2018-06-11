package main.effects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import main.Enemy;
import main.Level;

public class LaserBeam implements Effect {

    private static final int DURATION = 80;

    private double x, y;
    private double damage;
    private int lifetime;
    private Enemy target;
    private Line visual;
    private Level level;

    public LaserBeam(double x, double y, int damage, Enemy target, Level level) {
        this.x = x;
        this.y = y;
        this.damage = (double) damage / DURATION;
        this.lifetime = DURATION;
        this.target = target;
        this.level = level;
        visual = buildVisual(x, y);
    }

    public void update() {
        if (!target.isAlive() || lifetime <= 0) {
            level.clearEffect(this);
            return;
        }
        move();
        target.damage(damage);
        double damageLeft = damage;
        for (Enemy enemy : level.getEnemiesByDistance(x, y)) {
            if (visual.getBoundsInParent().intersects(enemy.getVisual().getBoundsInParent())) {
                damageLeft = enemy.damage(damageLeft);
                if (damageLeft <= 0) {
                    blockLaser(enemy.distanceTo(x, y));
                    break;
                }
            }
        }
        lifetime--;
    }

    public Line getVisual() {
        return visual;
    }

    private void move() {
        visual.setEndX(target.getX());
        visual.setEndY(target.getY());
    }

    private void blockLaser(double distance) {
        double angle = Math.atan2(y - target.getY(), x - target.getX());
        visual.setEndX(x - distance * Math.cos(angle));
        visual.setEndY(y - distance * Math.sin(angle));
    }

    private Line buildVisual(double x, double y) {
        Line l = new Line(x, y, target.getX(), target.getY());
        l.setStroke(Color.RED);
        l.setStrokeWidth(2.5);
        return l;
    }

}
