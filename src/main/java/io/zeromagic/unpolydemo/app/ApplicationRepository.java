package io.zeromagic.unpolydemo.app;

import io.zeromagic.unpolydemo.newapp.Inspection;
import io.zeromagic.unpolydemo.newapp.InspectionUpdated;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ApplicationRepository {
  private final List<Application> applications = Collections.synchronizedList(
      new ArrayList<>());
  private final Map<Application.Key, SortedSet<ApplicationEvent>> timelines =
          Collections.synchronizedMap(
      new HashMap<>());

  @PostConstruct
  void generarateData() {
    applications.addAll(List.of(makeEntry("auth-service",
            URI.create("http://booking-system.payara.app/auth")),
        makeEntry("booking-service",
            URI.create("http://booking-system.payara.app/booking")),
        makeEntry("payment-service",
            URI.create("http://booking-system.payara.app/payment")),
        makeEntry("notification-service",
            URI.create("http://booking-system.payara.app/notification")),
        makeEntry("user-service",
            URI.create("http://booking-system.payara.app/user"))));
    applications.forEach(this::createTimeline);
  }

  void addCompletedInspections(@ObservesAsync InspectionUpdated updated) {
    if (updated.inspection().status() == Inspection.Status.COMPLETE) {
      Application app = makeEntry(updated.inspection().name(),
          URI.create("http://booking-system.payara.app/")
              .resolve(updated.inspection().contextRoot()));
      createTimeline(app);
      applications.add(app);
    }

  }

  private void createTimeline(Application app) {
    var timeline = timelines.computeIfAbsent(app.key(),
        k -> Collections.synchronizedSortedSet(new TreeSet<>()));
    app.revisions().forEach(revision -> {
      timeline.add(new ApplicationEvent(app.key(), revision.number(),
          revision.configuredAt(), "configured",
          "Configured revision " + revision.number().value()));
      timeline.add(new ApplicationEvent(app.key(), revision.number(),
          revision.deployedAt(), "deployed",
          "Deployed revision " + revision.number().value()));
    });
  }

  public List<Application> findAllApplications() {
    return List.copyOf(applications);
  }

  public SortedSet<ApplicationEvent> findTimeline(Application.Key key) {
    return timelines.getOrDefault(key, Collections.emptySortedSet());
  }

  public Optional<ApplicationEvent> findEvent(int id) {
    return timelines.values().stream()
        .flatMap(SortedSet::stream)
        .filter(e -> e.id() == id)
        .findFirst();
  }


  private Application makeEntry(String name, URI uri) {
    var numberOfRevisions = ThreadLocalRandom.current().nextInt(1, 5);
    var revisions = new ArrayList<Revision>(numberOfRevisions);
    var timestamp = past(Instant.now());
    for (int i = numberOfRevisions; i > 0; i--) {
      revisions.addFirst(
          new Revision(new Revision.Number(i), past(timestamp), timestamp));
      timestamp = past(revisions.getLast().configuredAt());
    }
    return new Application(name, uri, revisions);
  }

  private Instant past(Instant now) {
    return now.minusSeconds(
        ThreadLocalRandom.current().nextLong(10, TimeUnit.DAYS.toSeconds(7)));
  }
}
