package io.zeromagic.unpolydemo.comment;

import io.zeromagic.unpolydemo.app.ApplicationEvent;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CommentRepository {
    private final Map<Comment.CommentSubject, SortedSet<Comment>> comments = new ConcurrentHashMap<>();

    private void addComment(Comment.CommentSubject subject, Comment comment) {
        comments.computeIfAbsent(subject, k -> Collections.synchronizedSortedSet(new TreeSet<>()))
                .add(comment);
    }

    public List<Comment> findComments(ApplicationEvent event) {
        return List.copyOf(comments.getOrDefault(new Comment.EventComment(event.id()), new TreeSet<>()));
    }

    public List<Comment> findEventComments(int eventId) {
        return List.copyOf(comments.getOrDefault(new Comment.EventComment(eventId), new TreeSet<>()));
    }

    public Comment addEventComment(int eventId, String author, String content) {
        var comment = new Comment(new Comment.EventComment(eventId), author, Instant.now(), content);
        addComment(new Comment.EventComment(eventId), comment);
        return comment;
    }
}
