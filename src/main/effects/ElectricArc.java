package main.effects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import main.Enemy;
import main.Level;
import main.Target;

public class ElectricArc implements Effect {

    private static final int DURATION = 3;
    private static final double CHAIN_RANGE = 25;

    private double x, y;
    private int damage;
    private int chains;
    private int lifetime;
    private Enemy target;
    private Line visual;
    private Level level;

    public ElectricArc(double x, double y, int damage, int chains, Enemy target, Level level) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.chains = chains;
        this.lifetime = DURATION;
        this.target = target;
        this.level = level;
        visual = buildVisual(x, y);

        target.damage(damage);
        if (chains > 0) {
            double nextX = target.getX();
            double nextY = target.getY();
            Enemy nextTarget = Target.from(target).within(CHAIN_RANGE).closest();
            if (nextTarget != null) {
                level.spawnEffect(new ElectricArc(nextX, nextY, damage, chains - 1, nextTarget, level));
            }
        }
    }

    public void update() {
        if (lifetime-- <= 0) {
            level.clearEffect(this);
        }
    }

    public Line getVisual() {
        return visual;
    }

    private void blockLaser(double distance) {
        double angle = Math.atan2(y - target.getY(), x - target.getX());
        visual.setEndX(x - distance * Math.cos(angle));
        visual.setEndY(y - distance * Math.sin(angle));
    }

    private Line buildVisual(double x, double y) {
        Line l = new Line(x, y, target.getX(), target.getY());
        l.setStroke(Color.CORNFLOWERBLUE);
        l.setStrokeWidth(1.5);
        return l;
    }

}
