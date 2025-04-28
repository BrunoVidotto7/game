package com.scratch.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

/**
 * One entry from the “win_combinations” section of the config.
 * After loading, call {@link #initialise(String)} to set its ID
 * and convert the raw \"row:col\" strings into typed {@link Cell}s.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class WinCombination {

  public static final String SPLITTER = ":";
  @JsonProperty("reward_multiplier")
  private double rewardMultiplier = 1.0;

  private String when;
  private int count;
  private String group;

  @JsonProperty("covered_areas")
  private List<List<String>> coveredAreasRaw = Collections.emptyList();

  private String id;
  private final List<List<Cell>> coveredAreas = new ArrayList<>();

  /**
   * Called once by the config loader – supplies the map key (“same_symbol_3_times”
   * etc.) and transforms the raw area strings into row/col pairs.
   */
  public void initialise(String idFromMap) {
    this.id = idFromMap;
    if (!coveredAreasRaw.isEmpty()) {
      for (List<String> area : coveredAreasRaw) {
        List<Cell> parsed = new ArrayList<>();
        for (String token : area) {
          String[] parts = token.split(SPLITTER);
          parsed.add(new Cell(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        coveredAreas.add(parsed);
      }
    }
    coveredAreasRaw = List.of();
  }
}

