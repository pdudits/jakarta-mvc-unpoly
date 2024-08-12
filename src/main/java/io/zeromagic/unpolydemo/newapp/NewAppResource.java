package io.zeromagic.unpolydemo.newapp;

import io.zeromagic.unpolydemo.endpoint.FormField;
import io.zeromagic.unpolydemo.sse.Broadcaster;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.MvcContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.Map;

@RequestScoped
@Path("new-app")
public class NewAppResource {
  @Inject
  InspectionManager inspectionProcess;

  @Inject
  Models models;

  @Inject
  Broadcaster broadcaster;

  @Inject
  NewAppModel newApp;

  @Inject
  MvcContext mvc;

  @GET
  @Controller
  public String newAppForm() {
    return "newapp/index.jte";
  }

  @Controller
  @POST
  public Response newApp(@FormParam("name") String name,
                         @FormParam("contextRoot") String contextRoot) {
    if (name == null || name.isBlank()) {
      newApp.setName(
              FormField.invalid(name, "Name is required"));
    } else if (!dnsLabel(name)) {
      newApp.setName(FormField.invalid(name,
              "Name must contain lowercase alphanumeric characters or '-'"));
    } else {
      newApp.setName(FormField.valid(name));
    }

    if (contextRoot == null || contextRoot.isBlank()) {
      newApp.setContextRoot(
              FormField.invalid(contextRoot, "Context root is required"));
    } else if (!contextPrefix(contextRoot)) {
      newApp.setContextRoot(
              FormField.invalid(contextRoot,
                      "Context root be url prefix starting with '/'"));
    } else {
      newApp.setContextRoot(FormField.valid(contextRoot));
    }
    if (newApp.isInputValid()) {
      var id = inspectionProcess.start(name);
      return Response.seeOther(
              mvc.uri("NewAppResource#getStatus",
                      Map.of("id", id.id().toString()))).build();
    } else {
      return Response.status(Response.Status.BAD_REQUEST)
              .entity("newapp/index.jte").build();
    }
  }

  private boolean dnsLabel(String name) {
    return name.matches("[a-z]([a-z0-9-]{0,61}[a-z0-9])?");
  }

  private boolean contextPrefix(String contextRoot) {
    return contextRoot.matches("/[a-zA-Z0-9-]*");
  }

  @Path("_test_view")
  @Controller
  @GET
  public String testView() {
    models.put("inspection", new Inspection("test"));
    return "newapp/status.jte";
  }

  @Path("{id}")
  @GET
  @Controller
  @Produces("text/html")
  public String getStatus(@PathParam("id") Inspection.InspectionId id) {
    var status = inspectionProcess.get(id);
    if (status == null) {
      throw new NotFoundException("Inspection not found");
    }
    models.put("inspection", status);
    if (status.status() == Inspection.Status.COMPLETE) {
      return "redirect:app/"+status.name();
    }
    return "newapp/status.jte";
  }

  @Path("{id}")
  @GET
  @Produces("text/event-stream")
  public void streamStatus(@PathParam("id") Inspection.InspectionId id,
                           @Context SseEventSink sink, @Context Sse sse) {
    var status = inspectionProcess.get(id);
    if (status == null) {
      sink.send(sse.newEvent("{\"finalState\": \"NOT_FOUND\"}"));
      sink.close();
    } else if (status.status().isTerminal()) {
      sink.send(sse.newEvent(
              "{\"finalState\": \"" + status.status().name() + "\"}"));
      sink.close();
    } else {
      broadcaster.register(sink, id.id(), InspectionUpdated.class);
    }
  }
}
