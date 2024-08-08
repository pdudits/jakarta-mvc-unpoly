package io.zeromagic.unpolydemo.endpoint;

import jakarta.enterprise.inject.Model;

@Model
public class Page {
    private String title = "Unpoly Demo";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
