package main;

import java.util.Comparator;

/**
 * @since 10/16/2018
 */
public class Pair<T, U extends Comparable<U>> {

    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public static <T, U extends Comparable<U>> Comparator<Pair<T, U>> bySecond() {
        return Comparator.comparing(Pair::getSecond);
    }

}
