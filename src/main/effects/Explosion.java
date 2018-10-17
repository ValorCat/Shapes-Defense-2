package main.effects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Enemy;
import main.Level;
import main.Target;

public class Explosion implements Effect {

    private static final double MAX_RADIUS = 20;
    private static final int DURATION = 4;

    private double x, y;
    private int damage;
    private int lifetime;
    private Circle visual;
    private Level level;

    public Explosion(double x, double y, int damage, Level level) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.lifetime = DURATION;
        this.level = level;
        visual = buildVisual();

        double damageLeft = damage;
        for (Enemy enemy : Target.from(x, y).within(MAX_RADIUS).findAllByDistance()) {
            damageLeft = enemy.damage(damageLeft);
            if (damageLeft <= 0) break;
        }
    }

    public void update() {
        if (lifetime-- <= 0) {
            level.clearEffect(this);
        } else {
            visual.setRadius(MAX_RADIUS * (1 - (double) lifetime / DURATION));
        }
    }

    public Circle getVisual() {
        return visual;
    }

    private Circle buildVisual() {
        Circle visual = new Circle(0, Color.TRANSPARENT);
        visual.setStroke(Color.GRAY);
        visual.setStrokeWidth(2);
        visual.setCenterX(x);
        visual.setCenterY(y);
        return visual;
    }

}
