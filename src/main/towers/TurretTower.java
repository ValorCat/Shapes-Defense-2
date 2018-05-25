package main.towers;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import main.Enemy;
import main.Level;
import main.effects.Projectile;
import main.effects.TurretRound;

import static main.Tile.TILE_SIZE;

public class TurretTower extends Tower {

    private static final int DAMAGE = 8;
    private static final double RANGE = 4 * TILE_SIZE;
    private static final int COOLDOWN = 35;

    private Region head;
    private Enemy target;
    private int cooldown;

    public TurretTower(double x, double y, Level level) {
        super(x, y, RANGE, level);
        target = null;
        cooldown = 0;
    }

    public void update() {
        if (cooldown < COOLDOWN) cooldown++;
        if (target == null || distanceTo(target) > RANGE || !target.isAlive()) {
            target = getClosestEnemy(RANGE);
        }
        if (target != null) {
            head.setRotate(getRotation(target.getX() - x, target.getY() - y));
            if (cooldown >= COOLDOWN) {
                Projectile p = new TurretRound(x, y, target.getX() - x, target.getY() - y, DAMAGE, level);
                level.spawnProjectile(p);
                cooldown = 0;
            }
        }
    }

    protected Region buildVisual() {
        Shape base = buildVisualBase();
        Circle circle = new Circle(10, Color.DIMGRAY);
        Rectangle barrel = new Rectangle(4, 15, Color.BLACK);
        head = new StackPane(circle, barrel);
        return new StackPane(base, head);
    }

}
