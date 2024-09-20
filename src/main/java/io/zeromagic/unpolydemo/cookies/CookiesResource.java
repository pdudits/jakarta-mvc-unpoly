package io.zeromagic.unpolydemo.cookies;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Path("/cookies")
@RequestScoped
public class CookiesResource {
  private static final Logger LOGGER = Logger.getLogger(
      CookiesResource.class.getName());

  @Inject
  CookieModel model;

  @GET
  @Controller
  public Response cookiesDialog(
      @HeaderParam("X-Up-Mode") String layerMode,
      @CookieParam("cookie-pref") String currentCookiePreference) {
    model.setOverlay(layerMode != null && !"root".equals(layerMode));
    model.setCookiePreference(currentCookiePreference);
    // tell Unpoly that presence of specific layer mode changes output
    // and therefore it needs to cache it accordingly
    return Response.ok("cookies/index.jte").header("Vary", "X-Up-Mode").build();
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
        .maxAge(
            cookiePreference == null ? 0 : (int) TimeUnit.DAYS.toSeconds(180))
        .build();

    var responseBuilder = Response.ok("cookies/index.jte").cookie(cookie);
    LOGGER.info("Cookie preference: " + cookiePreference);
    var changedPreference = !Objects.equals(cookiePreference,
        currentCookiePreference);
    model.setCookiePreference(cookiePreference);
    if (layerMode != null && !"root".equals(layerMode)) {
      // we are handling submission within drawer.
      // accept and close the drawer with new preference
      var layerAction = changedPreference
          ? "X-Up-Accept-Layer"
          : "X-Up-Dismiss-Layer";
      // JSON-encode the value
      responseBuilder.header(layerAction, "\"" + cookiePreference + "\"");
      // ignore html content, just return to previous layer
      responseBuilder.header("X-Up-Target", ":none");

      // when layer is accepted then event will not be emitted, because the
      // source layer no longer exists.
    } else if (changedPreference) {
      // emit an event if the preference has changed
      responseBuilder.header("X-Up-Events", """
         [{"type": "cookie-pref:changed", "value": "%s"}]"""
          .formatted(cookiePreference));
    }
    return responseBuilder.build();
  }
}