package com.scratch.game.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * A utility class for selecting random elements based on their weights.
 * 
 * @param <T> the type of elements to be stored and selected
 */
public final class WeightedRandom<T> {
  private final NavigableMap<Double, T> map = new TreeMap<>();
  private double total = 0;

  /**
   * Adds an element with a specified weight to the collection.
   * 
   * @param weight the weight of the element; must be greater than 0
   * @param value  the element to be added
   */
  public void add(double weight, T value) {
    if (weight <= 0) return;
    total += weight;
    map.put(total, value);
  }

  /**
   * Selects a random element from the collection based on their weights.
   * 
   * @param rnd a {@link Random} instance used for generating random numbers
   * @return a randomly selected element
   */
  public T next(Random rnd) {
    double key = rnd.nextDouble() * total;
    return map.higherEntry(key).getValue();
  }
}

