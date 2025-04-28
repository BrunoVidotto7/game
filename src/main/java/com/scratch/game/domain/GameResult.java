package com.scratch.game.domain;

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
  public final Map<String, List<String>> applied_winning_combinations; // symbol -> list(ids)
  public final String applied_bonus_symbol; // may be null

  public GameResult(String[][] matrix,
      double reward,
      Map<String, List<String>> appliedWinning,
      String bonusSymbol) {
    this.matrix = matrix;
    this.reward = reward;
    this.applied_winning_combinations = appliedWinning;
    this.applied_bonus_symbol = bonusSymbol;
  }

  /** Pretty JSON string for CLI output */
  public String toPrettyJson() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialise GameResult", e);
    }
  }
}
