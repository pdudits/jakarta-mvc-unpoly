package io.zeromagic.unpolydemo.datatypes;

import java.util.EnumSet;

import static io.zeromagic.unpolydemo.datatypes.RuntimeSize.*;

public enum ScalabilityType {
  SINGLETON, ROLLING, HORIZONTAL;

  public static ScalabilityType fromString(String value) {
    for (var type : values()) {
      if (type.name().equalsIgnoreCase(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid scalability type: " + value);
  }

  public EnumSet<RuntimeSize> getSupportedSizes() {
    return switch (this) {
      case HORIZONTAL -> EnumSet.of(TWO_CORES, FOUR_CORES);
      default -> EnumSet.allOf(RuntimeSize.class);
    };
  }

  public boolean hasDatagrid() {
    return this == HORIZONTAL || this == ROLLING;
  }

  public boolean hasSwitchoverTime() {
    return this == ROLLING || this == HORIZONTAL;
  }

  public boolean hasReplicas() {
    return this == HORIZONTAL;
  }

  public RuntimeSize defaultRuntimeSize() {
    return switch (this) {
      case HORIZONTAL -> RuntimeSize.TWO_CORES;
      default -> QUARTER_CORE;
    };
  }
}
