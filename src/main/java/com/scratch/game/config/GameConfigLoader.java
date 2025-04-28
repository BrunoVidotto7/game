package com.scratch.game.config;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Loads the JSON configuration file, applies post‑processing (IDs, defaults),
 * and returns an immutable {@link GameConfig} instance ready for the engine.
 */
public final class GameConfigLoader {

  private GameConfigLoader() {}

  /**
   * @param path path to config.json
   * @return fully‑initialised game configuration
   */
  public static GameConfig load(Path path) throws IOException {
    ObjectMapper mapper = JsonMapper.builder()
        .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
        .build();

    GameConfig cfg = mapper.readValue(path.toFile(), GameConfig.class);

    initialiseWinCombinations(cfg);
    fillMissingProbabilityCells(cfg);

    return cfg;
  }

  /* -------------------------------------------------------------- */
  /* 1) give each WinCombination its ID + parse covered_areas        */
  /* -------------------------------------------------------------- */
  private static void initialiseWinCombinations(GameConfig cfg) {
    cfg.getWinCombinations().forEach((id, wc) -> wc.initialise(id));
  }

  /* -------------------------------------------------------------- */
  /* 2) if the JSON omits some probability cells, clone (0,0)        */
  /* -------------------------------------------------------------- */
  private static void fillMissingProbabilityCells(GameConfig cfg) {
    List<GameConfig.ProbabilityCell> cells = cfg.getProbabilityCells();
    if (cells.isEmpty()) return; // nothing we can do

    // index existing cells by row+col for O(1) look‑ups
    Map<String, GameConfig.ProbabilityCell> index = new HashMap<>();
    for (GameConfig.ProbabilityCell pc : cells) {
      index.put(key(pc.getRow(), pc.getColumn()), pc);
    }

    // baseline distribution → copy from first declared cell
    Map<String,Integer> baseline = new HashMap<>(cells.get(0).getWeights());

    for (int r = 0; r < cfg.getRows(); r++) {
      for (int c = 0; c < cfg.getColumns(); c++) {
        if (!index.containsKey(key(r, c))) {
          GameConfig.ProbabilityCell clone = new GameConfig.ProbabilityCell();
          clone.row = r;   // package‑private access ok – nested class
          clone.column = c;
          clone.symbols = new HashMap<>(baseline);
          cells.add(clone);
        }
      }
    }
  }

  private static String key(int row, int col) {
    return row + ":" + col;
  }
}

