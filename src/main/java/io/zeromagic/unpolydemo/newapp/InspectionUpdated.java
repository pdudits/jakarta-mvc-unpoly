package io.zeromagic.unpolydemo.newapp;

import io.zeromagic.unpolydemo.sse.BroadcastEvent;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;

import java.util.UUID;

public record InspectionUpdated(Inspection inspection) implements BroadcastEvent {
  @Override
  public UUID objectId() {
    return inspection.id().id();
  }

  @Override
  public boolean terminalEvent() {
    return inspection.status().isTerminal();
  }

  @Override
  public OutboundSseEvent toSseEvent(OutboundSseEvent.Builder eventBuilder) {
    return eventBuilder
            .data(inspection)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .build();
  }
}
