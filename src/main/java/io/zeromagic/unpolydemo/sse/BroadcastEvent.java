package io.zeromagic.unpolydemo.sse;

import jakarta.ws.rs.sse.OutboundSseEvent;

import java.util.UUID;

public interface BroadcastEvent {
  UUID objectId();

  boolean terminalEvent();

  OutboundSseEvent toSseEvent(OutboundSseEvent.Builder eventBuilder);
}
