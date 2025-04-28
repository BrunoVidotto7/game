package com.scratch.game.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One entry from the “win_combinations” section of the config.
 * After loading, call {@link #initialise(String)} to set its ID
 * and convert the raw \"row:col\" strings into typed {@link Cell}s.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinCombination {

  /* values that map directly from JSON */
  @JsonProperty("reward_multiplier")
  private double rewardMultiplier = 1.0;

  private String when;         // "same_symbols" | "linear_symbols"
  private int count;           // only for "same_symbols"
  private String group;

  @JsonProperty("covered_areas")
  private List<List<String>> coveredAreasRaw = Collections.emptyList();

  /* derived fields (not in JSON) */
  private String id;
  private final List<List<Cell>> coveredAreas = new ArrayList<>();

  /* ------------------------------------------------------------------ */
  /*  life-cycle helpers                                                */
  /* ------------------------------------------------------------------ */

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
          String[] parts = token.split(":");
          parsed.add(new Cell(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        coveredAreas.add(parsed);
      }
    }
    // free raw memory
    coveredAreasRaw = List.of();
  }

  /* ------------------------------------------------------------------ */
  /*  getters used by the engine                                        */
  /* ------------------------------------------------------------------ */

  public String getId()               { return id;               }
  public double getRewardMultiplier() { return rewardMultiplier; }
  public String getWhen()             { return when;             }
  public int getCount()               { return count;            }
  public String getGroup()            { return group;            }
  public List<List<Cell>> getCoveredAreas() { return coveredAreas; }

  /* ------------------------------------------------------------------ */
  /*  tiny value type for coordinates                                   */
  /* ------------------------------------------------------------------ */
  public static final class Cell {
    private final int row;
    private final int col;

    public Cell(int row, int col) {
      this.row = row;
      this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
  }
}

