package io.zeromagic.unpolydemo.comment;

import io.zeromagic.unpolydemo.app.ApplicationEvent;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CommentRepository {
  private final Map<Comment.CommentSubject, SortedSet<Comment>> comments =
          new ConcurrentHashMap<>();

  private int addComment(Comment.CommentSubject subject, Comment comment) {
    var commentSet = comments.computeIfAbsent(subject,
        k -> Collections.synchronizedSortedSet(new TreeSet<>()));
    commentSet.add(comment);
    return commentSet.size();
  }

  public List<Comment> findComments(ApplicationEvent event) {
    return List.copyOf(
        comments.getOrDefault(new Comment.EventComment(event.id()),
            new TreeSet<>()));
  }

  public List<Comment> findEventComments(int eventId) {
    return List.copyOf(comments.getOrDefault(new Comment.EventComment(eventId),
        new TreeSet<>()));
  }

  public AddResult addEventComment(int eventId, String author, String content) {
    var comment = new Comment(new Comment.EventComment(eventId), author,
        Instant.now(), content);
    var count = addComment(new Comment.EventComment(eventId), comment);
    return new AddResult(count, comment);
  }

  public record AddResult(int count, Comment comment) {
  }
}
