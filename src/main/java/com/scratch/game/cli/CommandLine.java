package com.scratch.game.cli;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for parsing command-line arguments.
 * Arguments are expected in the format: --key value or --key (for boolean flags).
 */
public final class CommandLine {

  private final Map<String, String> kv = new HashMap<>();

  /**
   * Constructs a CommandLine instance by parsing the provided arguments.
   *
   * @param args the command-line arguments to parse
   * @throws IllegalArgumentException if an unexpected token is encountered
   */
  public CommandLine(String[] args) {
    for (int i = 0; i < args.length; i++) {
      String token = args[i];
      if (token.startsWith("--")) {
        String value = (i + 1 < args.length && !args[i + 1].startsWith("--"))
            ? args[++i]
            : "true";
        kv.put(token, value);
      } else {
        throw new IllegalArgumentException("Unexpected token: " + token);
      }
    }
  }

  /**
   * Retrieves the value associated with the specified key.
   *
   * @param key the key to look up
   * @return the value associated with the key, or {@code null} if the key is not present
   */
  public String get(String key) {
    return kv.get(key);
  }

  /**
   * Retrieves the value associated with the specified key, throwing an exception if the key is not present.
   *
   * @param key the key to look up
   * @return the value associated with the key
   * @throws IllegalArgumentException if the key is not present
   */
  public String require(String key) {
    String v = get(key);
    if (v == null) {
      throw new IllegalArgumentException("Missing required argument: " + key);
    }
    return v;
  }
}

