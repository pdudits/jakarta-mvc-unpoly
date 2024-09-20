package io.zeromagic.unpolydemo.endpoint;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public boolean is(String value) {
        return Objects.equals(this.value, value);
    }

    public static boolean allValid(FormField... fields) {
        for (var field : fields) {
            if (field != null && !field.inputValid()) {
                return false;
            }
        }
        return true;
    }

    public static FormField blank() {
        return new FormField(null, null, null);
    }

    public static FormField invalid(String value, String message) {
        return new FormField(value, message, false);
    }

    public static FormField undetermined(String value) {
        return new FormField(value, null, null);
    }

    public static <T> T parse(String value, Consumer<FormField> fieldSetter, Function<String, T> parser) {
        try {
            var parsed = parser.apply(value);
            fieldSetter.accept(FormField.valid(value));
            return parsed;
        } catch (IllegalArgumentException e) {
            fieldSetter.accept(FormField.invalid(value, e.getMessage()));
            return null;
        }
    }

    public static <T> Function<String,T> require(String requiredMessage, Function<String, T> parser) {
        return value -> {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(requiredMessage);
            }
            return parser.apply(value);
        };
    }

    public static <T> Function<T,T> check(Consumer<T> check) {
        return value -> {
            check.accept(value);
            return value;
        };
    }

    public static <T> Function<T,T> check(Predicate<T> predicate, String message) {
        return value -> {
            if (!predicate.test(value)) {
                throw new IllegalArgumentException(message);
            }
            return value;
        };
    }

    public static <T> Function<T,T> check(Predicate<T> predicate, Function<T,String> messageComposer) {
        return value -> {
            if (!predicate.test(value)) {
                throw new IllegalArgumentException(messageComposer.apply(value));
            }
            return value;
        };
    }

    public static void parse(String value, Consumer<FormField> fieldSetter, Consumer<String> parser) {
        try {
            parser.accept(value);
            fieldSetter.accept(FormField.valid(value));
        } catch (IllegalArgumentException e) {
            fieldSetter.accept(FormField.invalid(value, e.getMessage()));
        }
    }
}
