package com.scratch.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scratch.game.config.GameConfig.BonusProb;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Probabilities {
  @JsonProperty("standard_symbols")
  private List<ProbabilityCell> standardSymbols = new ArrayList<>();
  @JsonProperty("bonus_symbols")
  private BonusProb bonusSymbols = new BonusProb();
}
