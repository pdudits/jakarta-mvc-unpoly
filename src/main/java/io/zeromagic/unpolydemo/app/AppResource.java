package io.zeromagic.unpolydemo.app;

import io.zeromagic.unpolydemo.endpoint.FormField;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.MvcContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/app")
@RequestScoped
@Controller
public class AppResource {
  @Inject
  AppModel app;

  @Inject
  NewAppModel newApp;

  @Inject
  MvcContext mvc;

  @GET
  public String index() {
    return "app/index.jte";
  }

  @GET
  @Path("new")
  public String newAppForm() {
    return "app/new.jte";
  }

  @POST
  @Path("new")
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
      return Response.seeOther(
              mvc.uri("AppResource#appDetail",
                      Map.of("name", name))).build();
    } else {
      return Response.status(Response.Status.BAD_REQUEST)
              .entity("app/new.jte").build();
    }
  }

  private boolean dnsLabel(String name) {
    return name.matches("[a-z]([a-z0-9-]{0,61}[a-z0-9])?");
  }

  private boolean contextPrefix(String contextRoot) {
    return contextRoot.matches("/[a-zA-Z0-9-]*");
  }


  @GET
  @Path("{name}")
  public String appDetail(@PathParam("name") String name) {
    app.setName(name);
    return "app/detail.jte";
  }
}

