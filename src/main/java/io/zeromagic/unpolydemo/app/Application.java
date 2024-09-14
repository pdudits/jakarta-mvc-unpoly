package io.zeromagic.unpolydemo.app;

import java.net.URI;
import java.util.List;

public record Application(String name, URI endpoint, List<Revision> revisions) {
    public Key key() {
        return new Key(name);
    }

    record Key(String name) {}
}
