package com.scratch.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Behaviour of a bonus symbol once a win exists. */
public enum Impact {
  MULTIPLY_REWARD("multiply_reward"),
  EXTRA_BONUS("extra_bonus"),
  MISS("miss");

  private final String json;

  Impact(String json) {
    this.json = json;
  }

  /** serialise to the same string the JSON config uses */
  @JsonValue
  public String value() {
    return json;
  }

  /** deserialise from JSON (case-insensitive) */
  @JsonCreator
  public static Impact fromJson(String v) {
    for (Impact i : values()) {
      if (i.json.equalsIgnoreCase(v)) {
        return i;
      }
    }
    throw new IllegalArgumentException("Unknown impact: " + v);
  }
}
