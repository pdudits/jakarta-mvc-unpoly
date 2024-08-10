package io.zeromagic.unpolydemo.endpoint;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@ApplicationScoped
public class HotReloader {
    @Context
    Sse sse;

    @Inject
    ServletContext context;

    private SseBroadcaster broadcaster;
    private OutboundSseEvent.Builder eventBuilder;
    private volatile boolean running = true;

    @PostConstruct
    void init() {
        this.broadcaster = sse.newBroadcaster();
        this.eventBuilder = sse.newEventBuilder();
        watch();
    }

    @PreDestroy
    void stop() {
        running = false;
    }

    public void register(SseEventSink sink) {
        broadcaster.register(sink);
    }

    private void watch() {
        var root = Path.of(context.getRealPath("/WEB-INF/views"));
        try {
            new Thread(new Watcher(root)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class Watcher implements Runnable {
        private final WatchService watchService;

        Watcher(Path root) throws IOException {
            this.watchService = root.getFileSystem().newWatchService();
            register(root);
            Files.walk(root).filter(Files::isDirectory).forEach(this::register);
        }

        private void register(Path path) {
            try {
                path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void run() {
            while (running) {
                try {
                    var key = watchService.poll(1, TimeUnit.SECONDS);
                    if (key == null) {
                        continue;
                    }
                    key.pollEvents().forEach(event -> {
                        var path = (Path) event.context();
                        var fullPath = ((Path) key.watchable()).resolve(path);
                        if (Files.isDirectory(fullPath)) {
                            register(fullPath);
                        } else {
                            broadcaster.broadcast(eventBuilder.data(path.toString()).build());
                        }
                    });
                    key.reset();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
