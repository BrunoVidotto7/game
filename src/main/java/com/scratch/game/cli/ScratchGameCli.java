package com.scratch.game.cli;

import com.scratch.game.config.GameConfigLoader;
import com.scratch.game.config.GameConfig;
import com.scratch.game.domain.GameResult;
import com.scratch.game.domain.Symbol;
import com.scratch.game.domain.SymbolType;
import com.scratch.game.engine.MatrixGenerator;
import com.scratch.game.engine.RewardCalculator;
import com.scratch.game.engine.WinEvaluator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public final class ScratchGameCli {

  public static void main(String[] args) throws Exception {
    CommandLine cli = new CommandLine(args);
    Path cfgPath = Paths.get(cli.require("--config"));
    double betAmount = Double.parseDouble(cli.require("--betting-amount"));

    GameConfig cfg = GameConfigLoader.load(cfgPath);

    MatrixGenerator generator = new MatrixGenerator(cfg);
    WinEvaluator evaluator = new WinEvaluator();
    RewardCalculator calculator = new RewardCalculator();

    String[][] matrix = generator.generate();
    Map<String, List<String>> wins = evaluator.evaluate(matrix, cfg);
    double reward = calculator.compute(betAmount, wins, matrix, cfg);

    GameResult result = new GameResult(
        matrix,
        reward,
        wins,
        bonusSymbolName(matrix, cfg)
    );

    System.out.println(result.toPrettyJson());
  }

  /** Returns the first bonus symbol found in the matrix, or null if none. */
  private static String bonusSymbolName(String[][] matrix, GameConfig cfg) {
    for (String[] row : matrix) {
      for (String cell : row) {
        Symbol sym = cfg.getSymbols().get(cell);
        if (sym != null && sym.getType() == SymbolType.BONUS) {
          return sym.getName();
        }
      }
    }
    return null;
  }
}

