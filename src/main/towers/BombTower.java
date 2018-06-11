package main.towers;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import main.Enemy;
import main.Level;
import main.effects.ExplosiveRound;
import main.effects.Effect;

import static main.Tile.TILE_SIZE;

public class BombTower extends Tower {

    private static final int DAMAGE = 15;
    private static final double RANGE = 3 * TILE_SIZE;
    private static final int COOLDOWN = 90;

    private Enemy target;
    private int cooldown;

    public BombTower(double x, double y, Level level) {
        super(x, y, RANGE, level);
        target = null;
        cooldown = 0;
    }

    public void update() {
        if (cooldown < COOLDOWN) cooldown++;
        if (target == null || target.distanceTo(x, y) > RANGE || !target.isAlive()) {
            target = getClosestEnemy(RANGE);
        }
        if (target != null && cooldown >= COOLDOWN) {
            Effect e = new ExplosiveRound(x, y, target.getX(), target.getY(), DAMAGE, level);
            level.spawnEffect(e);
            cooldown = 0;
        }
    }

    protected Region buildVisual() {
        Shape base = buildVisualBase();
        Circle circle = new Circle(TILE_SIZE * 0.25);
        circle.setFill(Color.MIDNIGHTBLUE);
        return new StackPane(base, circle);
    }

}
