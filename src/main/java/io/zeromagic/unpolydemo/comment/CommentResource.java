package io.zeromagic.unpolydemo.comment;

import io.zeromagic.unpolydemo.app.ApplicationRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@RequestScoped
@Path("/comment")
public class CommentResource {
    @Inject
    CommentRepository commentRepository;

    @Inject
    ApplicationRepository applicationRepository;

    @Inject
    CommentsModel model;

    @GET
    @Path("appevent/{eventId}/")
    @Controller
    public String listComments(@PathParam("eventId") int eventId,
                               @HeaderParam("X-Up-Version") String version) {
        model.setComments(commentRepository.findEventComments(eventId));
        if (version == null) {
            model.setEvent(applicationRepository.findEvent(eventId).orElseThrow(() -> new NotFoundException()));
        }
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
    public Response newComment(@PathParam("eventId") int eventId,
                               @FormParam("author") String author,
                               @FormParam("content") String content,
                               @HeaderParam("X-Up-Target") String target) {
        var comment = commentRepository.addEventComment(eventId, author, content);
        if (target != null && target.contains(".comment-list")) {
            // we're appending to comment list, just generate single comment;
            model.setComments(List.of(comment.comment()));
            var response = Response.ok("comment/list.jte");
            if (comment.count() ==1) {
                // if this is the first comment we need to retarget, otherwise
                // we'll append to list saying "No Comments".
                response.header("X-Up-Target", ".comment-list,.add-comment");
            }
            return response.build();
        }
        return Response.seeOther(URI.create("/comment/appevent/" + eventId + "/")).build();
    }
}
