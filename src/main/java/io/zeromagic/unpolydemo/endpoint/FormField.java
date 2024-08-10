package io.zeromagic.unpolydemo.endpoint;

public record FormField(String value, String message, Boolean valid) {
    public static FormField valid(String value) {
        return new FormField(value, null, true);
    }

    public String ariaInvalid() {
        return valid != null ? String.valueOf(!valid) : null;
    }

    public boolean inputValid() {
        return valid == null || valid;
    }

    public static FormField blank() {
        return new FormField(null, null, null);
    }

    public static FormField invalid(String value, String message) {
        return new FormField(value, message, false);
    }
}
