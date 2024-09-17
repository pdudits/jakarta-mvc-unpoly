package io.zeromagic.unpolydemo.comment;

import io.zeromagic.unpolydemo.app.ApplicationEvent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestScoped
@Named("comments")
public class CommentsModel {
    @Inject
    HttpServletRequest request;

    private List<Comment> comments;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm");
    private ApplicationEvent event;


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String formatCommentTimestamp(Comment comment) {
        return dateFormat.format(comment.timestamp().atOffset(ZoneOffset.UTC));
    }

    public String newPostLink() {
        if (request.getRequestURI().endsWith("/")) {
            return request.getRequestURI() + "new";
        } else if (request.getRequestURI().endsWith("/new")) {
            return request.getRequestURI();
        } else {
            return request.getRequestURI() + "/new";
        }
    }

    public void setEvent(ApplicationEvent applicationEvent) {
        this.event = applicationEvent;
    }

    public ApplicationEvent getEvent() {
        return event;
    }
}
