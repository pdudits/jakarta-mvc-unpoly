package io.zeromagic.unpolydemo.datatypes;

import io.zeromagic.unpolydemo.app.AppResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public record JakartaVersion(int version, int... javaVersions) {
    static final List<JakartaVersion> ALLOWED_JAKARTA_VERSIONS = List.of(
            new JakartaVersion(8, 8, 11, 17, 21),
            new JakartaVersion(10, 11, 17, 21),
            new JakartaVersion(11, 21));

    public static JakartaVersion parse(String input) {
        try {
            var asInt = Integer.parseInt(input);
            return ALLOWED_JAKARTA_VERSIONS.stream()
                    .filter(v -> v.version == asInt)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Jakarta EE version"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Jakarta EE version");
        }
    }

    public void checkJavaVersion(String javaVersion) {
        if (javaVersion == null || javaVersion.isBlank()) {
            throw new IllegalArgumentException("Java version is required");
        }
        try {
            var asInt = Integer.parseInt(javaVersion);
            if (IntStream.of(javaVersions).noneMatch(v -> v == asInt)) {
                throw new IllegalArgumentException("Supported java versions are: " + Arrays.toString(javaVersions));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Java version");
        }
    }
}
