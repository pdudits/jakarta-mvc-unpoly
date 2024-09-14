package io.zeromagic.unpolydemo.app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
@Named("appList")
public class AppListModel {
    @Inject
    ApplicationRepository repository;

    public List<AppEntry> apps() {
        return repository.findAllApplications().stream().map(this::convert).toList();
    }

    private  AppEntry convert(Application application) {
        return makeEntry(application.name(), application.endpoint());
    }

    private static AppEntry makeEntry(String name, URI uri) {
        return new AppEntry(name, uri, ThreadLocalRandom.current().nextFloat(0.5f, 0.9f),
                ThreadLocalRandom.current().nextFloat(0.01f, 0.97f));
    }

    public record AppEntry(String name, URI uri, float memoryUtilization, float cpuUtilization) {
        public String formattedMemoryUtilization() {
            return "%.2f".formatted(memoryUtilization * 100);
        }

        public String formattedCpuUtilization() {
            return "%.2f".formatted(cpuUtilization * 100);
        }
    }
}
