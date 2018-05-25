package main;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Rectangle;
import main.towers.BombTower;
import main.towers.Tower;
import main.towers.TurretTower;

import java.lang.reflect.InvocationTargetException;

public class Tile {

    public static final int TILE_SIZE = 45;

    private double x, y;
    private int row, col;
    private boolean canPlaceOn, path, start, end;
    private Rectangle visual;
    private Level level;

    public Tile(double x, double y, int row, int col, boolean canPlaceOn, String style,
                boolean start, boolean end, Level level) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.col = col;
        this.canPlaceOn = canPlaceOn;
        this.path = style.equals("path-tile");
        this.start = start;
        this.end = end;
        this.level = level;
        visual = new Rectangle(TILE_SIZE, TILE_SIZE);
        visual.getStyleClass().add(style + "-tile");

        ContextMenu menu = new ContextMenu();
        for (Class<? extends Tower> t : new Class[] {TurretTower.class, BombTower.class}) {
            MenuItem item = new MenuItem(t.getSimpleName());
            item.setOnAction(event -> {
                try {
                    if (canPlaceOn) {
                        level.spawnTower(
                                t.getConstructor(double.class, double.class, Level.class).newInstance(x, y, level));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            });
            menu.getItems().add(item);
        }
        visual.setOnContextMenuRequested(event -> menu.show(visual, Side.RIGHT, event.getX() - TILE_SIZE, event.getY()));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean canPlaceOn() {
        return canPlaceOn;
    }

    public boolean isPath() {
        return path;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }

    public Rectangle getVisual() {
        return visual;
    }

    public String toString() {
        return String.format("T(%d,%d)", col, row);
    }

}
