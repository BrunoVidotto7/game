package com.scratch.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Symbol {
  @JsonProperty("reward_multiplier")
  private double rewardMultiplier;
  private SymbolType type;
  private Impact impact;
  private double extra;

}
