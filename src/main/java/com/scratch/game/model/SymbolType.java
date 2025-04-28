package com.scratch.game.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SymbolType {
  STANDARD("standard"),
  BONUS("bonus");

  private final String json;

  SymbolType(String json) {
    this.json = json;
  }

  @JsonValue
  public String value() {
    return json;
  }

  @JsonCreator
  public static SymbolType fromJson(String v) {
    for (SymbolType i : values()) {
      if (i.json.equalsIgnoreCase(v)) {
        return i;
      }
    }
    throw new IllegalArgumentException("Unknown symbol type: " + v);
  }
}

