package com.scratch.game.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.scratch.game.domain.Impact;
import com.scratch.game.domain.Symbol;
import com.scratch.game.domain.SymbolType;
import com.scratch.game.domain.WinCombination;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Immutable configuration holder that is deserialised once at startup.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GameConfig {

  /* ---------- top‑level JSON fields ---------- */
  private final int columns = 3;  // sensible defaults
  private final int rows = 3;
  private final Map<String, Symbol> symbols = new HashMap<>();
  private final Map<String, WinCombination> win_combinations = new HashMap<>();
  private final Probabilities probabilities = new Probabilities();

  public Map<String, WinCombination> getWinCombinations() {
    return win_combinations;
  }

  /** Standard‑symbol probability grid flattened into a list. */
  public List<ProbabilityCell> getProbabilityCells() {
    return probabilities.standard_symbols;
  }

  /** Map of bonus symbol → weight. */
  public Map<String,Integer> getBonusWeights() {
    return Optional.ofNullable(probabilities.bonus_symbols)
        .map(b -> b.symbols)
        .orElseGet(Collections::emptyMap);
  }

  /** all WinCombinations with WHEN = same_symbols */
  public List<WinCombination> getSameSymbolCombos() {
    return win_combinations.values().stream()
        .filter(w -> "same_symbols".equals(w.getWhen()))
        .collect(Collectors.toList());
  }

  /** all WinCombinations with WHEN = linear_symbols */
  public List<WinCombination> getLinearCombos() {
    return win_combinations.values().stream()
        .filter(w -> "linear_symbols".equals(w.getWhen()))
        .collect(Collectors.toList());
  }

  /* ---------- loader utility ---------- */
  public static GameConfig load(Path path) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    // allow trailing commas etc. if you like: mapper.enable(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature());
    return mapper.readValue(path.toFile(), GameConfig.class);
  }

  /* ---------- nested DTOs ---------- */

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Probabilities {
    public List<ProbabilityCell> standard_symbols = new ArrayList<>();
    public BonusProb bonus_symbols = new BonusProb();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  public static class ProbabilityCell {
    public int column;
    public int row;
    public Map<String,Integer> symbols = new HashMap<>();
    public Map<String,Integer> getWeights() { return symbols; }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class BonusProb {
    public Map<String,Integer> symbols = new HashMap<>();
  }

  /* ---------- static helpers to build Symbol / WinCombination ---------- */

  public static Symbol standardSymbol(String name, double mul) {
    return new Symbol(name, mul, SymbolType.STANDARD, null, 0);
  }

  public static Symbol bonusMultiply(String name, double mul) {
    return new Symbol(name, mul, SymbolType.BONUS, Impact.MULTIPLY_REWARD, 0);
  }

  public static Symbol bonusExtra(String name, double extra) {
    return new Symbol(name, 1, SymbolType.BONUS, Impact.EXTRA_BONUS, extra);
  }
}
