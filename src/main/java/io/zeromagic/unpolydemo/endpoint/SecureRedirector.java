package io.zeromagic.unpolydemo.endpoint;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class SecureRedirector implements ContainerResponseFilter {
  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {
    var location = responseContext.getHeaderString("Location");
    var forwardedScheme = requestContext.getHeaderString("X-Forwarded-Proto");
    if (location != null && location.startsWith("http:") &&
        "https".equals(forwardedScheme)) {
      responseContext.getHeaders().putSingle("Location",
          location.replace("http:", "https:"));
    }
  }
}
