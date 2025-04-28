package com.scratch.game.engine;

import com.scratch.game.config.GameConfig;
import com.scratch.game.config.GameConfig.ProbabilityCell;
import com.scratch.game.util.WeightedRandom;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

/**
 * Produces a symbol matrix based on the per‑cell weighted probabilities in the
 * config and then overwrites ONE random cell with a bonus symbol.
 */
public class MatrixGenerator {

  private final GameConfig cfg;
  private final Random rnd = new SecureRandom();

  public MatrixGenerator(GameConfig cfg) {
    this.cfg = cfg;
  }

  /**
   * @return newly generated matrix (rows × columns)
   */
  public String[][] generate() {
    int rows = cfg.getRows();
    int cols = cfg.getColumns();
    String[][] matrix = new String[rows][cols];

    /* --------------------------------------------------------- */
    /* 1) Fill every cell with a STANDARD symbol using its       */
    /*    individual probability distribution.                   */
    /* --------------------------------------------------------- */
    for (ProbabilityCell cell : cfg.getProbabilityCells()) {
      int r = cell.getRow();
      int c = cell.getColumn();
      matrix[r][c] = pick(cell.getWeights());
    }

    /* Safety: if the config had gaps we still fill them using   */
    /* the baseline distribution from (0,0).                     */
    Map<String,Integer> baseline = cfg.getProbabilityCells().get(0).getWeights();
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (matrix[r][c] == null) {
          matrix[r][c] = pick(baseline);
        }
      }
    }

    /* --------------------------------------------------------- */
    /* 2) Overwrite a random cell with a BONUS symbol.           */
    /* --------------------------------------------------------- */
    int br = rnd.nextInt(rows);
    int bc = rnd.nextInt(cols);
    matrix[br][bc] = pick(cfg.getBonusWeights());

    return matrix;
  }

  /* Helper: pick one key according to weight map */
  private String pick(Map<String,Integer> weights) {
    WeightedRandom<String> wr = new WeightedRandom<>();
    weights.forEach((sym, w) -> wr.add(w, sym));
    return wr.next(rnd);
  }
}


