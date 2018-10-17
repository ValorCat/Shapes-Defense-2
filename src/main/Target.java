package main;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @since 10/16/2018
 */
public class Target {

    // Target.from(x, y).within(range)
    // ... getClosest, getFarthest, getStrongest

    // Target.from(x, y).within(range).sorted()
    //  .. getAll, getDistances

    // Target.from(enemy).within(range).getClosest();

    private static List<Enemy> enemies;

    private double x, y, radius;
    private Enemy enemyToExclude;

    private Target(double x, double y, Enemy toExclude) {
        this.x = x;
        this.y = y;
        this.radius = -1;
        this.enemyToExclude = toExclude;
    }

    public static void setEnemies(List<Enemy> enemies) {
        Target.enemies = enemies;
    }

    public static Target from(double x, double y) {
        return new Target(x, y, null);
    }

    public static Target from(Enemy enemy) {
        return new Target(enemy.getX(), enemy.getY(), enemy);
    }

    public Target within(double range) {
        this.radius = range;
        return this;
    }

    public List<Enemy> findAll() {
        return getEnemies().collect(toList());
    }

    public List<Enemy> findAllByDistance() {
        return getEnemies()
                .sorted(comparingDouble(e -> e.distanceTo(x, y)))
                .collect(toList());
    }

    public Map<Enemy, Double> findDistances() {
        return getEnemies()
                .collect(toMap(Function.identity(), e -> e.distanceTo(x, y)));
    }

    public List<Pair<Enemy, Double>> findSortedDistances() {
        return getEnemies()
                .map(e -> new Pair<>(e, e.distanceTo(x, y)))
                .sorted(Pair.bySecond())
                .collect(toList());
    }

    public Enemy getClosestEnemy(double x, double y, double range) {
        return enemies.stream()
                .min(comparingDouble(e -> e.distanceTo(x, y)))
                .filter(e -> e.distanceTo(x, y) < range)
                .orElse(null);
    }

    public Enemy closest() {
        return findMin(e -> e.distanceTo(x, y));
    }

    public Enemy findMin(ToDoubleFunction<Enemy> key) {
        return getEnemies()
                .min(comparingDouble(key))
                .orElse(null);
    }

    public Enemy findMax(ToDoubleFunction<Enemy> key) {
        return getEnemies()
                .max(comparingDouble(key))
                .orElse(null);
    }

    private Stream<Enemy> getEnemies() {
        if (enemies == null) throw new IllegalStateException("Enemies list not initialized");
        Stream<Enemy> stream = enemies.stream();
        if (radius > -1) stream = stream.filter(e -> e.distanceTo(x, y) < radius);
        if (enemyToExclude != null) stream = stream.filter(e -> e != enemyToExclude);
        return stream;
    }

}
