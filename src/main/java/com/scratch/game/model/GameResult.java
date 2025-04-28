package com.scratch.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Immutable result returned by the game engine and printed by CLI.
 */
public class GameResult {
  public final String[][] matrix;
  public final double reward;
  @JsonProperty("applied_winning_combinations")
  public final Map<String, List<String>> appliedWinningCombinations;
  @JsonProperty("applied_bonus_symbol")
  public final String appliedBonusSymbol;

  public GameResult(String[][] matrix,
      double reward,
      Map<String, List<String>> appliedWinning,
      String bonusSymbol) {
    this.matrix = matrix;
    this.reward = reward;
    this.appliedWinningCombinations = appliedWinning;
    this.appliedBonusSymbol = bonusSymbol;
  }

  public String toPrettyJson() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialise GameResult", e);
    }
  }
}
