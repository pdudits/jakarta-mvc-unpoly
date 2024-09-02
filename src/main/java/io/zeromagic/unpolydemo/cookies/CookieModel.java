package io.zeromagic.unpolydemo.cookies;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Named;

@Model // not overriding name, because there's standard named bean called cookie
public class CookieModel {
  private String cookiePreference;
  private boolean overlay;

  public void setCookiePreference(String cookiePreference) {
    this.cookiePreference = cookiePreference;
  }

  public boolean isForMe() {
    return this.cookiePreference == null || "for-me".equals(this.cookiePreference);
  }

  public boolean isForAll() {
    return "for-all".equals(this.cookiePreference);
  }

  public boolean isForYou() {
    return "for-you".equals(this.cookiePreference);
  }

  public void setOverlay(boolean b) {
    this.overlay = b;
  }

  public boolean isOverlay() {
    return this.overlay;
  }
}
