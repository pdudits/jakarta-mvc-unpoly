package io.zeromagic.unpolydemo.app;

import java.time.Instant;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public record ApplicationEvent(
        int id,
        Application.Key key,
        Revision.Number revision,
        Instant timestamp,
        String kind,
        String message) implements Comparable<ApplicationEvent> {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    public ApplicationEvent(Application.Key key, Revision.Number revision, Instant timestamp, String kind, String message) {
        this(ID_GENERATOR.incrementAndGet(), key, revision, timestamp, kind, message);
    }

    private static final Comparator<ApplicationEvent> COMPARATOR = Comparator.comparing(ApplicationEvent::timestamp)
            .thenComparing(e -> e.key().name());

    @Override
    public int compareTo(ApplicationEvent o) {
        return COMPARATOR.compare(this, o);
    }
}
