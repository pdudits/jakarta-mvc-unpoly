package io.zeromagic.unpolydemo.datatypes;

import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public record DnsLabel(String value) {

  private static final String REGEX = "[a-z]([a-z0-9-]{0,61}[a-z0-9])?";

  public DnsLabel {
    if (!isValidDnsLabel(value)) {
      throw new IllegalArgumentException("Invalid DNS label: " + value);
    }
  }

  public static boolean isValidDnsLabel(String name) {
    return name.matches(REGEX);
  }

  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @Pattern(regexp = REGEX,
      message = "Context root be url prefix starting with '/'")
  public @interface Constraint {
  }
}
