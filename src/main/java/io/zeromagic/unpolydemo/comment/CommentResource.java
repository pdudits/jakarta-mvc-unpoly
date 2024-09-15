package io.zeromagic.unpolydemo.comment;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.time.Instant;
import java.util.List;

@RequestScoped
@Path("/comment")
public class CommentResource {
    @Inject
    CommentRepository commentRepository;

    @Inject
    CommentsModel model;

    @GET
    @Path("appevent/{eventId}/")
    @Controller
    public String listComments(@PathParam("eventId") int eventId) {
        model.setComments(commentRepository.findEventComments(eventId));
        return "comment/list.jte";
    }

    @GET
    @Path("appevent/{eventId}/new")
    @Controller
    public String newCommentForm(@PathParam("eventId") int eventId) {
        return "comment/new.jte";
    }

    @POST
    @Path("appevent/{eventId}/new")
    @Controller
    public String newComment(@PathParam("eventId") int eventId,
                             @FormParam("author") String author,
                             @FormParam("content") String content,
                             @HeaderParam("X-Up-Target") String target) {
        var comment = commentRepository.addEventComment(eventId, author, content);
        if (target != null && target.contains(".comment-list")) {
            // we're appending to comment list, just generate single comment;
            model.setComments(List.of(comment));
            return "comment/list.jte";
        }
        return "redirect:/comment/appevent/" + eventId;
    }
}
