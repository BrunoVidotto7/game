package com.scratch.game.engine;

import com.scratch.game.config.GameConfig;
import com.scratch.game.model.Cell;
import com.scratch.game.model.WinCombination;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Evaluates the winning combinations in the game matrix based on the game configuration.
 */
public class WinEvaluator {

  /**
   * Evaluates the game matrix to find winning combinations.
   *
   * @param m the game matrix
   * @param cfg the game configuration
   * @return a map where the key is the symbol name and the value is a list of winning combination IDs
   */
  public Map<String, List<String>> evaluate(String[][] m, GameConfig cfg) {
    Map<String, List<String>> hits = new HashMap<>();

    Map<String, Long> counts =
        Arrays.stream(m)
            .flatMap(Arrays::stream)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    cfg.getSameSymbolCombos().forEach(combo -> counts.entrySet().stream()
        .filter(e -> e.getValue() >= combo.getCount())
        .forEach(e ->
            hits.computeIfAbsent(e.getKey(), k -> new ArrayList<>())
                .add(combo.getId())));

    for (WinCombination combo : cfg.getLinearCombos()) {
      for (List<Cell> area : combo.getCoveredAreas()) {
        String first = m[area.get(0).getRow()][area.get(0).getCol()];
        if (first == null) continue;
        boolean allSame = area.stream()
            .allMatch(c -> first.equals(m[c.getRow()][c.getCol()]));
        if (allSame) {
          hits.computeIfAbsent(first, k -> new ArrayList<>())
              .add(combo.getId());
        }
      }
    }
    return hits;
  }
}
