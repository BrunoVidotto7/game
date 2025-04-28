package com.scratch.game.engine;

import static org.junit.Assert.*;

import com.scratch.game.config.GameConfig;
import com.scratch.game.config.GameConfigLoader;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Test;

public class MatrixGeneratorTest {

  @Test
  public void matrixHasCorrectDimensions() throws IOException {
    GameConfig cfg = GameConfigLoader.load(Paths.get("src/test/resources/config.json"));
    MatrixGenerator g = new MatrixGenerator(cfg);

    String[][] m = g.generate();
    assertEquals(cfg.getRows(), m.length);
    assertEquals(cfg.getColumns(), m[0].length);
  }
}