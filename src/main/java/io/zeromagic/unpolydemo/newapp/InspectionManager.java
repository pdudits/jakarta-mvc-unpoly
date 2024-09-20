package io.zeromagic.unpolydemo.newapp;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static io.zeromagic.unpolydemo.newapp.Inspection.Status.*;

@ApplicationScoped
public class InspectionManager {
  @Resource
  ManagedScheduledExecutorService executorService;

  @Inject
  Event<InspectionUpdated> updatedEvent;

  private ConcurrentHashMap<Inspection.InspectionId, Inspection>
      inflight = new ConcurrentHashMap<>();

  public Inspection.InspectionId start(String name, String contextRoot) {
    Inspection inspection = new Inspection(name, contextRoot);
    inflight.put(inspection.id(), inspection);
    // simulate the process
    updateLater(1000, inspection, i -> i.transition(IN_PROGRESS));
    updateLater(3000, inspection, i -> i.transition(UPLOADING));
    updateLater(4000, inspection, i -> i.transition(COMPLETE));
    return inspection.id();
  }

  public Inspection get(Inspection.InspectionId id) {
    return inflight.get(id);
  }

  private void update(Inspection i,
      Function<Inspection, Inspection> transition) {
    var newStatus = inflight.compute(i.id(),
        (id, inspection) -> transition.apply(inspection));
    updatedEvent.fireAsync(new InspectionUpdated(newStatus));
    if (newStatus.status() == COMPLETE || newStatus.status() == FAILED) {
      // remove completed in
      executorService.schedule(() -> inflight.remove(i.id()), 5,
          TimeUnit.MINUTES);
    }
  }

  private void updateLater(long delay, Inspection i,
      Function<Inspection, Inspection> transition) {
    executorService.schedule(() -> update(i, transition),
        delay, TimeUnit.MILLISECONDS);
  }
}
