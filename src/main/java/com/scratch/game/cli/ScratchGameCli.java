package com.scratch.game.cli;

import com.scratch.game.config.GameConfigLoader;
import com.scratch.game.config.GameConfig;
import com.scratch.game.model.GameResult;
import com.scratch.game.model.Symbol;
import com.scratch.game.model.SymbolType;
import com.scratch.game.engine.MatrixGenerator;
import com.scratch.game.engine.RewardCalculator;
import com.scratch.game.engine.WinEvaluator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * The command-line interface for the Scratch Game.
 * This class initializes the game configuration, processes the game logic, and outputs the result.
 */
public final class ScratchGameCli {

  /**
   * The entry point of the Scratch Game CLI application.
   *
   * @param args the command-line arguments
   * @throws Exception if an error occurs during game execution
   */
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

  /**
   * Determines the name of the bonus symbol in the generated matrix.
   *
   * @param matrix the generated game matrix
   * @param cfg the game configuration
   * @return the name of the bonus symbol, or {@code null} if no bonus symbol is found
   */
  private static String bonusSymbolName(String[][] matrix, GameConfig cfg) {
    for (String[] row : matrix) {
      for (String cell : row) {
        Symbol sym = cfg.getSymbols().get(cell);
        if (sym != null && sym.getType() == SymbolType.BONUS) {
          return cell;
        }
      }
    }
    return null;
  }
}

