package com.scratch.game.config;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import com.scratch.game.model.ProbabilityCell;
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

  private static void initialiseWinCombinations(GameConfig cfg) {
    cfg.getWinCombinations().forEach((id, wc) -> wc.initialise(id));
  }


  private static void fillMissingProbabilityCells(GameConfig cfg) {
    List<ProbabilityCell> cells = cfg.getProbabilityCells();
    if (cells.isEmpty()) return;

    Map<String, ProbabilityCell> index = new HashMap<>();
    for (ProbabilityCell pc : cells) {
      index.put(key(pc.getRow(), pc.getColumn()), pc);
    }

    Map<String,Integer> baseline = new HashMap<>(cells.get(0).getSymbols());

    for (int r = 0; r < cfg.getRows(); r++) {
      for (int c = 0; c < cfg.getColumns(); c++) {
        if (!index.containsKey(key(r, c))) {
          ProbabilityCell clone = new ProbabilityCell();
          clone.setRow(r);
          clone.setColumn(c);
          clone.setSymbols(new HashMap<>(baseline));
          cells.add(clone);
        }
      }
    }
  }

  private static String key(int row, int col) {
    return row + ":" + col;
  }
}

