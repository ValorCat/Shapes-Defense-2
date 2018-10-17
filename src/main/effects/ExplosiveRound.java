package main.effects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import main.Level;

import static main.Level.HEIGHT;
import static main.Level.WIDTH;

public class ExplosiveRound implements Effect {

    private static final double SPEED = 0.1;
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
            level.spawnEffect(new Explosion(x, y, damage, level));
            level.clearEffect(this);
        }
    }

    public Circle getVisual() {
        return visual;
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
