package io.zeromagic.unpolydemo.sse;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class Broadcaster {
  private static final Logger LOGGER = Logger.getLogger(
      Broadcaster.class.getName());

  @Context
  Sse sse;

  private OutboundSseEvent.Builder eventBuilder;
  private ConcurrentHashMap<SinkKey, SseBroadcaster>
      broadcasts = new ConcurrentHashMap<>();

  @PostConstruct
  void init() {
    this.eventBuilder = sse.newEventBuilder();
  }

  @PreDestroy
  void stop() {
    broadcasts.values().forEach(SseBroadcaster::close);
  }

  public void register(SseEventSink sink, UUID objectId,
      Class<? extends BroadcastEvent> eventClass) {
    var key = new SinkKey(eventClass, objectId);
    var broadcaster = broadcasts
        .computeIfAbsent(key, k -> sse.newBroadcaster());
    broadcaster.register(sink);
  }

  void onBroadcast(@Observes BroadcastEvent event) {
    for (Class<?> clazz = event.getClass();
         clazz != null && BroadcastEvent.class.isAssignableFrom(clazz);
         clazz = clazz.getSuperclass()) {
      var key = new SinkKey(event.getClass(), event.objectId());
      broadcast(event, key);
      if (event.objectId() != null) {
        broadcast(event, new SinkKey(event.getClass(), null));
      }
    }
  }

  void onAsyncBroadcast(@ObservesAsync BroadcastEvent event) {
    onBroadcast(event);
  }

  private void broadcast(BroadcastEvent event, SinkKey key) {
    var broadcaster = broadcasts.get(key);
    if (broadcaster != null) {
      broadcaster.broadcast(event.toSseEvent(eventBuilder))
          .whenComplete((r, t) -> {
            if (t != null) {
              LOGGER.log(Level.WARNING,
                  "Error broadcasting event", t);
            }
          });
      if (event.terminalEvent()) {
        broadcaster.close();
        broadcasts.remove(key);
      }
    }
  }

  record SinkKey(Class<?> eventClass, UUID objectId) {
  }
}
