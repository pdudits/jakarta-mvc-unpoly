package io.zeromagic.unpolydemo.app;

import java.time.Instant;

public record Revision(Number number, Instant configuredAt,
                       Instant deployedAt) {
  public record Number(int value) {
    public Number {
      if (value < 1) {
        throw new IllegalArgumentException(
            "Revision value must be greater than 0");
      }
    }
  }
}
