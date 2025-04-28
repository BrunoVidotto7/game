package com.scratch.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Symbol {

  private final String name;
  private final double rewardMultiplier;   // for standard OR bonus multiply_reward
  private final SymbolType type;          // STANDARD | BONUS
  private final Impact impact;            // multiply_reward | extra_bonus | miss | null
  private final double extra;

}
