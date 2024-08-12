package io.zeromagic.unpolydemo.newapp;

import java.util.Objects;
import java.util.UUID;

public record Inspection(InspectionId id, String name, Status status) {
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

  public Inspection(String name) {
    this(new InspectionId(), name, Status.PENDING);
  }

  public Inspection transition(Status status) {
    return new Inspection(id, name, status);
  }
}
