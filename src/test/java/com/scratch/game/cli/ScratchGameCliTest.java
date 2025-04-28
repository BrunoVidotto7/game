package com.scratch.game.cli;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratch.game.config.GameConfig;
import com.scratch.game.model.Symbol;
import com.scratch.game.model.SymbolType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Unit-tests for the pure helper method inside {@link ScratchGameCli}.
 * We access it via reflection because it’s private.
 */
public class ScratchGameCliTest {
  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream captured;

  @Before
  public void redirectStdout() {
    captured = new ByteArrayOutputStream();
    System.setOut(new PrintStream(captured));
  }

  @After
  public void restoreStdout() {
    System.setOut(originalOut);
  }

  @Test
  public void mainProducesValidJsonResult() throws Exception {

    String cfg = Paths.get("src/test/resources/config.json").toString();
    String[] args = { "--config", cfg, "--betting-amount", "100" };

    ScratchGameCli.main(args);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(captured.toString());

    assertTrue("matrix missing", root.has("matrix"));
    assertTrue("reward missing", root.has("reward"));

    JsonNode matrix = root.get("matrix");
    assertEquals("rows", 4, matrix.size());
    assertEquals("cols", 4, matrix.get(0).size());

    assertTrue("reward non-negative", root.get("reward").asDouble() >= 0);

    if (root.has("applied_winning_combinations")) {
      assertTrue(root.get("applied_winning_combinations").isObject());
    }
    if (root.has("applied_bonus_symbol") && !root.get("applied_bonus_symbol").isNull()) {
      assertTrue(root.get("applied_bonus_symbol").isTextual());
    }
  }

  private String callBonusSymbolName(String[][] matrix, GameConfig cfg)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    Method m = ScratchGameCli.class.getDeclaredMethod(
        "bonusSymbolName", String[][].class, GameConfig.class);
    m.setAccessible(true);
    return (String) m.invoke(null, matrix, cfg);
  }

  @Test
  public void bonusSymbolDetected() throws Exception {

    String[][] matrix = { {"A", "10x"} };

    GameConfig cfg = new GameConfig();
    Map<String, Symbol> map = new HashMap<>();
    Symbol bonus = new Symbol();
    bonus.setType(SymbolType.BONUS);
    map.put("10x", bonus);
    cfg.setSymbols(map);

    String result = callBonusSymbolName(matrix, cfg);

    assertEquals("10x", result);
  }

  @Test
  public void noBonusSymbolReturnsNull() throws Exception {

    String[][] matrix = { {"A", "B"} };

    GameConfig cfg = new GameConfig();
    cfg.setSymbols(new HashMap<>());

    String result = callBonusSymbolName(matrix, cfg);

    assertNull(result);
  }
}