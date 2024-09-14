package io.zeromagic.unpolydemo.app;

import java.time.Instant;
import java.util.Comparator;

public record ApplicationEvent(
        Application.Key key,
        Revision.Number revision,
        Instant timestamp,
        String kind,
        String message) implements Comparable<ApplicationEvent> {

    private static final Comparator<ApplicationEvent> COMPARATOR = Comparator.comparing(ApplicationEvent::timestamp)
            .thenComparing(e -> e.key().name());

    @Override
    public int compareTo(ApplicationEvent o) {
        return COMPARATOR.compare(this, o);
    }
}
