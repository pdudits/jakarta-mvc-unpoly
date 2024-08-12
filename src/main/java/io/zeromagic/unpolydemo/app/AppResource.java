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
  MvcContext mvc;

  @GET
  public String index() {
    return "app/index.jte";
  }

  @GET
  @Path("{name}")
  public String appDetail(@PathParam("name") String name) {
    app.setName(name);
    return "app/detail.jte";
  }
}

