package main.towers;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import main.Enemy;
import main.Level;
import main.Target;
import main.effects.LaserBeam;

import static main.Tile.TILE_SIZE;

/**
 * @since 5/30/2018
 */
public class LaserTower extends Tower {

    private static final int DAMAGE = 18;
    private static final double RANGE = 4 * TILE_SIZE;
    private static final int COOLDOWN = 160;

    private Enemy target;
    private int cooldown;

    public LaserTower(double x, double y, Level level) {
        super(x, y, RANGE, level);
        target = null;
        cooldown = 0;
    }

    public void update() {
        if (cooldown < COOLDOWN) cooldown++;
        if (cooldown >= COOLDOWN) {
            Enemy target = Target.from(x, y).within(RANGE).findMax(Enemy::getProgress);
            if (target != null) {
                level.spawnEffect(new LaserBeam(x, y, DAMAGE, target, level));
                cooldown = 0;
            }
        }
    }

    protected Region buildVisual() {
        Shape base = buildVisualBase();
        Circle circle = new Circle(10, Color.MEDIUMVIOLETRED);
        return new StackPane(base, circle);
    }

}
