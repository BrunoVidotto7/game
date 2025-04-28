package com.scratch.game.engine;

import com.scratch.game.config.GameConfig;
import com.scratch.game.domain.WinCombination;
import com.scratch.game.domain.WinCombination.Cell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WinEvaluator {

  /**
   * @return map symbol -> list<winCombinationId>
   */
  public Map<String, List<String>> evaluate(String[][] m, GameConfig cfg) {
    Map<String, List<String>> hits = new HashMap<>();

    // (a) SAME_SYMBOL_N_TIMES
    Map<String, Long> counts =
        Arrays.stream(m)
            .flatMap(Arrays::stream)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    cfg.getSameSymbolCombos().forEach(combo -> {
      counts.entrySet().stream()
          .filter(e -> e.getValue() >= combo.getCount())
          .forEach(e ->
              hits.computeIfAbsent(e.getKey(), k -> new ArrayList<>())
                  .add(combo.getId()));
    });

    // (b) LINEAR (horizontal / vertical / diagonal)
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
