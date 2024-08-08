package io.zeromagic.unpolydemo.endpoint;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/")
@RequestScoped
public class RootResource {
    @Inject
    Models models;

    @GET
    @Controller
    public String index(@QueryParam("raw") @DefaultValue("~") Flag raw) {
        models.put("message", "Hello, JTE!");
        return raw.present() ? "raw:index.jte" : "index.jte";
    }

    public record Flag(String value) {
        boolean present() {
            return "".equals(value) || "true".equals(value);
        }
    }
}
