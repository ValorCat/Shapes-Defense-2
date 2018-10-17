package main.towers;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import main.Enemy;
import main.Level;
import main.Target;
import main.effects.ElectricArc;

import static main.Tile.TILE_SIZE;

/**
 * @since 5/30/2018
 */
public class ShockTower extends Tower {

    private static final int DAMAGE = 4;
    private static final double RANGE = 2 * TILE_SIZE;
    private static final int COOLDOWN = 25;

    private Enemy target;
    private int cooldown;

    public ShockTower(double x, double y, Level level) {
        super(x, y, RANGE, level);
        target = null;
        cooldown = 0;
    }

    public void update() {
        if (cooldown < COOLDOWN) cooldown++;
        if (cooldown >= COOLDOWN) {
            Enemy target = Target.from(x, y).within(RANGE).closest();
            if (target != null) {
                level.spawnEffect(new ElectricArc(x, y, DAMAGE, 3, target, level));
                cooldown = 0;
            }
        }
    }

    protected Region buildVisual() {
        Shape base = buildVisualBase();
        Circle circle = new Circle(10, Color.LIGHTSTEELBLUE);
        return new StackPane(base, circle);
    }

}
