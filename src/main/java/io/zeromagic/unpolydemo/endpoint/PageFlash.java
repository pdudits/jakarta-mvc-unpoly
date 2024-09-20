package io.zeromagic.unpolydemo.endpoint;

import jakarta.mvc.RedirectScoped;

import java.io.Serializable;


@RedirectScoped
class PageFlash implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
