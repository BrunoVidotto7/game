package com.scratch.game.engine;

import static org.junit.Assert.assertEquals;

import com.scratch.game.config.GameConfig;
import com.scratch.game.config.GameConfigLoader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class RewardCalculatorTest {

  @Test
  public void rewardWithBonusMultiply() throws IOException {
    // handcrafted matrix: 3×"B" vertical + "10x"
    String[][] m = {
        {"A","B","C"},
        {"E","B","10x"},
        {"F","D","B"}
    };
    GameConfig cfg = GameConfigLoader.load(Paths.get("src/test/resources/config.json"));

    WinEvaluator eval = new WinEvaluator();
    RewardCalculator calc = new RewardCalculator();

    Map<String, List<String>> wins = eval.evaluate(m, cfg);
    double reward = calc.compute(100, wins, m, cfg);

    assertEquals(3000.0, reward, 0.0001);
  }
}