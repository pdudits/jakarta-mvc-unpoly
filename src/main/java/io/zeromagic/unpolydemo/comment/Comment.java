package io.zeromagic.unpolydemo.comment;

import java.time.Instant;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public record Comment(int id, CommentSubject subject, String author, Instant timestamp, String content) implements Comparable<Comment> {
    private static final Comparator<Comment> COMPARATOR = Comparator.comparing(Comment::timestamp).thenComparing(Comment::id);
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    public Comment(CommentSubject subject, String author, Instant timestmap, String content) {
        this(ID_GENERATOR.incrementAndGet(), subject, author, timestmap, content);
    }

    @Override
    public int compareTo(Comment o) {
        return COMPARATOR.compare(this, o);
    }

    public sealed interface CommentSubject {

    }

    public record EventComment(int eventId) implements CommentSubject {

    }
}
