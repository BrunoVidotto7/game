package com.scratch.game.engine;

import com.scratch.game.config.GameConfig;
import com.scratch.game.domain.Symbol;
import com.scratch.game.domain.SymbolType;
import com.scratch.game.domain.WinCombination;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RewardCalculator {

  public double compute(double bet,
      Map<String, List<String>> wins,
      String[][] matrix,
      GameConfig cfg) {

    double total = 0;

    for (Map.Entry<String, List<String>> e : wins.entrySet()) {
      String symbolName = e.getKey();
      Symbol sym = cfg.getSymbols().get(symbolName);

      double reward = bet * sym.getRewardMultiplier();
      // multiply by each UNIQUE win-group once
      Set<String> appliedGroups = new HashSet<>();
      for (String comboId : e.getValue()) {
        WinCombination wc = cfg.getWinCombinations().get(comboId);
        if (appliedGroups.add(wc.getGroup())) {
          reward *= wc.getRewardMultiplier();
        }
      }
      total += reward;
    }

    // Bonus symbol impact (only if SOME win exists)
    if (!wins.isEmpty()) {
      Optional<Symbol> bonusOpt = findBonus(matrix, cfg);
      if (bonusOpt.isPresent()) {
        Symbol b = bonusOpt.get();
        switch (b.getImpact()) {
          case MULTIPLY_REWARD:
            total *= b.getRewardMultiplier();
            break;
          case EXTRA_BONUS:
            total += b.getExtra();
            break;
          case MISS:
          default:
            // nothing
        }
      }
    }
    return Math.round(total);  // integral prizes
  }

  private Optional<Symbol> findBonus(String[][] m, GameConfig cfg) {
    for (String[] row : m) {
      for (String cell : row) {
        Symbol s = cfg.getSymbols().get(cell);
        if (s != null && s.getType() == SymbolType.BONUS) return Optional.of(s);
      }
    }
    return Optional.empty();
  }
}

