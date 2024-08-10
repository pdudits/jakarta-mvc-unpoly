package io.zeromagic.unpolydemo.endpoint;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@Model
public class Page {
    @Inject
    HttpServletRequest request;

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
}
