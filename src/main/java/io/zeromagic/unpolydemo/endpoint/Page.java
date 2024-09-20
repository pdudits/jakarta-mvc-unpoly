package io.zeromagic.unpolydemo.endpoint;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@Model
public class Page {
  @Inject
  HttpServletRequest request;

  @Inject
  PageFlash flash;

  // this cannot be injected will require a request filter to initialize
  String cookiePreference;

  private String title = "Unpoly Demo";

  public String contextPath() {
    return request.getContextPath();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean analyticsCookiesEnabled() {
    return "for-us".equals(cookiePreference) || marketingCookiesEnabled();
  }

  public boolean marketingCookiesEnabled() {
    return "for-all".equals(cookiePreference);
  }

  public void flash(String message) {
    flash.setMessage(message);
  }

  public boolean hasFlash() {
    return flash.getMessage() != null && !flash.getMessage().isBlank();
  }

  public String flash() {
    return flash.getMessage();
  }
}
