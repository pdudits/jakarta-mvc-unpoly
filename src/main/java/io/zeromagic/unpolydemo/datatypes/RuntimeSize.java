package io.zeromagic.unpolydemo.datatypes;

public enum RuntimeSize {
  QUARTER_CORE("0.25 Core"),
  HALF_CORE("0.5 Core"),
  ONE_CORE("1 Core"),
  TWO_CORES("2 Cores"),
  FOUR_CORES("4 Cores");

  private final String label;

  RuntimeSize(String label) {
    this.label = label;
  }

  public String label() {
    return label;
  }

  public static RuntimeSize fromString(String value) {
    for (var type : values()) {
      if (type.name().equalsIgnoreCase(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid runtime size: " + value);
  }

}
