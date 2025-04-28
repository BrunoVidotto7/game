package com.scratch.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProbabilityCell {
  private int column;
  private int row;
  private Map<String,Integer> symbols = new HashMap<>();
}
