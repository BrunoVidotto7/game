package com.scratch.game.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public final class WeightedRandom<T> {
  private final NavigableMap<Double, T> map = new TreeMap<>();
  private double total = 0;

  public void add(double weight, T value) {
    if (weight <= 0) return;
    total += weight;
    map.put(total, value);
  }

  public T next(Random rnd) {
    double key = rnd.nextDouble() * total;
    return map.higherEntry(key).getValue();
  }
}

