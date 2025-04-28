package com.scratch.game.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.scratch.game.model.Probabilities;
import com.scratch.game.model.ProbabilityCell;
import com.scratch.game.model.Symbol;
import com.scratch.game.model.WinCombination;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Configuration class for the Scratch Game.
 * This class holds the game settings, symbols, win combinations, and probabilities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfig {

  private int columns = 3;
  private int rows = 3;
  private Map<String, Symbol> symbols = new HashMap<>();
  @JsonProperty("win_combinations")
  private Map<String, WinCombination> winCombinations = new HashMap<>();
  private Probabilities probabilities = new Probabilities();

  /**
   * Retrieves the list of standard symbol probabilities.
   *
   * @return a list of {@link ProbabilityCell} representing standard symbol probabilities
   */
  public List<ProbabilityCell> getProbabilityCells() {
    return probabilities.getStandardSymbols();
  }

  /**
   * Retrieves the weights of bonus symbols.
   *
   * @return a map of bonus symbol names to their weights
   */
  public Map<String,Integer> getBonusWeights() {
    return Optional.ofNullable(probabilities.getBonusSymbols())
        .map(BonusProb::getSymbols)
        .orElseGet(Collections::emptyMap);
  }

  /**
   * Retrieves the win combinations for "same symbols".
   *
   * @return a list of {@link WinCombination} for "same symbols" conditions
   */
  public List<WinCombination> getSameSymbolCombos() {
    return winCombinations.values().stream()
        .filter(w -> "same_symbols".equals(w.getWhen()))
        .collect(Collectors.toList());
  }

  /**
   * Retrieves the win combinations for "linear symbols".
   *
   * @return a list of {@link WinCombination} for "linear symbols" conditions
   */
  public List<WinCombination> getLinearCombos() {
    return winCombinations.values().stream()
        .filter(w -> "linear_symbols".equals(w.getWhen()))
        .collect(Collectors.toList());
  }

  /**
   * Inner class representing bonus probabilities.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  public static class BonusProb {
    private Map<String,Integer> symbols = new HashMap<>();
  }
}
