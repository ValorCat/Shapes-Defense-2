package main;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import main.effects.Effect;
import main.towers.Tower;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import static main.Tile.TILE_SIZE;

public class Level {

    private static final int ROWS = 15, COLUMNS = 25;
    public static final double WIDTH = COLUMNS * TILE_SIZE, HEIGHT = ROWS * TILE_SIZE;

    private Tile[][] tiles;
    private GridPane tileVisuals;
    private Pane gameArea;
    private ObservableList<Node> visuals;
    private List<Tower> towers;
    private List<Enemy> enemies;
    private List<Effect> effects;
    private List<Tile> path;

    public Level(InputStream input) {
        Scanner scanner = new Scanner(input);
        String data = "";
        for (int line = 0; line < ROWS; line++) {
            data += scanner.nextLine();
        }
        data = data.replaceAll("\n", "");
        fillTiles(data);
        buildPath(scanner);
        scanner.close();

        gameArea = new Pane(tileVisuals);
        visuals = gameArea.getChildren();
        towers = new CopyOnWriteArrayList<>();
        enemies = new CopyOnWriteArrayList<>();
        effects = new CopyOnWriteArrayList<>();

        Target.setEnemies(enemies);
    }

    public void update() {
        towers.forEach(Tower::update);
        enemies.forEach(Enemy::update);
        effects.forEach(Effect::update);
    }

    public void renderBottom(Node n) {
        visuals.add(1, n);
    }

    public void renderTop(Node n) {
        visuals.add(n);
    }

    public void unrender(Node n) {
        visuals.remove(n);
    }

    public void spawnTower(Tower t) {
        towers.add(t);
        renderBottom(t.getVisual());
    }

    public void clearTower(Tower t) {
        towers.remove(t);
        unrender(t.getVisual());
    }

    public void spawnEnemy(EnemyType type) {
        Enemy e = new Enemy(type, this);
        enemies.add(e);
        renderBottom(e.getVisual());
        renderTop(e.getHealthBar());
    }

    public void clearEnemy(Enemy e) {
        enemies.remove(e);
        unrender(e.getVisual());
        unrender(e.getHealthBar());
    }

    public void spawnEffect(Effect e) {
        effects.add(e);
        renderBottom(e.getVisual());
    }

    public void clearEffect(Effect e) {
        effects.remove(e);
        unrender(e.getVisual());
    }

    public Pane getGameArea() {
        return gameArea;
    }

    public List<Tile> getPath() {
        return path;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    private void fillTiles(String data) {
        tiles = new Tile[ROWS][COLUMNS];
        tileVisuals = new GridPane();
        int inputPos = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                fillTile(row, col, data.charAt(inputPos));
                inputPos++;
            }
        }
    }

    private void fillTile(int row, int col, char code) {
        double x = TILE_SIZE * (col + 0.5);
        double y = TILE_SIZE * (row + 0.5);
        boolean canPlaceOn = false, start = false, end = false;
        String style;
        switch (code) {
            case ' ': style = "basic";  canPlaceOn = true;  break;
            case 'X': style = "blocked";                    break;
            case 'O': style = "path";                       break;
            case 'A': style = "path";   start = true;       break;
            case 'Z': style = "path";   end = true;         break;
            default:
                System.out.printf("Invalid tile code '%c' at (%d,%d)\n", code, row, col);
                System.exit(1);
                return;
        }
        Tile tile = new Tile(x, y, row, col, canPlaceOn, style, start, end, this);
        tiles[row][col] = tile;
        tileVisuals.add(tile.getVisual(), col, row);
    }

    private void buildPath(Scanner scanner) {
        path = new ArrayList<>();
        scanner.useDelimiter("[\\s,]+");
        while (scanner.hasNextInt()) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            path.add(tiles[y][x]);
        }
    }

}
