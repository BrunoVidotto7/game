package com.scratch.game.engine;

import com.scratch.game.config.GameConfig;
import com.scratch.game.model.Symbol;
import com.scratch.game.model.SymbolType;
import com.scratch.game.model.WinCombination;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Calculates the total reward for a game based on the bet amount, winning combinations, 
 * the generated matrix, and the game configuration.
 */
public class RewardCalculator {

  /**
   * Computes the total reward for the game.
   *
   * @param bet the amount of the bet
   * @param wins a map of winning symbol names to their associated winning combinations
   * @param matrix the generated game matrix
   * @param cfg the game configuration
   * @return the total reward, rounded to the nearest whole number
   */
  public double compute(double bet,
      Map<String, List<String>> wins,
      String[][] matrix,
      GameConfig cfg) {

    double total = 0;

    for (Map.Entry<String, List<String>> e : wins.entrySet()) {
      String symbolName = e.getKey();
      Symbol sym = cfg.getSymbols().get(symbolName);

      double reward = bet * sym.getRewardMultiplier();
      Set<String> appliedGroups = new HashSet<>();
      for (String comboId : e.getValue()) {
        WinCombination wc = cfg.getWinCombinations().get(comboId);
        if (appliedGroups.add(wc.getGroup())) {
          reward *= wc.getRewardMultiplier();
        }
      }
      total += reward;
    }

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
    return Math.round(total);
  }

  /**
   * Finds the first bonus symbol in the game matrix.
   *
   * @param m the game matrix
   * @param cfg the game configuration
   * @return an {@link Optional} containing the bonus symbol if found, or empty if no bonus symbol is present
   */
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

