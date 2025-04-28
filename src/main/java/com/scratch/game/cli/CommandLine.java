package com.scratch.game.cli;

import java.util.HashMap;
import java.util.Map;
public final class CommandLine {

  private final Map<String,String> kv = new HashMap<>();

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
   * Returns the value for the flag (e.g. <code>--config</code>), or null if absent.
   */
  public String get(String key) {
    return kv.get(key);
  }

  /** Convenience: required flag with human message */
  public String require(String key) {
    String v = get(key);
    if (v == null) {
      throw new IllegalArgumentException("Missing required argument: " + key);
    }
    return v;
  }
}

