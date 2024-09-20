package io.zeromagic.unpolydemo.datatypes;

import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public record ContextRoot(String value) {

  private static final String REGEX = "/[a-zA-Z0-9-]*";

  public ContextRoot {
    if (!isValidContextRoot(value)) {
      throw new IllegalArgumentException("Invalid context root: " + value);
    }
  }

  public static boolean isValidContextRoot(String contextRoot) {
    return contextRoot != null && contextRoot.matches(REGEX);
  }

  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @Pattern(regexp = REGEX,
      message = "Context root be url prefix starting with '/'")
  public @interface Constraint {
  }
}
