package main;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import main.effects.Projectile;
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
    private List<Projectile> projectiles;
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
//        Tile t = tiles[3][5];
//        spawnTower(new TurretTower(t.getX(), t.getY(), this));
//        t = tiles[3][12];
//        spawnTower(new BombTower(t.getX(), t.getY(), this));

        enemies = new CopyOnWriteArrayList<>();

        projectiles = new CopyOnWriteArrayList<>();
    }

    public void update() {
        towers.forEach(Tower::update);
        enemies.forEach(Enemy::update);
        projectiles.forEach(Projectile::update);
    }

    public void render(Node n) {
        visuals.add(n);
    }

    public void unrender(Node n) {
        visuals.remove(n);
    }

    public void spawnTower(Tower t) {
        towers.add(t);
        visuals.add(t.getVisual());
    }

    public void spawnEnemy(EnemyType type) {
        Enemy e = new Enemy(type, this);
        enemies.add(e);
        visuals.add(e.getVisual());
        visuals.add(e.getHealthBar());
    }

    public void clearEnemy(Enemy e) {
        enemies.remove(e);
        visuals.remove(e.getVisual());
        visuals.remove(e.getHealthBar());
    }

    public void spawnProjectile(Projectile p) {
        projectiles.add(p);
        visuals.add(p.getVisual());
    }

    public void clearProjectile(Projectile p) {
        projectiles.remove(p);
        visuals.remove(p.getVisual());
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

    public List<Projectile> getProjectiles() {
        return projectiles;
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
