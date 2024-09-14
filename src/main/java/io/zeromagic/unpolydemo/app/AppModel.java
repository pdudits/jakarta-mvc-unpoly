package io.zeromagic.unpolydemo.app;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SortedSet;

@RequestScoped
@Named("app")
public class AppModel {
    @Inject
    ApplicationRepository repository;

    private String name;
    private List<AppChart> charts;
    private SortedSet<ApplicationEvent> timeline;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.timeline = repository.findTimeline(new Application.Key(name));
    }

    public SortedSet<ApplicationEvent> getTimeline() {
        return timeline;
    }

    public List<AppChart> getCharts() {
        return charts;
    }

    public void setCharts(List<AppChart> charts) {
        this.charts = charts;
    }

    public String formatEventTimestamp(Instant timestamp) {
        return dateFormat.format(timestamp.atOffset(ZoneOffset.UTC));
    }
}
