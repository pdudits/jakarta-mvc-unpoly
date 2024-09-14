package io.zeromagic.unpolydemo.newapp;

import java.util.Objects;
import java.util.UUID;

public record Inspection(InspectionId id, String name, Status status, String contextRoot) {
  public enum Status {
    PENDING, IN_PROGRESS, UPLOADING, COMPLETE, FAILED;

    public boolean isTerminal() {
      return switch (this) {
        case COMPLETE, FAILED -> true;
        default -> false;
      };
    }
  }

  public record InspectionId(UUID id) {
    public InspectionId {
      Objects.requireNonNull(id);
    }

    public InspectionId() {
      this(UUID.randomUUID());
    }

    public InspectionId(String value) {
      this(UUID.fromString(value));
    }
  }

  public Inspection(String name, String contextRoot) {
    this(new InspectionId(), name, Status.PENDING, contextRoot);
  }

  public Inspection transition(Status status) {
    return new Inspection(id, name, status, contextRoot);
  }
}
