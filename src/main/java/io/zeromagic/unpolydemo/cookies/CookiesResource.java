package io.zeromagic.unpolydemo.cookies;

import java.beans.ConstructorProperties;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.POST;
import jakarta.mvc.Controller;
import java.util.Objects;
import java.util.logging.Logger;

@Path("/cookies")
@RequestScoped
public class CookiesResource {
    private static final Logger LOGGER = Logger.getLogger(CookiesResource.class.getName());

    @GET
    @Controller
    public String cookiesDialog() {
        return "cookies/index.jte";
    }

    @POST
    @Controller
    public Response saveCookiePreference(
          @FormParam("pref") String cookiePreference,
          @CookieParam("cookie-pref") String currentCookiePreference,
          @HeaderParam("X-Up-Mode") String layerMode) {
        var cookie = new NewCookie.Builder("cookie-pref")
                .value(cookiePreference)
                .path("/")
                .maxAge(cookiePreference == null ? 0 : (int)TimeUnit.DAYS.toSeconds(180))
                .build();
        var responseBuilder = Response.ok("cookies/index.jte").cookie(cookie);
        LOGGER.info("Cookie preference: " + cookiePreference);
        if (layerMode != null) {
            // we are handling submission within drawer.
            // accept and close the drawer with new preference
            responseBuilder.header("X-Up-Accept-Layer", "\"+cookiePreference\"");
            // ignore html content, just return to previous layer
            responseBuilder.header("X-Up-Target", ":none");
            
            if (!Objects.equals(cookiePreference, currentCookiePreference)) {
                // emit an event if the preference has changed
                responseBuilder.header("X-Up-Events", """
                        [{"type": "cookie-pref:changed", "value": "%s"}]"""
                        .formatted(cookiePreference));
                LOGGER.info("Emitting cookie-pref:changed event");
            }
        }
        return responseBuilder.build();
    }
}