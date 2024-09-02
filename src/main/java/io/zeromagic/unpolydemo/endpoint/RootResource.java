package io.zeromagic.unpolydemo.endpoint;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

@Path("/")
@RequestScoped
public class RootResource {

    @Inject
    Models models;

    @Inject
    HotReloader reloader;

    @GET
    @Controller
    public String index(@QueryParam("raw") @DefaultValue("~") Flag raw) {
        models.put("message", "Hello, JTE!");
        return raw.present() ? "raw:index.jte" : "index.jte";
    }

    @GET
    @Produces("text/event-stream")
    public void hotreload(@Context SseEventSink sink) {
        reloader.register(sink);
    }

    public record Flag(String value) {
        boolean present() {
            return "".equals(value) || "true".equals(value);
        }
    }

    @GET
    @Path("ui-preference")
    @Controller
    public String uiPreference() {
        return "ui-preference.jte";
    }
}
